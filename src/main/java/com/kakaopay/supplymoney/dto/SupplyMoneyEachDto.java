package com.kakaopay.supplymoney.dto;

import com.kakaopay.supplymoney.domain.TakeMoney;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SupplyMoneyEachDto {
    Long userId;
    Long takenMoney;

    public static SupplyMoneyEachDto of(TakeMoney takeMoney) {
        return SupplyMoneyEachDto.builder()
                .takenMoney(takeMoney.getMoneyAmount())
                .userId(takeMoney.getUserId()).build();
    }
}
