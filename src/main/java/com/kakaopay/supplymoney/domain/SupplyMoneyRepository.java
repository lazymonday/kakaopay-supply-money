package com.kakaopay.supplymoney.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface SupplyMoneyRepository extends JpaRepository<SupplyMoney, Long> {
    Optional<SupplyMoney> findByTokenAndRoomIdAndExpireAtAfter(String token, String roomId, OffsetDateTime now);

    Optional<SupplyMoney> findByTokenAndOwnerIdAndRoomIdAndCreatedAtAfter(String token, Long owenrId, String RoomId, OffsetDateTime now);

    int countByTokenAndOwnerIdAndRoomIdAndExpireAtBefore(String token, Long userId, String roomId, OffsetDateTime now);
}
