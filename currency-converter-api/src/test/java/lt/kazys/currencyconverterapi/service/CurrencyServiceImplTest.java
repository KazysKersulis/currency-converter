package lt.kazys.currencyconverterapi.service;

import lt.kazys.currencyconverterapi.model.ExchangeData;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    private ExchangeData exchangeData;

    @Mock
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        exchangeData = ExchangeData.builder().rates(Maps.newHashMap("CHF", 1.0548)).base("EUR").date("2020-03-15").build();
    }

    @Test
    void getRates() {
        when(currencyService.getRates("EUR")).thenReturn(exchangeData);
        ExchangeData exchangeData = currencyService.getRates("EUR");
        assertEquals(1.0548, exchangeData.getRates().get("CHF").doubleValue());
    }
}