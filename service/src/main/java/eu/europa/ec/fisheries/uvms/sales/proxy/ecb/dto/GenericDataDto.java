package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GenericData",
                namespace = "http://www.sdmx.org/resources/sdmxml/schemas/v2_1/message")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericDataDto {

    @XmlElement(name = "DataSet")
    private DataSetDto dataSet;


    public DataSetDto getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSetDto dataSet) {
        this.dataSet = dataSet;
    }

    public GenericDataDto dataSet(DataSetDto dataSet) {
        this.dataSet = dataSet;
        return this;
    }
}
