package com.kakaopay.supplymoney.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface SupplyMoneyRepository extends JpaRepository<SupplyMoney, Long> {
    Optional<SupplyMoney> findByToken(String token);

    Optional<SupplyMoney> findByTokenAndCreatedAtAfter(String token, OffsetDateTime now);
}
