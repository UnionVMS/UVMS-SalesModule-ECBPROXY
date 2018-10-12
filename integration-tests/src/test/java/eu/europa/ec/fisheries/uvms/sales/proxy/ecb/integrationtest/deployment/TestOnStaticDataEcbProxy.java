package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.deployment;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public abstract class TestOnStaticDataEcbProxy {

    @Deployment(name = "static-data-ecb-proxy", order = 1)
    public static Archive<?> createFluxActivityWSPluginDeployment() {
        WebArchive archive = DeploymentFactory.createStandardDeployment();
        archive.addAsResource("test-data/GetExchangeRateResponse.txt", "test-data/GetExchangeRateResponse.txt");
        archive.addAsResource("datasets/exchange-rate-dataset.xml", "datasets/exchange-rate-dataset.xml");
        archive.addAsResource("datasets/exchange-rate-empty-dataset.xml","datasets/exchange-rate-empty-dataset.xml");
        return archive;
    }
}
