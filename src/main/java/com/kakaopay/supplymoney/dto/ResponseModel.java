package com.kakaopay.supplymoney.dto;

import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.exception.SupplyMoneyException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel {
    private String errCode;
    private String errMsg;
    private Object result;

    public ResponseModel(SupplyMoneyStatus status, Object anyObj) {
        super();
        this.errCode = status.getCode();
        this.errMsg = status.getDesc();
        this.result = anyObj;
    }

    public ResponseModel(String errCode, String errMsg, Object anyObj) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.result = anyObj;
    }

    public ResponseModel(SupplyMoneyException e) {
        errCode = e.getErrCode();
        errMsg = e.getErrMsg();
    }

    public static ResponseModel of(Exception e) {
        return new ResponseModel(SupplyMoneyStatus.INTERNAL_ERROR, null);
    }
}
