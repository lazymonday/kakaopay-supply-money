package com.kakaopay.supplymoney.exception;

import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import lombok.Getter;

@Getter
public class SupplyMoneyException extends RuntimeException {
    private final String errCode;
    private final String errMsg;

    public SupplyMoneyException(SupplyMoneyStatus errStatus) {
        this.errCode = errStatus.getCode();
        this.errMsg = errStatus.getDesc();
    }
}
