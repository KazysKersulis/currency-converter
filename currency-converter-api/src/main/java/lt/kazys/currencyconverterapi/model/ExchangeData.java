package lt.kazys.currencyconverterapi.model;

import lombok.Builder;

import java.util.Map;

public class ExchangeData {

    private final Map<String, Double> rates;
    private final String base;
    private final String date;

    @Builder
    public ExchangeData(Map<String, Double> rates, String base, String date) {
        this.rates = rates;
        this.base = base;
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

}
