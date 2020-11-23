package com.kakaopay.supplymoney;

import com.kakaopay.supplymoney.domain.SupplyMoney;
import com.kakaopay.supplymoney.domain.TakeMoney;
import com.kakaopay.supplymoney.dto.RequestSupplyMoneyDto;

public class SampleData {
    public final static Long ownerId = 1827L;
    public final static Long takenUserId = 1800L;
    public final static Long totalSupplyMoney = 101L;
    public final static Long shareCount = 3L;
    public final static Long availablePeriodInMin = 10L;
    public final static String roomId = "78P8fj1k";
    public final static String token = "QWE";

    public static SupplyMoney sampleData() {
        RequestSupplyMoneyDto req = RequestSupplyMoneyDto.builder()
                .userId(ownerId)
                .roomId(roomId)
                .shareCount(shareCount)
                .totalMoney(totalSupplyMoney)
                .build();

        SupplyMoney supplyMoney = new SupplyMoney(token, req, availablePeriodInMin);
        TakeMoney taken = new TakeMoney(supplyMoney, 30L);
        taken.take(takenUserId);
        supplyMoney.getTakeMoneyRecords().add(taken);
        supplyMoney.getTakeMoneyRecords().add(new TakeMoney(supplyMoney, 24L));
        supplyMoney.getTakeMoneyRecords().add(new TakeMoney(supplyMoney, 46L));
        return supplyMoney;
    }
}
