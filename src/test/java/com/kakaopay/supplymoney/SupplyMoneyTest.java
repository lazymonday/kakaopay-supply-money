package com.kakaopay.supplymoney;

import com.kakaopay.supplymoney.constants.SupplyMoneyStatus;
import com.kakaopay.supplymoney.domain.SupplyMoneyRepository;
import com.kakaopay.supplymoney.domain.TakeMoneyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SupplyMoneyTest extends SupplyMoneyCoreTest {

    @MockBean
    private SupplyMoneyRepository supplyMoneyRepository;

    @MockBean
    private TakeMoneyRepository takeMoneyRepository;

    @Test
    void T01_돈_뿌리기_성공() throws Exception {
        // then
        supplyMoney(SampleData.ownerId, SampleData.roomId, SampleData.totalSupplyMoney, SampleData.shareCount)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.SUCCESS.getCode())))
                .andExpect(jsonPath("$.result.token", hasLength(3)));
    }

    @Test
    void T02_돈_뿌리기_실패_01() throws Exception {
        // then
        supplyMoney(SampleData.ownerId, SampleData.roomId, SampleData.totalSupplyMoney, 0L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.INVALID_ARGUMENT.getCode())));
    }

    @Test
    void T03_돈_뿌리기_실패_02() throws Exception {
        // then
        supplyMoney(SampleData.ownerId, SampleData.roomId, 100L, 101L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errCode", is(SupplyMoneyStatus.NOT_ENOUGH_MONEY.getCode())));
    }
}
