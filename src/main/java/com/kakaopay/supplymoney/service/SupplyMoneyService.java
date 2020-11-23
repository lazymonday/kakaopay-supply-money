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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SupplyMoneyService {
    final int tokenLength = 3;
    final int validPeriodInMin = 10;    // data를 어디에 두지?
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
        Optional<SupplyMoney> supplyMoney = supplyMoneyRepository.findByToken(token);

        supplyMoney.orElseThrow(() -> new SupplyMoneyException(SupplyMoneyStatus.INVALID_TOKEN));
        SupplyMoney spMoney = supplyMoney.get();
        if (spMoney.isExpired()) {
            throw new SupplyMoneyException(SupplyMoneyStatus.EXPIRED_TOKEN);
        } else if (!spMoney.getRoomId().equals(roomId)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.INVALID_ROOM);
        } else if (spMoney.getOwnerId().equals(userId)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.CANT_TAKE_MONEY_ITSELF);
        } else if (spMoney.getTakeMoneyRecords().stream()
                .filter(arg -> arg.getUserId() != null)
                .anyMatch(arg -> arg.getUserId().equals(userId))) {
            throw new SupplyMoneyException(SupplyMoneyStatus.ALREADY_TAKEN);
        } else if (spMoney.getTakeMoneyRecords().stream()
                .noneMatch(arg -> arg.getReceivedAt() == null)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.NO_MORE_MONEY);
        }

        Optional<TakeMoney> takeOne = spMoney.getTakeMoneyRecords().stream()
                .filter(arg -> arg.getReceivedAt() == null).findFirst();
        takeOne.orElseThrow(() -> new SupplyMoneyException(SupplyMoneyStatus.INTERNAL_ERROR));
        takeOne.get().take(userId);
        return new ResponseSupplyMoneyDto(takeOne.get());
    }

    @Transactional
    public ResponseSupplyMoneyDto descSupplyMoney(Long userId, String roomId, String token) {
        Optional<SupplyMoney> supplyMoney = supplyMoneyRepository.findByTokenAndCreatedAtAfter(token, OffsetDateTime.now().minusDays(7));
        supplyMoney.orElseThrow(() -> new SupplyMoneyException(SupplyMoneyStatus.EXPIRED_TOKEN));
        if (!supplyMoney.get().getOwnerId().equals(userId)) {
            throw new SupplyMoneyException(SupplyMoneyStatus.INVALID_OWNER);
        }

        return new ResponseSupplyMoneyDto(supplyMoney.get());
    }

    private List<TakeMoney> divideMoney(SupplyMoney supplyMoney) {
        assert (supplyMoney.getTotalMoney() >= supplyMoney.getShareCount());

        List<TakeMoney> takeMoneyEachList = new ArrayList<>();
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
