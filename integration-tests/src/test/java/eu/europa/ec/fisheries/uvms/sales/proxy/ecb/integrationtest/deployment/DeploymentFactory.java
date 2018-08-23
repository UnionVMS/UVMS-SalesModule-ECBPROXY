package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.deployment;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeploymentFactory {

    private DeploymentFactory() {

    }

    static final Logger LOG = LoggerFactory.getLogger(DeploymentFactory.class);

    public static WebArchive createStandardDeployment() {
        WebArchive archive = createArchiveWithTestFiles("test");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.commons.message");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service");

        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.constant");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception");
        archive.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper");

        return archive;
    }


    private static WebArchive createArchiveWithTestFiles(final String name) {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        printFiles(files);

        // Embedding war package which contains the test class is needed
        // So that Arquillian can invoke test class through its servlet test runner
        WebArchive testWar = ShrinkWrap.create(WebArchive.class, name + ".war");

        testWar.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        testWar.addAsResource("persistence-test.xml", "META-INF/persistence.xml");
        testWar.addAsResource("logback-test.xml", "logback.xml");
        testWar.addAsManifestResource("jboss-deployment-structure.xml","jboss-deployment-structure.xml");

        testWar.addAsLibraries(files);

        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.deployment");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.ghost");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.message.producer");
        testWar.addPackages(true, "org.awaitility");

        return testWar;
    }


    private static void printFiles(File[] files) {

        List<File> filesSorted = new ArrayList<>();
        for(File f : files){
            filesSorted.add(f);
        }

        Collections.sort(filesSorted, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        LOG.info("FROM POM - begin");
        for(File f : filesSorted){
            LOG.info("       --->>>   "   +   f.getName());
        }
        LOG.info("FROM POM - end");
    }
}
