package lt.kazys.currencyconverterapi.service;

import lt.kazys.currencyconverterapi.model.ExchangeData;

public interface CurrencyService {

    ExchangeData getRates(String currencySymbol);
}
