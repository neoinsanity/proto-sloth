/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.core.world.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import proto.sloth.api.world.Location;

import java.util.List;
import java.util.UUID;

/**
 * This is an implementation of the {@code World} interface. It delegates most
 * implementation details to the
 * {@link proto.sloth.core.world.imp.AbstractWorld} class.
 */
class WorldImp
        extends AbstractWorld {

    private static final Log log = LogFactory.getLog(WorldImp.class);

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public long getDate() {
        return iteration;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <h3>Implementation Detail</h3>
     * Execution of this method is not thread safe. It should only be invoked
     * once, and allowed to complete before invoking the method again.
     *
     * @param count {@inheritDoc}
     */
    @Override
    public void spin(final int count) {

        long currentCount = 0;
        while (currentCount++ < count) {
            iteration++;

            if (log.isDebugEnabled()) log.debug("iteration[" + iteration + "]");

            for (final LocationImp currentLocation : currentGlobe.getLocationList()) {

                executeGameRulesOnCurrentLocation(currentLocation);
            }

            // After the iteration, the results of the future globe are
            // now the current globe. The following code makes the future
            // globe the current globe. It also makes the previous current globe
            // the new future globe. It's easier to thing of it as switching to
            // hash map buffers to reduce memory thrashing.
            final Globe previousGlobe = currentGlobe; // the current is now past
            currentGlobe = futureGlobe; // the future is now
            futureGlobe = previousGlobe; // this is just to reuse the resource
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param x {@inheritDoc}
     * @param y {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Location getLocation(final long x, final long y) {

        return currentGlobe.getLocation(x, y);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Location> locations() {

        // the double casting is due to a Java idiosyncrasy.
        //noinspection RedundantCast
        return (List<Location>) (List) currentGlobe.getLocationList();
    }

    private void executeGameRulesOnCurrentLocation(
            final LocationImp currentLocation) {

        // The future location whose state will need to be managed
        final LocationImp futureLocation = futureGlobe.getLocation(
                currentLocation.getX(), currentLocation.getY());

        // The population count for the neighboring
        // locations to the current location
        final int neighborPopulationCount =
                currentGlobe.getNeighborPopulationCount(
                        currentLocation.getX(), currentLocation.getY());

        // For a space that is 'populated'
        if (currentLocation.isPopulated()) {
            switch (neighborPopulationCount) {
                // Each cell with one or no neighbors dies, as if by loneliness.
                case 0:
                case 1:
                    futureLocation.setPopulated(false);
                    break;
                // Each cell with two or three neighbors survives.
                case 2:
                case 3:
                    futureLocation.setPopulated(true);
                    break;
                // Each cell with four or more neighbors dies.
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    futureLocation.setPopulated(false);
                    break;
                default:
                    final String msg = "A neighbor count of "
                            + neighborPopulationCount + " is illegal.";
                    log.error(msg);
                    throw new IllegalStateException(msg);
            }
        }
        // For a space that is 'empty' or 'unpopulated'
        else {
            // Each cell with three neighbors becomes populated.
            if (neighborPopulationCount == 3) futureLocation.setPopulated(true);
            else futureLocation.setPopulated(false);
        }
    }
}
