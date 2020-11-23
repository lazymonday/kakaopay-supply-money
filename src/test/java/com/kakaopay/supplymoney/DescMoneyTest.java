package com.kakaopay.supplymoney;

import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.domain.SupplyMoneyRepository;
import com.kakaopay.supplymoney.domain.TakeMoneyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DescMoneyTest extends SupplyMoneyCoreTest {

    @MockBean
    private SupplyMoneyRepository supplyMoneyRepository;

    @MockBean
    private TakeMoneyRepository takeMoneyRepository;

    @Test
    void T01_뿌린_돈_조회_성공() throws Exception {

        when(supplyMoneyRepository.findByTokenAndCreatedAtAfter(anyString(), any(OffsetDateTime.class))).
                thenReturn(Optional.of(SampleData.sampleData()));

        this.descSupplyMoney(SampleData.token, SampleData.ownerId, SampleData.roomId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.SUCCESS.getCode())))
                .andExpect(jsonPath("$.result.token", is(SampleData.token)))
                .andExpect(jsonPath("$.result.totalMoney", is(SampleData.totalSupplyMoney), Long.class))
                .andExpect(jsonPath("$.result.takenList", hasSize(Math.toIntExact(SampleData.shareCount))))
        ;
    }

    @Test
    void T02_뿌린_돈_조회_실패() throws Exception {

        when(supplyMoneyRepository.findByTokenAndCreatedAtAfter(anyString(), any(OffsetDateTime.class))).
                thenReturn(Optional.of(SampleData.sampleData()));

        this.descSupplyMoney(SampleData.token, 1234L, SampleData.roomId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.INVALID_OWNER.getCode())))
        ;
    }

    @Test
    void T03_뿌린_돈_조회_실패() throws Exception {
        when(supplyMoneyRepository.findByTokenAndCreatedAtAfter(anyString(), any(OffsetDateTime.class))).
                thenReturn(Optional.ofNullable(null));

        this.descSupplyMoney(SampleData.token, SampleData.ownerId, SampleData.roomId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.EXPIRED_TOKEN.getCode())))
        ;
    }
}
