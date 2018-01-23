package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;

@Slf4j
@Entity
@Table(name = "exchange_rate")
@SequenceGenerator( name = "exchange_rate_id_seq",
        sequenceName = "exchange_rate_id_seq",
        allocationSize = 50)
@EqualsAndHashCode
@ToString
@NamedQueries({
        @NamedQuery(name = ExchangeRateEntity.FIND_BY_TILL_DATE, query = "SELECT s FROM ExchangeRateEntity s WHERE s.startDateTime <= :startDateTime AND s.sourceCurrency = :sourceCurrency AND s.targetCurrency = :targetCurrency ORDER BY s.startDateTime DESC"),
        @NamedQuery(name = ExchangeRateEntity.FIND_BY_MOST_RECENT_RATE_DATE, query = "SELECT s FROM ExchangeRateEntity s ORDER BY s.startDateTime DESC")
})
public class ExchangeRateEntity {

    public static final String FIND_BY_TILL_DATE = "ExchangeRateEntity.findByDate";
    public static final String FIND_BY_MOST_RECENT_RATE_DATE = "ExchangeRateEntity.findByMostRecentRateDate";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "exchange_rate_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "start_date", nullable = false)
    private DateTime startDateTime;

    @Column(name = "source_currency", nullable = false)
    private String sourceCurrency;

    @Column(name = "target_currency", nullable = false)
    private String targetCurrency;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "creation", nullable = false)
    private DateTime creationDateTime;

    public ExchangeRateEntity() {
    }

    public Integer getId() {
        return id;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public DateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setCreationDateTime(DateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public ExchangeRateEntity id(Integer id) {
        this.id = id;
        return this;
    }

    public ExchangeRateEntity startDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public ExchangeRateEntity sourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public ExchangeRateEntity targetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public ExchangeRateEntity rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public ExchangeRateEntity creationDateTime(DateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }
}
