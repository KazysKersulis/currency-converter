package lt.kazys.currencyconverterapi.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRatesResponse {
    public Map<String, Double> rates;
    public String base;
    public String date;
}
