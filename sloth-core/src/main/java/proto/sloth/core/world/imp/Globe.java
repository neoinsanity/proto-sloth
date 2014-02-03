/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.core.world.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Globe is a concrete representation of a globe on a flat map with (x,y)
 * coordinates.
 */
public class Globe {

    private static final Log log = LogFactory.getLog(Globe.class);

    /**
     * The upper bound of the allowed coordinate along either the
     * x-axis or the y-axis. As location coordinates are 0 based indexes,
     * the maximum allowed coordinate is (upperBound-1, upperBound-1).
     */
    private final long upperBound;

    /**
     * This is the total number of locations in the entire globe.
     * The value must be equal to upperBound^2. It's stored as an
     * optimization.
     */
    private final double totalNumberOfLocations;

    /**
     * A Hashmap representation of the Globe, where the
     */
    private final ConcurrentHashMap<Long /*x coordinate*/,
            Map<Long /* y coordinate */, LocationImp>> locations;

    public Globe(final long upperBound) {

        // initialize the globe initial settings.
        this.upperBound = upperBound;

        // validate that the size of the globe does not exceed the capacity of
        // the underlying implementation.
        totalNumberOfLocations = upperBound ^ 2;
        if (totalNumberOfLocations > Integer.MAX_VALUE) {
            final String msg = "Total number of Locations is limited to to "
                    + Integer.MAX_VALUE
                    + " which is determined by the value of"
                    + " upperBound of Globe squared: "
                    + totalNumberOfLocations;

            log.error(msg);

            throw new IllegalArgumentException(msg);
        }

        locations = new ConcurrentHashMap<Long, Map<Long, LocationImp>>();

        for (long x = 0; x < this.upperBound; x++) {

            // add an ordinate map for each x coordinate
            final Map<Long, LocationImp> ordinateMap
                    = new ConcurrentHashMap<Long, LocationImp>();
            locations.put(x, ordinateMap);

            // place a location imp at each y coordinate
            for (long y = 0; y < this.upperBound; y++) {
                final LocationImp location = new LocationImp(x, y);
                ordinateMap.put(y, location);
            }
        }

        log.info("created globe of size(" + this.upperBound
                + ", " + this.upperBound + ")");
    }

    public LocationImp getLocation(final long x, final long y) {

        final Map<Long, LocationImp> ordinateMap = locations.get(x);

        return ordinateMap != null ? // is there an ordinateMap ?
                ordinateMap.get(y) : // return the location at the y ordinate
                null; // else, invalid x value, to return null result
    }

    /**
     * This creates a list by iterating through the values of the underlying
     * hashmap implementation. The results are ordered by x-axis, y-axis.
     *
     * @return A copy of the locations at the current state in order by x, y.
     */
    public List<LocationImp> getLocationList() {

        // allocate a list that is equal to the number of locations
        final List<LocationImp> locationList =
                new ArrayList<LocationImp>((int) totalNumberOfLocations);

        for (long x = 0; x < upperBound; x++) {
            final Map<Long, LocationImp> ordinateMap = locations.get(x);
            for (long y = 0; y < upperBound; y++) {
                locationList.add(ordinateMap.get(y));
            }
        }
        return locationList;
    }

    /**
     * This method will return the location of the 8 neighbor locations for
     * any given location coordinate.
     *
     * @param x The abscissa of the target location.
     * @param y The ordinate of the target location.
     * @return A list of the 8 neighboring locations to the given
     *         target location.
     */
    public List<LocationImp> getNeighbors(final long x, final long y) {

        final List<LocationImp> neighborList = new ArrayList<LocationImp>(8);

        for (long xDelta = -1; xDelta <= 1; xDelta++) {
            for (long yDelta = -1; yDelta <= 1; yDelta++) {

                long abscissa = determineNeighborCoordinate(x, xDelta);
                long ordinate = determineNeighborCoordinate(y, yDelta);

                // if the (x,y)=(abscissa, ordinate) then it is the current
                // location, which is not want to include in the returned list.
                if (/*NOT*/!(x == abscissa && y == ordinate)) {
                    if (log.isDebugEnabled())
                        log.debug("including neighbor location(" + abscissa
                                + ", " + ordinate + ")");
                    neighborList.add(getLocation(abscissa, ordinate));
                }
            }
        }
        return neighborList;
    }

    /**
     * This method will inspect each of the neighboring for a target location
     * and return the count for the neighbors that are currently populated.
     *
     * @param x The x coordinate of the target location.
     * @param y The y coordinate of the target location.
     * @return The count of each neighbor that was populated.
     */
    public int getNeighborPopulationCount(final long x, final long y) {

        int populationCount = 0;

        final List<LocationImp> locationNeighbors = getNeighbors(x, y);
        for (final LocationImp neighbors : locationNeighbors) {

            if (neighbors.isPopulated()) populationCount++;
        }

        return populationCount;
    }

    @Override
    public String toString() {
        return "Globe{" +
                "locations=" + getLocationList() +
                '}';
    }

    /**
     * This helper method will return the coordinate for a neighboring location
     * given the current target location and the delta to the neighboring
     * location desired.
     *
     * @param coordinate The coordinate for the target location.
     * @param delta      0 or +/-1 to determine the direction of the
     *                   desired location.
     * @return The canonical coordinate for the desired location.
     */
    private long determineNeighborCoordinate(
            final long coordinate,
            final long delta) {

        long neighborCoordinate = coordinate + delta;

        // test if it's necessary to wrap the neighbors
        // coordinate due to map edge crossing.
        if (neighborCoordinate == -1) neighborCoordinate = upperBound - 1;
        if (neighborCoordinate == upperBound) neighborCoordinate = 0;

        return neighborCoordinate;
    }
}
