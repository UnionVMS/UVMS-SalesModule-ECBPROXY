package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import com.google.common.collect.ComparisonChain;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.LocalDate;

@EqualsAndHashCode
@ToString
public class ExchangeRateKey implements Comparable<ExchangeRateKey> {

    private final LocalDate date;
    private final String sourceCurrency;
    private final String targetCurrency;

    public ExchangeRateKey(LocalDate date, String sourceCurrency, String targetCurrency) {
        this.date = date;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    @Override
    public int compareTo(ExchangeRateKey otherExchangeRateKey) {
        return ComparisonChain
                .start()
                .compare(this.date, otherExchangeRateKey.date)
                .result();
    }

}
