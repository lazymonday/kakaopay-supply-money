package com.kakaopay.supplymoney.dto;

import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.exception.SupplyMoneyException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel<T> implements IResponseModel {
    private String errCode;
    private String errMsg;
    private T result;

    public ResponseModel(SupplyMoneyStatus status, T anyObj) {
        super();
        this.errCode = status.getCode();
        this.errMsg = status.getDesc();
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
