package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DataSetDto {

    @XmlElement(name = "Series")
    private List<SeriesDto> series;

    public List<SeriesDto> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesDto> series) {
        this.series = series;
    }

    public DataSetDto series(List<SeriesDto> series) {
        this.series = series;
        return this;
    }
}
