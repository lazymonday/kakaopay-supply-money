package com.kakaopay.supplymoney;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.supplymoney.constants.Header;
import com.kakaopay.supplymoney.dto.RequestSupplyMoneyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
abstract public class SupplyMoneyCoreTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    public ResultActions supplyMoney(Long userId, String roomId, Long totalMoney, Long shareCount) throws Exception {
        RequestSupplyMoneyDto req = RequestSupplyMoneyDto.builder()
                .totalMoney(totalMoney)
                .shareCount(shareCount)
                .build();

        return mockMvc.perform(
                post("/api/v1/kakaopay/supplymoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Header.ROOM_ID, roomId)
                        .header(Header.USER_ID, userId)
                        .content(mapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                ;
    }

    public ResultActions takeMoney(String token, Long userId, String roomId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("/api/v1/kakaopay/takemoney/").append(token);
        return mockMvc.perform(
                put(sb.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Header.ROOM_ID, roomId)
                        .header(Header.USER_ID, userId))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                ;
    }

    public ResultActions fetchSupplyMoney(String token, Long userId, String roomId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("/api/v1/kakaopay/supplymoney/").append(token);
        return mockMvc.perform(
                get(sb.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Header.USER_ID, userId)
                        .header(Header.ROOM_ID, roomId))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                ;
    }
}
