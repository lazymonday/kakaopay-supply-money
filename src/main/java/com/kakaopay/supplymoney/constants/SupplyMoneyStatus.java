package com.kakaopay.supplymoney.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SupplyMoneyStatus {
    SUCCESS("000", "Operation Success"),

    NOT_ENOUGH_MONEY("100", "Not enough to share money"),
    INVALID_ARGUMENT("101", "Amount of money or Number of people to share is invalid"),
    CANT_TAKE_MONEY_ITSELF("102", "Can't take money yourself."),
    ALREADY_TAKEN("103", "Already taken money"),
    NO_MORE_MONEY("104", "There is no money to take"),
    EXPIRED_TOKEN("105", "Supplied money has been expired"),
    INVALID_TOKEN("106", "Given token is invalid"),
    INVALID_ROOM("107", "Invalid room with given token"),
    INVALID_OWNER("108", "Invalid owner of supply money."),
    BAD_REQUEST("400", "Bad request with invalid header."),
    INTERNAL_ERROR("500", "Internal Error"),
    ;

    private final String code;
    private final String desc;
}
