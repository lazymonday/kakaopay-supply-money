package com.kakaopay.supplymoney.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "supply_id, id", unique = true)
})
public class TakeMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long moneyAmount;

    private Long userId;
    private OffsetDateTime receivedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supply_id")
    private SupplyMoney supplyMoney;

    public TakeMoney(SupplyMoney supplyMoney, Long moneyAmount) {
        super();
        this.supplyMoney = supplyMoney;
        this.moneyAmount = moneyAmount;
    }

    public long take(Long takenUserId) {
        this.userId = takenUserId;
        receivedAt = OffsetDateTime.now(ZoneOffset.UTC);
        return moneyAmount;
    }
}
