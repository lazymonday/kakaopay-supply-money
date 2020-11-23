package com.kakaopay.supplymoney.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaopay.supplymoney.domain.SupplyMoney;
import com.kakaopay.supplymoney.domain.TakeMoney;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ResponseSupplyMoneyDto {
    private final String token;
    private final String createdAt;
    private final String expireAt;
    private final Long totalMoney;
    private final Long takenMoney;
    private final List<SupplyMoneyEachDto> takenList;

    public static ResponseSupplyMoneyDto of(SupplyMoney supplyMoney) {
        List<SupplyMoneyEachDto> eachMoney = new ArrayList<>();
        Long[] totalTakenMoney = {0L,};
        supplyMoney.getTakeMoneyRecords().forEach(arg -> {
            eachMoney.add(SupplyMoneyEachDto.of(arg));
            if (arg.getUserId() != null) {
                totalTakenMoney[0] += arg.getMoneyAmount();
            }
        });

        return ResponseSupplyMoneyDto.builder()
                .token(supplyMoney.getToken())
                .createdAt(supplyMoney.getCreatedAt().toLocalDateTime().toString())
                .expireAt(supplyMoney.getExpireAt().toLocalDateTime().toString())
                .totalMoney(supplyMoney.getTotalMoney())
                .takenMoney(totalTakenMoney[0])
                .takenList(eachMoney)
                .build();
    }

    public static ResponseSupplyMoneyDto of(String token) {
        return ResponseSupplyMoneyDto.builder()
                .token(token)
                .build();
    }

    public static ResponseSupplyMoneyDto of(TakeMoney takeMoney) {
        return ResponseSupplyMoneyDto.builder()
                .takenMoney(takeMoney.getMoneyAmount())
                .build();
    }
}
