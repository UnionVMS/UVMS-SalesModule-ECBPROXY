package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.xmlunit;

import org.apache.commons.lang3.StringUtils;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import static org.junit.Assert.assertFalse;

public class XmlUtil {

    public static void equalsXML(String expectedXmlAsString, String actualXmlAsString) {
        equalsAndFilteredXML(expectedXmlAsString, actualXmlAsString, StringUtils.EMPTY);
    }

    public static void equalsAndFilteredXML(String expectedXmlAsString, String actualXmlAsString, String... nodeNamesToFilter) {
        DiffBuilder diffBuilder = DiffBuilder.compare(Input.fromString(expectedXmlAsString))
                .withTest(Input.fromString(actualXmlAsString))
                .checkForSimilar()
                .ignoreWhitespace();
        for (String nodeNameToFilter : nodeNamesToFilter) {
            diffBuilder.withNodeFilter(node -> !node.getNodeName().equals(nodeNameToFilter));
        }
        Diff differenceResult = diffBuilder.build();
        assertFalse("\nExpected: " + expectedXmlAsString + "\nActual: " + actualXmlAsString + " XML similar: " + differenceResult.toString(), differenceResult.hasDifferences());
    }

}
