/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.core.world.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import proto.sloth.api.world.World;
import proto.sloth.api.world.WorldFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This implementation of the WorldFactory will load from a simple line-separated file.
 * The first line of the file must contain the size of the World to create. Subsequent lines
 * of the file are used to represent the state of the locations. Only valid characters
 * are 0(unpopulated) and 1(populated). The second line of the stream is 0 along the x-axis,
 * with every line after that increments up. Reading the chars from left to right along
 * a given line represents the y-axis point.
 */
public class SimpleCharWorldFactoryImp
        implements WorldFactory {

    private static final Log log = LogFactory.getLog(SimpleCharWorldFactoryImp.class);

    /**
     * NOTE: This method is NOT supported. It will throw an UnsupportedOperationException.
     * <p/>
     * {@inheritDoc}
     *
     * @param upperBound {@inheritDoc}
     * @return THIS IMPLEMENTATION WILL NOT RETURN.
     */
    @Override
    public World createWorld(long upperBound) {

        final String msg = "The createWorld(long):World method is not supported.";
        log.error(msg);
        throw new UnsupportedOperationException(msg);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * DEVELOPER NOTE: This WorldFactory implementation is not intended for production use. It
     * lacks many of the validation or data integrity checks that would be necessary in a
     * production environment.
     *
     * @param bufferedReader {@inheritDoc}
     * @return {@inheritDoc}
     * @throws {@inheritDoc}
     */
    @Override
    public World loadWorld(BufferedReader bufferedReader)
            throws IOException {

        log.info("loadWorld invoked");

        // 1. Get the size from the first line in the buffer
        final String upperBoundLine = bufferedReader.readLine();
        final long upperBound = Long.parseLong(upperBoundLine);

        final World world = new WorldImp().setUpperBound(upperBound);

        // 2. Iterate over each subsequent line, up to the size, and set the population.
        for (int x = 0; x < upperBound; x++) {

            // get the next line
            final String line = bufferedReader.readLine();

            for (int y = 0; y < upperBound; y++) {
                final LocationImp location = (LocationImp) world.getLocation(x, y);
                location.setPopulated(/*NOT*/ !(line.charAt(y) == '0'));
            }
        }

        return world;
    }
}
