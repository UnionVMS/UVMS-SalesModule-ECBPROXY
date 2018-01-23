package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
public class ExchangeRate {

    private BigDecimal rate;
    private String sourceCurrency;
    private String targetCurrency;
    private LocalDate startDate;

    public ExchangeRate() {

    }

    public ExchangeRate(BigDecimal rate, String sourceCurrency, String targetCurrency, LocalDate startDate) {
        this.rate = rate;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.startDate = startDate;
    }


    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getSourceCurrency() { return sourceCurrency; }

    public void setSourceCurrency(String sourceCurrency) { this.sourceCurrency = sourceCurrency; }

    public String getTargetCurrency() { return targetCurrency; }

    public void setTargetCurrency(String targetCurrency) { this.targetCurrency = targetCurrency; }

    public LocalDate getStartDate() { return startDate; }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public ExchangeRate rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public ExchangeRate sourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public ExchangeRate targetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public ExchangeRate targetCurrency(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

}
