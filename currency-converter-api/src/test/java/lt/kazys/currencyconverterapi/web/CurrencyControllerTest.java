package lt.kazys.currencyconverterapi.web;

import lt.kazys.currencyconverterapi.model.ExchangeData;
import lt.kazys.currencyconverterapi.service.CurrencyService;
import org.assertj.core.util.Maps;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    CurrencyController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void getRates() throws Exception {
        ExchangeData exchangeData = ExchangeData.builder().rates(Maps.newHashMap("CHF", 1.0548)).base("EUR").date("2020-03-15").build();
        when(currencyService.getRates(anyString())).thenReturn(exchangeData);
        String expected = "{\"rates\":{\"CHF\":1.0548},\"base\":\"EUR\",\"date\":\"2020-03-15\"}";
        mockMvc.perform(get("/v1/currencies/rates/CHF"))
                .andExpect(status().isOk()).andExpect(content().string(expected));
    }

}