package com.kakaopay.supplymoney.domain;

import com.kakaopay.supplymoney.domain.SupplyMoney;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface SupplyMoneyRepository extends JpaRepository<SupplyMoney, Long> {
    SupplyMoney findByToken(String token);
    SupplyMoney findByTokenAndCreatedAtAfter(String token, OffsetDateTime now);
}
