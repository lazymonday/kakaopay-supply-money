package com.kakaopay.supplymoney.service;

import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.domain.SupplyMoney;
import com.kakaopay.supplymoney.domain.SupplyMoneyRepository;
import com.kakaopay.supplymoney.domain.TakeMoney;
import com.kakaopay.supplymoney.domain.TakeMoneyRepository;
import com.kakaopay.supplymoney.dto.RequestSupplyMoneyDto;
import com.kakaopay.supplymoney.dto.ResponseSupplyMoneyDto;
import com.kakaopay.supplymoney.exception.SupplyMoneyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SupplyMoneyService {
    final int tokenLength = 3;
    final int validPeriodInMin = 10;    // data를 어디에 두지?
    final Random random;
    private final SupplyMoneyRepository supplyMoneyRepository;
    private final TakeMoneyRepository takeMoneyRepository;

    @Transactional
    public String supplyMoney(RequestSupplyMoneyDto requestSupplyMoneyDto) {
        if (requestSupplyMoneyDto.getTotalMoney() <= 1 || requestSupplyMoneyDto.getShareCount() <= 1) {
            throw new SupplyMoneyException(SupplyMoneyStatus.INVALID_ARGUMENT);
        }

        if (requestSupplyMoneyDto.getTotalMoney() < requestSupplyMoneyDto.getShareCount()) {
            throw new SupplyMoneyException(SupplyMoneyStatus.NOT_ENOUGH_MONEY);
        }

        String token = TokenService.createRandomToken(tokenLength);
        SupplyMoney supplyMoney = new SupplyMoney(token, requestSupplyMoneyDto, validPeriodInMin);
        supplyMoneyRepository.save(supplyMoney);
        takeMoneyRepository.saveAll(divideMoney(supplyMoney));
        return token;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseSupplyMoneyDto takeMoney(Long userId, String roomId, String token) {
        SupplyMoney supplyMoney = supplyMoneyRepository.findByToken(token);

        if (supplyMoney == null) {
            throw new SupplyMoneyException(SupplyMoneyStatus.INVALID_TOKEN);
        }

        if (supplyMoney.isExpired()) {
            throw new SupplyMoneyException(SupplyMoneyStatus.EXPIRED_TOKEN);
        } else if (!supplyMoney.getRoomId().equals(roomId)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.INVALID_ROOM);
        } else if (supplyMoney.getOwnerId().equals(userId)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.CANT_TAKE_MONEY_ITSELF);
        } else if (supplyMoney.getTakeMoneyRecords().stream()
                .filter(arg -> arg.getUserId() != null)
                .anyMatch(arg -> arg.getUserId().equals(userId))) {
            throw new SupplyMoneyException(SupplyMoneyStatus.ALREADY_TAKEN);
        } else if (supplyMoney.getTakeMoneyRecords().stream()
                .noneMatch(arg -> arg.getReceivedAt() == null)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.NO_MORE_MONEY);
        }

        List<TakeMoney> takeAbles = supplyMoney.getTakeMoneyRecords().stream()
                .filter(arg -> arg.getReceivedAt() == null)
                .collect(Collectors.toList());

        TakeMoney takeOne = takeAbles.get(0);
        takeOne.take(userId);
        return new ResponseSupplyMoneyDto(takeOne);
    }

    @Transactional
    public ResponseSupplyMoneyDto descSupplyMoney(Long userId, String roomId, String token) {

        SupplyMoney supplyMoney = supplyMoneyRepository.findByTokenAndCreatedAtAfter(token, OffsetDateTime.now().minusDays(7));
        if(supplyMoney == null) {
            throw new SupplyMoneyException(SupplyMoneyStatus.EXPIRED_TOKEN);
        }

        if(!supplyMoney.getOwnerId().equals(userId)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.INVALID_OWNER);
        }

        return new ResponseSupplyMoneyDto(supplyMoney);
    }

    private List<TakeMoney> divideMoney(SupplyMoney supplyMoney) {
        assert (supplyMoney.getTotalMoney() >= supplyMoney.getShareCount());

        ArrayList<TakeMoney> takeMoneyEachList = new ArrayList<>();
        Long consumeMoney = supplyMoney.getShareCount();
        final Long minimalMoney = 1L;
        for (int i = 0; i < supplyMoney.getShareCount() - 1; ++i) {
            long availableMoney = supplyMoney.getTotalMoney() - consumeMoney;
            Long moneyAmount = Math.round(Math.random() * availableMoney);
            takeMoneyEachList.add(new TakeMoney(supplyMoney, minimalMoney + moneyAmount));
            consumeMoney += moneyAmount;
        }

        takeMoneyEachList.add(new TakeMoney(supplyMoney, minimalMoney + supplyMoney.getTotalMoney() - consumeMoney));
        return takeMoneyEachList;
    }
}
