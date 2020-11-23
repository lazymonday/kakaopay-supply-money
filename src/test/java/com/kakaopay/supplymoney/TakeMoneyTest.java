package com.kakaopay.supplymoney;

import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.domain.SupplyMoney;
import com.kakaopay.supplymoney.domain.SupplyMoneyRepository;
import com.kakaopay.supplymoney.domain.TakeMoneyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TakeMoneyTest extends SupplyMoneyCoreTest {
    @MockBean
    private SupplyMoneyRepository supplyMoneyRepository;

    @MockBean
    private TakeMoneyRepository takeMoneyRepository;

    @Test
    void T01_돈_받기_성공_01() throws Exception {

        when(supplyMoneyRepository.findByToken(anyString())).thenReturn(Optional.of((SampleData.sampleData())));

        takeMoney(SampleData.token, 1890L, SampleData.roomId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.SUCCESS.getCode())))
                .andExpect(jsonPath("$.result.takenMoney", is(24L), Long.class));
    }

    @Test
    void T02_돈_받기_실패_01() throws Exception {

        when(supplyMoneyRepository.findByToken(anyString())).thenReturn(Optional.of(SampleData.sampleData()));

        takeMoney(SampleData.token, SampleData.takenUserId, SampleData.roomId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.ALREADY_TAKEN.getCode())));
    }

    @Test
    void T03_돈_받기_실패_02() throws Exception {
        SupplyMoney sample = SampleData.sampleData();
        sample.getTakeMoneyRecords().get(1).take(2000L);
        sample.getTakeMoneyRecords().get(2).take(3000L);

        when(supplyMoneyRepository.findByToken(anyString())).thenReturn(Optional.of(sample));

        takeMoney(SampleData.token, 4000L, SampleData.roomId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.NO_MORE_MONEY.getCode())));
    }

    @Test
    void T04_돈_받기_실패_03() throws Exception {
        when(supplyMoneyRepository.findByToken(anyString())).thenReturn(Optional.of(SampleData.sampleData()));

        takeMoney(SampleData.token, SampleData.ownerId, SampleData.roomId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.CANT_TAKE_MONEY_ITSELF.getCode())));
    }

    @Test
    @DisplayName("유효기간이 지난 토큰에 대한 뿌리기는 돈을 받을 수 없음")
    void T05_돈_받기_실패_04() throws Exception {
        SupplyMoney supplyMoney = Mockito.mock(SupplyMoney.class);
        when(supplyMoney.isExpired()).thenReturn(true);
        ;
        when(supplyMoneyRepository.findByToken(anyString())).thenReturn(Optional.of(supplyMoney));

        takeMoney(SampleData.token, 2000L, SampleData.roomId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.EXPIRED_TOKEN.getCode())));
    }
}
