package com.kakaopay.supplymoney.controller;

import com.kakaopay.supplymoney.constants.Header;
import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.dto.ResponseModel;
import com.kakaopay.supplymoney.dto.RequestSupplyMoneyDto;
import com.kakaopay.supplymoney.dto.ResponseSupplyMoneyDto;
import com.kakaopay.supplymoney.service.SupplyMoneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SupplyMoneyController {

    private final SupplyMoneyService supplyMoneyService;

    @PostMapping("/api/v1/kakaopay/supplymoney")
    ResponseEntity<ResponseModel> supplyMoney(@RequestHeader(Header.USER_ID) Long userId,
                                              @RequestHeader(Header.ROOM_ID) String roomId,
                                              @RequestBody RequestSupplyMoneyDto requestSupplyMoneyDto) {
        requestSupplyMoneyDto.setUserId(userId);
        requestSupplyMoneyDto.setRoomId(roomId);

        ResponseSupplyMoneyDto responseSupplyMoneyDto = new ResponseSupplyMoneyDto();
        responseSupplyMoneyDto.setToken(supplyMoneyService.supplyMoney(requestSupplyMoneyDto));

        ResponseModel response = new ResponseModel(SupplyMoneyStatus.SUCCESS, responseSupplyMoneyDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/v1/kakaopay/takemoney/{token}")
    ResponseEntity<ResponseModel> takeMoney(@RequestHeader(Header.USER_ID) Long userId,
                                            @RequestHeader(Header.ROOM_ID) String roomId,
                                            @PathVariable String token) {

        ResponseSupplyMoneyDto resultObj = supplyMoneyService.takeMoney(userId, roomId, token);
        ResponseModel response = new ResponseModel(SupplyMoneyStatus.SUCCESS, resultObj);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/kakaopay/descsupplymoney/{token}")
    ResponseEntity<ResponseModel> descSupplyMoney(@RequestHeader(Header.USER_ID) Long userId,
                                                 @RequestHeader(Header.ROOM_ID) String roomId,
                                                 @PathVariable String token) {

        ResponseSupplyMoneyDto resultObj = supplyMoneyService.descSupplyMoney(userId, roomId, token);
        ResponseModel response = new ResponseModel(SupplyMoneyStatus.SUCCESS, resultObj);
        return ResponseEntity.ok(response);
    }
}