/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.api.world;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A WorldFacotory is used to create worlds.
 */
public interface WorldFactory {

    /**
     * Create a world with a given upper bound.
     *
     * @param upperBound The upperBound of the world edge.
     * @return The return value may never be {@code null}.
     */
    World createWorld(final long upperBound);

    /**
     * This method will take a BufferedReader and use the data feed to create
     * the target world.
     * <p/>
     * DEVELOPER NOTE: The syntax and semantics of the character stream utilized
     * to generate the world is left to the WorldFactory implementation.
     *
     * @param bufferedReader A character stream input that may never be null.
     * @return The return value may never be {@code null}.
     * @throws IOException This may be thrown due to the
     *                     use of the BufferedReader.
     */
    World loadWorld(BufferedReader bufferedReader)
            throws IOException;
}
