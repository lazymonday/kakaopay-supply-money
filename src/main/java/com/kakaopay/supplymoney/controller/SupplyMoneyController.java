package com.kakaopay.supplymoney.controller;

import com.kakaopay.supplymoney.constants.Header;
import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.dto.IResponseModel;
import com.kakaopay.supplymoney.dto.RequestSupplyMoneyDto;
import com.kakaopay.supplymoney.dto.ResponseModel;
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
    ResponseEntity<IResponseModel> supplyMoney(@RequestHeader(Header.USER_ID) Long userId,
                                               @RequestHeader(Header.ROOM_ID) String roomId,
                                               @RequestBody RequestSupplyMoneyDto requestSupplyMoneyDto) {
        requestSupplyMoneyDto.setUserId(userId);
        requestSupplyMoneyDto.setRoomId(roomId);

        return ResponseEntity.ok(new ResponseModel<>(SupplyMoneyStatus.SUCCESS,
                ResponseSupplyMoneyDto.of(supplyMoneyService.supplyMoney(requestSupplyMoneyDto))));
    }

    @PutMapping("/api/v1/kakaopay/takemoney/{token}")
    ResponseEntity<IResponseModel> takeMoney(@RequestHeader(Header.USER_ID) Long userId,
                                             @RequestHeader(Header.ROOM_ID) String roomId,
                                             @PathVariable String token) {

        return ResponseEntity.ok(new ResponseModel<>(SupplyMoneyStatus.SUCCESS,
                supplyMoneyService.takeMoney(userId, roomId, token)));
    }

    @GetMapping("/api/v1/kakaopay/descsupplymoney/{token}")
    ResponseEntity<IResponseModel> descSupplyMoney(@RequestHeader(Header.USER_ID) Long userId,
                                                   @RequestHeader(Header.ROOM_ID) String roomId,
                                                   @PathVariable String token) {

        return ResponseEntity.ok(new ResponseModel<>(SupplyMoneyStatus.SUCCESS,
                supplyMoneyService.descSupplyMoney(userId, roomId, token)));
    }
}