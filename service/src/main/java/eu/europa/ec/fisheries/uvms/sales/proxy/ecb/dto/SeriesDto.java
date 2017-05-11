package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SeriesDto {

    @XmlElement(name = "SeriesKey")
    private SeriesKeyDto seriesKey;

    @XmlElement(name = "Obs")
    private List<ObsDto> obs;

    public SeriesKeyDto getSeriesKey() {
        return seriesKey;
    }

    public void setSeriesKey(SeriesKeyDto seriesKey) {
        this.seriesKey = seriesKey;
    }

    public List<ObsDto> getObs() {
        return obs;
    }

    public void setObs(List<ObsDto> obs) {
        this.obs = obs;
    }

    public SeriesDto seriesKey(SeriesKeyDto seriesKey) {
        this.seriesKey = seriesKey;
        return this;
    }

    public SeriesDto obs(List<ObsDto> obs) {
        this.obs = obs;
        return this;
    }
}
