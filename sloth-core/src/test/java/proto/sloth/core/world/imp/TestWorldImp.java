package proto.sloth.core.world.imp; /**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import proto.sloth.api.world.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

/**
 *
 */
@Test(groups = {"dev"})
@ContextConfiguration(locations = {"/world-imp-context.xml"})
public class TestWorldImp
        extends AbstractTestNGSpringContextTests {

    private static final Log log = LogFactory.getLog(TestWorldImp.class);

    private WorldImp world;

    @Autowired
    SimpleCharWorldFactoryImp simpleWorldFactory;

    @Autowired
    String baseArchivePath;

    public void init()
            throws Exception {
        log.info("-- init(): " + new Date());

        BufferedReader bufferedReader =
                new BufferedReader(new FileReader(baseArchivePath + "world-init.txt"));
        world = (WorldImp) simpleWorldFactory.loadWorld(bufferedReader);

        validateWorld("world-post-init-check.txt", world);

        if (log.isDebugEnabled()) log.debug(logWorld("Initial state:", world));
    }

    @Test(dependsOnMethods = {"init"})
    public void spin() {
        log.info("-- spin():" + new Date());

        for (int i = 1; i <= 50; i++) {

            // - Test
            world.spin(1);

            if (log.isDebugEnabled()) {
                log.debug(logWorld("-- Iteration[" + i + "]:", world));
            }
        }

        //todo: raul - complete this unit test - aaaagh, I need to hand validate a bunch of state point, and archive the results to validate against.
    }

    private void validateWorld(final String expectedStateFile, final WorldImp world)
            throws IOException {

        final BufferedReader expectedReader =
                new BufferedReader(new FileReader(baseArchivePath + expectedStateFile));
        final BufferedReader currentReader =
                new BufferedReader(new StringReader(logWorld(/*no header*/null, world)));

        final long upperBound = world.upperBound;

        for (long x = 0; x < upperBound; x++) {

            final String expectedLine = expectedReader.readLine();
            final String currentLine = currentReader.readLine();

            for (long y = 0; y < upperBound; y++) {

                final char expectedChar = expectedLine.charAt((int) y);
                final char currentChar = currentLine.charAt((int) y);

                assert expectedChar == currentChar
                        : "Expected '" + expectedChar + "' at location (" + x + ", " + y + "), not: " + currentChar;

                final Location location = world.getLocation(x, y);
                if (location.isPopulated()) {
                    assert expectedChar == '1' : "The location population state for (" + x + ", " + y +
                            ") should be true, does not match the expected unpopulated state of " + expectedChar;
                } else {
                    assert expectedChar == '0' : "The location population state for (" + x + ", " + y +
                            ") should be false, does not match the expected populated state of " + expectedChar;
                }

                assert location.getX() == x : "Expected location x-axis must be " + x + ", not " + location.getX();
                assert location.getY() == y : "Expected location y-axis must be " + y + ", not " + location.getY();
            }
        }
    }

    /**
     * This is a helper method that is used to extract a very simple representation of a world
     * in string format. Locations are represented as
     *
     * @param msg   A message header to be attached to the char map representation of the world.
     *              If this value is null, then no message will be prepended to the char representation.
     * @param world The target world to generate the char map representation. The value may not be {@code null}.
     * @return A {@code String} containing the char map representation for the target world.
     */
    private String logWorld(final String msg, final WorldImp world) {

        final long upperBound = world.getUpperBound();

        final StringBuilder map = new StringBuilder();

        // set log message is it is not null, else don't include
        if (msg != null) {
            map.append(msg).append('\n');
        }

        for (long x = 0; x < upperBound; x++) {
            for (long y = 0; y < upperBound; y++) {
                final Location location = world.getLocation(x, y);
                map.append(location.isPopulated() ? 1 : 0);
            }
            map.append('\n');
        }

        return map.toString();
    }
}
