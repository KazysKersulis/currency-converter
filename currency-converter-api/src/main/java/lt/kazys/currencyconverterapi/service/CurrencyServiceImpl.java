package lt.kazys.currencyconverterapi.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lt.kazys.currencyconverterapi.model.CurrencySymbols;
import lt.kazys.currencyconverterapi.model.ExchangeData;
import lt.kazys.currencyconverterapi.web.response.ExchangeRatesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Value("${openexchangeapi.root_uri}")
    private String rootUri;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, ExchangeData> currRatesCache = Maps.newHashMap();
    private RestTemplate exchangeRatesApi;
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    public CurrencyServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.exchangeRatesApi = restTemplateBuilder
                .rootUri(rootUri)
                .build();
    }

    @PostConstruct
    public void initService() {
        reloadCache();
    }

    @Scheduled(cron = "${refresh.cron}")
    public void reloadCache() {
        List<ExchangeData> exchangeDataRates = null;
        LOG.info("Refreshing live Euro cross rates from url ->" + this.rootUri);
        try {
            exchangeDataRates = fetchExchangeDataRates(rootUri);
            LOG.info(String.format("Found %d live rates", exchangeDataRates.size()));
        } catch (RuntimeException e) {
            LOG.error("error encountered whilst fetching live rates", e);
        }
        Lock writeLock = this.lock.writeLock();
        writeLock.lock();
        try {
            if (exchangeDataRates != null) {
                this.currRatesCache.clear();
                for (ExchangeData rate : exchangeDataRates) {
                    this.currRatesCache.put(rate.getBase(), rate);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public List<ExchangeData> fetchExchangeDataRates(String uri) throws RuntimeException {
        List<ExchangeData> exchangeDataList = Lists.newArrayList();

        for (CurrencySymbols currencySymbol : CurrencySymbols.values()) {
            ResponseEntity<ExchangeRatesResponse> response = exchangeRatesApi.getForEntity(rootUri + "/latest?base=" + currencySymbol, ExchangeRatesResponse.class);
            exchangeDataList.add(toExchangeData(response.getBody()));
        }

        return exchangeDataList;
    }

    public ExchangeData toExchangeData(ExchangeRatesResponse response) {
        return new ExchangeData(response.rates, response.base, response.date);
    }

    @Override
    public ExchangeData getRates(String currencySymbol) {
        Lock readLock = this.lock.readLock();
        readLock.lock();
        try {
            return this.currRatesCache.get(currencySymbol.toUpperCase());
        } finally {
            readLock.unlock();
            LOG.error("error encountered whilst fetching live rates for -> " + currencySymbol);
        }
    }
}
