package lt.kazys.currencyconverterapi.web;

import lt.kazys.currencyconverterapi.model.ExchangeData;
import lt.kazys.currencyconverterapi.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/v1/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping(value = "/rates/{currencySymbol}")
    public ExchangeData getRates(@PathVariable(value="currencySymbol") String currencySymbol) {
        return currencyService.getRates(currencySymbol);
    }
}
