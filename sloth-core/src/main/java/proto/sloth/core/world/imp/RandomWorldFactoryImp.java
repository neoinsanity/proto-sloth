/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.core.world.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import proto.sloth.api.world.Location;
import proto.sloth.api.world.World;
import proto.sloth.api.world.WorldFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

/**
 * This implementation of the WorldFactory randomly populates the world by visiting
 * each Location in turn and flipping a coin.
 */
public class RandomWorldFactoryImp
        implements WorldFactory {

    private static final Log log = LogFactory.getLog(WorldFactory.class);

    /**
     * {@inheritDoc}
     * <p/>
     * The {@code RandomWorldFactoryImp} implementation of {@code WorldFactory} interface, simply sets the
     * population state for each generated state.
     *
     * @param upperBound {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public World createWorld(final long upperBound) {

        log.info("createWorld invoked");

        final Random random = new Random(System.currentTimeMillis());

        final World world = new WorldImp().setUpperBound(upperBound);

        for (Location location : world.locations()) {

            ((LocationImp) location).setPopulated(/*flip a coin*/random.nextBoolean());
        }

        return world;
    }

    /**
     * NOTE: This method is NOT supported. It will throw an UnsupportedOperationException if utilized at runtime.
     * <p/>
     * {@inheritDoc}
     *
     * @param bufferedReader {@inheritDoc}.
     * @return THIS IMPLEMENTATION WILL NOT RETURN.
     */
    @Override
    public World loadWorld(BufferedReader bufferedReader)
            throws IOException {

        final String msg = "The loadWorld(BufferedReader):World method is not supported.";
        log.error(msg);
        throw new UnsupportedOperationException(msg);
    }
}
