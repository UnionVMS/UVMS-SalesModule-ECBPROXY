package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SeriesKeyDto {

    @XmlElement(name = "Value")
    private List<KeyDto> keys;

    public List<KeyDto> getKeys() {
        return keys;
    }

    public void setKeys(List<KeyDto> keys) {
        this.keys = keys;
    }


    public SeriesKeyDto keys(List<KeyDto> keys) {
        this.keys = keys;
        return this;
    }
}
