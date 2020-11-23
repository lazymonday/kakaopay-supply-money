package com.kakaopay.supplymoney.exception;

import com.kakaopay.supplymoney.dto.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SupplyMoneyExceptionHandler {

    @ExceptionHandler(SupplyMoneyException.class)
    public ResponseEntity<ResponseModel> handleSupplyMoneyException(SupplyMoneyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel> handleUnauthorizedException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.of(e));
    }
}
