package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.deployment;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public abstract class TestOnGoodWorkingEcbProxy {

    @Deployment(name = "good-working-ecb-proxy", order = 1)
    public static Archive<?> createFluxActivityWSPluginDeployment() {
        WebArchive archive = DeploymentFactory.createStandardDeployment();
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.schedule");
        archive.addAsResource("test-data/GetExchangeRateResponse.txt", "test-data/GetExchangeRateResponse.txt");
        archive.addAsResource("datasets/exchange-rate-empty-dataset.xml","datasets/exchange-rate-empty-dataset.xml");
        return archive;
    }

}
