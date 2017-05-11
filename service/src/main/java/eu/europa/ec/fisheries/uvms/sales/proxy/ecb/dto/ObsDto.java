package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ObsDto {

    @XmlElement(name = "ObsDimension")
    private ValueDto date;

    @XmlElement(name = "ObsValue")
    private ValueDto rate;

    public ValueDto getDate() {
        return date;
    }

    public void setDate(ValueDto date) {
        this.date = date;
    }

    public ValueDto getRate() {
        return rate;
    }

    public void setRate(ValueDto rate) {
        this.rate = rate;
    }


    public ObsDto date(ValueDto date) {
        this.date = date;
        return this;
    }

    public ObsDto rate(ValueDto rate) {
        this.rate = rate;
        return this;
    }
}
