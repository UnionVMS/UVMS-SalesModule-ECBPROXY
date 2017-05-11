package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
public class ExchangeRate {

    private ExchangeRateKey key;
    private BigDecimal rate;

    public ExchangeRate() {

    }

    public ExchangeRate(BigDecimal rate, String sourceCurrency, String targetCurrency, LocalDate date) {
        this.key = new ExchangeRateKey(date, sourceCurrency, targetCurrency);
        this.rate = rate;
    }

    public ExchangeRateKey getKey() {
        return key;
    }

    public void setKey(ExchangeRateKey key) {
        this.key = key;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }


    public ExchangeRate key(ExchangeRateKey key) {
        this.key = key;
        return this;
    }

    public ExchangeRate rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }
}
