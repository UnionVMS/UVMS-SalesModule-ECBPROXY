package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class KeyDto {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "value")
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public KeyDto id(String id) {
        this.id = id;
        return this;
    }

    public KeyDto value(String value) {
        this.value = value;
        return this;
    }
}
