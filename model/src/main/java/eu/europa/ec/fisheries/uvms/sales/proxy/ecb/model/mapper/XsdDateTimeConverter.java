package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.model.mapper;

import org.joda.time.DateTime;

import javax.xml.bind.DatatypeConverter;

public class XsdDateTimeConverter {

    private XsdDateTimeConverter() {}

    public static DateTime unmarshal(String dateTime) {
        return new DateTime(DatatypeConverter.parseDate(dateTime));
    }

    public static String marshalDate(DateTime dateTime) {
        return DatatypeConverter.printDate(dateTime.toGregorianCalendar());
    }

    public static String marshalDateTime(DateTime dateTime) {
        return DatatypeConverter.printDateTime(dateTime.toGregorianCalendar());
    }
}
