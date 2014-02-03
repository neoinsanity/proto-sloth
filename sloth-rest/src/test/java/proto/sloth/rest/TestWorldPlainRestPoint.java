/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.rest;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.Date;

@Test(groups = {"dev"})
@ContextConfiguration({"classpath:cargo-jetty-test-context.xml"})
public class TestWorldPlainRestPoint
  extends AbstractTestNGSpringContextTests {

    private static final Log log = LogFactory.getLog(TestWorldPlainRestPoint.class);

    public void world()
      throws Exception {

        log.info("-- world(): " + new Date());

        final WebClient client = new WebClient();

        {// world get
            WebRequestSettings webRequestSettings =
              new WebRequestSettings(new URL("http://localhost:8092/webapp/sloth/world"));
            WebResponse webResponse = client.loadWebResponse(webRequestSettings);

            final int statusCode = webResponse.getStatusCode();
            assert statusCode == 200 : "Status code should be 200, not: " + statusCode;
        }
    }
}
