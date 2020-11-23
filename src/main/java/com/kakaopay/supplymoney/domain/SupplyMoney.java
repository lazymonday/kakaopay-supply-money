package com.kakaopay.supplymoney.domain;

import com.kakaopay.supplymoney.dto.RequestSupplyMoneyDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(columnList = "token, ownerId, roomId, expireAt", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupplyMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;
    @Column(nullable = false)
    private String roomId;
    @Column(nullable = false, length = 3)
    private String token;
    @Column(nullable = false)
    private Long totalMoney;
    @Column(nullable = false)
    private Long shareCount;
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    @Column(nullable = false)
    private OffsetDateTime expireAt;
    @OneToMany(mappedBy = "supplyMoney", cascade = CascadeType.ALL)
    private List<TakeMoney> takeMoneyRecords = new ArrayList<>();

    public SupplyMoney(String token, RequestSupplyMoneyDto requestSupplyMoneyDto, long availablePeriodInMin) {
        this.token = token;
        this.ownerId = requestSupplyMoneyDto.getUserId();
        this.roomId = requestSupplyMoneyDto.getRoomId();
        this.totalMoney = requestSupplyMoneyDto.getTotalMoney();
        this.shareCount = requestSupplyMoneyDto.getShareCount();
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.expireAt = createdAt.plusMinutes(availablePeriodInMin);
    }

    public boolean isExpired() {
        return expireAt.isBefore(OffsetDateTime.now(ZoneOffset.UTC));
    }
}
