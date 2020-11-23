package com.kakaopay.supplymoney.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestSupplyMoneyDto {
    private Long userId;
    private String roomId;
    private Long totalMoney;
    private Long shareCount;
}
