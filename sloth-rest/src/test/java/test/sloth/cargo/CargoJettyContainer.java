/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package test.sloth.cargo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.EmbeddedLocalContainer;
import org.codehaus.cargo.container.configuration.LocalConfiguration;
import org.codehaus.cargo.container.deployable.Deployable;
import org.codehaus.cargo.container.jetty.Jetty9xEmbeddedLocalContainer;
import org.codehaus.cargo.container.jetty.Jetty9xEmbeddedStandaloneLocalConfiguration;
import org.codehaus.cargo.container.jetty.Jetty9xStandaloneLocalConfiguration;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 */
public class CargoJettyContainer {

    private static final Log log = LogFactory.getLog(CargoJettyContainer.class);

    private Deployable deployable;

    private LocalConfiguration configuration;

    private EmbeddedLocalContainer container;

    @Required
    public void setDeployable(Deployable deployable) {
        this.deployable = deployable;
    }

    @Required
    public void setConfiguration(LocalConfiguration configuration) {
        this.configuration = configuration;
    }

    @PostConstruct
    public void start()
            throws Exception {

        log.info("start()");


        configuration.addDeployable(deployable);
        configuration.setProperty("cargo.servlet.port", "8092");

        container = (EmbeddedLocalContainer) new DefaultContainerFactory().
                createContainer("jetty9x", ContainerType.EMBEDDED, configuration);

        try {
            container.start();
        } catch (Exception e) {
            container.setTimeout(1000/*ms*/);
            container.stop();
            throw e; // NEVER handle the exception. It is only trapped here to trigger the container shutdown.
        }
        log.info("start initialized.");
    }

    @PreDestroy
    public void stop() {

        log.info("stop()");

        try {
            //container.setTimeout(1000/*ms*/ * 3);
            container.stop();
            Thread.yield();
            Thread.sleep(500);
        } catch (Exception e) {
            log.warn("Exception while waiting for Cargo/Jetty to stop: " + e.getMessage(), e);
        }
    }
}
