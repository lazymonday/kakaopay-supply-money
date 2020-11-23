package com.kakaopay.supplymoney.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaopay.supplymoney.domain.SupplyMoney;
import com.kakaopay.supplymoney.domain.TakeMoney;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ResponseSupplyMoneyDto {
    private String token;
    private String createdAt;
    private String expireAt;
    private Long totalMoney;
    private Long takenMoney;
    private List<SupplyMoneyEachDto> takenList;

    public ResponseSupplyMoneyDto(SupplyMoney supplyMoney) {
        this.token = supplyMoney.getToken();
        this.createdAt = supplyMoney.getCreatedAt().toLocalDateTime().toString();
        this.expireAt = supplyMoney.getExpireAt().toLocalDateTime().toString();
        this.totalMoney = supplyMoney.getTotalMoney();
        this.takenMoney = 0L;
        this.takenList = new ArrayList<>();
        supplyMoney.getTakeMoneyRecords().forEach(arg -> {
            takenList.add(SupplyMoneyEachDto.of(arg));
            if (arg.getUserId() != null) {
                this.takenMoney += arg.getMoneyAmount();
            }
        });
    }

    public ResponseSupplyMoneyDto(TakeMoney takeMoney) {
        this.takenMoney = takeMoney.getMoneyAmount();
    }
}
