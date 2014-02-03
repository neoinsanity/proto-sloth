/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.core.world.imp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import proto.sloth.api.world.Location;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Test(groups = {"dev"}, suiteName = "Sloth Core")
@ContextConfiguration(locations = {"/globe-context.xml"})
public class TestGlobe
        extends AbstractTestNGSpringContextTests {

    private static final Log log = LogFactory.getLog(TestGlobe.class);

    private static final int UPPER_BOUND = 5;

    /**
     * This is the test subject. The target globe is created
     * by the spring context loader.
     */
    @Autowired
    Globe globe;

    /**
     * The basic notion behind this test is to set life state in each location where
     * ((x * upper_bound) + y) mod N is equal to 0(zero), all other location are set
     * to no life. The value of N will be in the sequence of 3,4, and 5. This will
     * ensure that location setting is functional.
     */
    public void getAndSetLocations() {

        log.info("-- getAndSetLocations(): " + new Date());

        // This population handler will be used to populate the globes locations for read/write
        // testing of locations in the globe.
        final PopulateByModVisitor populateByModVisitor = new PopulateByModVisitor();

        /*
        Each of the following code blocks test the setting of locations
        using the
        a Visitor class to utilize the target seed.
         */
        {
            visitLocations(populateByModVisitor, 5);

            final String expectedMap = "1000010000100001000010000";
            validateMapDump(expectedMap);
        }

        {
            visitLocations(populateByModVisitor, 4);

            final String expectedMap = "1000100010001000100010001";
            validateMapDump(expectedMap);
        }

        {
            visitLocations(populateByModVisitor, 3);

            final String expectedMap = "1001001001001001001001001";
            validateMapDump(expectedMap);
        }

        {
            visitLocations(populateByModVisitor, 6);

            final String expectedMap = "1000001000001000001000001";
            validateMapDump(expectedMap);
        }
    }

    /**
     * This test will set the location population settings, and then it will retrieve the list of locations.
     * The locations must be returned in order of x, y.
     */
    @Test(dependsOnMethods = {"getAndSetLocations"})
    public void getLocationList() {

        log.info("-- getLocationList: " + new Date());

        // Ensure that globe is populated to a known state.
        visitLocations(new PopulateByModVisitor(), 3);

        // This is the method being tested
        final List<LocationImp> locations = globe.getLocationList();

        assert locations != null
                : "Locations list should not be null.";

        assert locations.size() == 25
                : "The size of the location list should be 25, not " + locations.size();

        int x = 0;
        int y = 0;

        for (final Location location : locations) {

            if (log.isDebugEnabled()) log.debug(location);

            // ensure that the location coordinates are correct
            assert location.getX() == x : "The x coordinate should be " + x + " not: " + location.getX();
            assert location.getY() == y : "The y coordinate should be " + y + " not: " + location.getY();

            // ensure that the location population is correct.
            // Note that modulo 3 was used to determine the population setting.
            if ((((location.getX() * UPPER_BOUND) + location.getY()) % 3) == 0) {
                assert location.isPopulated() : "The location should be true, not false";
            } else {
                assert /*not*/!location.isPopulated() : "The location should be false, not true";
            }

            // DEVELOPER NOTE: This is the important bit. This is where the y-axis loops as the
            // 1's place in a 5 base number system, and x-axis is in the 10's place.
            // Increment the y location as we iterate over the locations
            y++;
            if (y == 5) { // it's now time to move to the next x-coordinate
                y = 0;
                x++;
            }
        }
    }

    /**
     * This test ensures that for a given location on the globe, a valid list of that locations neighbors can be
     * retrieved. In addition it is expected that the returned neighbor list is returned in from
     * the upper-left most neighbor to the lower-right most neighbor. The order of the list will run from left-to-right,
     * top-to-bottom. Note: the target location will not be included in the list.
     *
     * @param x                    The x coordinate of the target location for testing.
     * @param y                    The y coordinate of the target location for testing.
     * @param expectedNeighborList The expected list of neighbors, in the order they should be returned.
     */
    @Test(
            dependsOnMethods = {"getLocationList"},
            dataProvider = "neighborsTestData")
    public void getNeighbors(final long x, final long y, final long[][] expectedNeighborList) {

        log.info("-- getNeighbors(" + x + ", " + y + "): " + new Date());

        // Ensure that globe is populated to a known state.
        visitLocations(new PopulateByModVisitor(), 3);

        if (log.isDebugEnabled()) {
            log.debug("Current Globe: " + globe);
        }

        // Get the test subject
        final List<LocationImp> neighbors = globe.getNeighbors(x, y);

        assert neighbors != null :
                "The return neighbor list cannot be null for coordinate(0,0)";
        assert neighbors.size() == 8 :
                "The return neighbor list should be of size 8, not: " + neighbors.size();

        for (int coordinateIndex = 0; coordinateIndex < 8; coordinateIndex++) {

            final Location location = neighbors.get(coordinateIndex);
            long[] expectedCoordinates = expectedNeighborList[coordinateIndex];
            assert location.getX() == expectedCoordinates[0] // 0 is the x coordinate
                    : "Expected coordinates " + Arrays.toString(expectedCoordinates) + " not: " + location;
            assert location.getY() == expectedCoordinates[1] // 1 is the y coordinate
                    : "Expected coordinates " + Arrays.toString(expectedCoordinates) + " not: " + location;
        }
    }

    /**
     * @param x                       The x coordinate of the target location for testing.
     * @param y                       The y coordinate of the target location for testing.
     * @param expectedPopulationCount The expected list of neighbors, in the order they should be returned.
     */
    @Test(
            dependsOnMethods = {"getNeighbors"},
            dataProvider = "neighborsCountData")
    public void getNeighborPopulationCount(final long x, final long y, final int expectedPopulationCount) {

        log.info("-- getNeighborPopulationCount(" + x + ", " + y + ", " + expectedPopulationCount + "): " + new Date());

        if (log.isDebugEnabled()) {
            log.debug("Current Globe: " + globe);
        }

        // Ensure that globe is populated to a known state.
        visitLocations(new PopulateByModVisitor(), 3);

        // Get the test subject
        final int populationCount = globe.getNeighborPopulationCount(x, y);
        assert populationCount == expectedPopulationCount :
                "The expected population count is " + expectedPopulationCount + ", not " + populationCount;
    }

    // **********************************************************************************
    // **********************************************************************************
    // ************
    // ************ INTERNAL UTILITY METHODS
    // ************
    // **********************************************************************************
    // **********************************************************************************

    /**
     * This method makes a simple string representation of the globe map. The map is returned
     * in x, y order.
     *
     * @return A String representation of the globe.
     */
    private String mapDump() {

        final StringBuilder dump = new StringBuilder();

        visitLocations(new LocationVisitor() {
            public void executeOnLocation(final LocationImp location, final Object... objects) {
                StringBuilder logMap = (StringBuilder) objects[0];

                logMap.append(location.isPopulated() ? '1' : "0");
            }
        }, dump);

        return dump.toString();
    }

    /**
     * This utility method will take a {@link #mapDump()} and assert that dump
     * against an expected map.
     *
     * @param expectedLogMap The expected value of a current map dump.
     */
    private void validateMapDump(final String expectedLogMap) {

        final String logMap = mapDump();
        if (log.isDebugEnabled()) log.debug(logMap);
        assert logMap != null : "The generated globe log map cannot be null.";
        assert logMap.equals(expectedLogMap) :
                "The expected value should be:\n" + expectedLogMap + "\nnot:\n" + logMap;
    }

    /**
     * This is a general purpose method used to execute a visitor pattern wherein a
     * a target implementer of the {@link @visitLocations}
     *
     * @param locationVisitor The visitor to execute against each location.
     * @param objects         A variable length argument of objects that will be passed to the visitor.
     */
    private void visitLocations(final LocationVisitor locationVisitor, final Object... objects) {

        for (long x = 0; x < UPPER_BOUND; x++) {
            for (long y = 0; y < UPPER_BOUND; y++) {

                final LocationImp location = globe.getLocation(x, y);
                locationVisitor.executeOnLocation(location, objects);
            }
        }
    }

    /**
     * This is the interface definition that is expected by the
     * {@link TestGlobe#visitLocations(proto.sloth.core.world.imp.TestGlobe.LocationVisitor, Object...)}
     * method to allow us to write
     */
    private interface LocationVisitor {

        void executeOnLocation(final LocationImp location, final Object... objects);
    }

    /**
     * This implementation of the {@link proto.sloth.core.world.imp.TestGlobe.LocationVisitor} is used to
     * seed the globe with population.
     */
    private class PopulateByModVisitor
            implements LocationVisitor {

        @Override
        public void executeOnLocation(LocationImp location, Object... objects) {

            Integer modulo = (Integer) objects[0];

            if ((((location.getX() * UPPER_BOUND) + location.getY()) % modulo) == 0) {
                location.setPopulated(true);
            } else {
                location.setPopulated(false);
            }
        }
    }

    // **********************************************************************************
    // **********************************************************************************
    // ************
    // ************ DATA PROVIDERS
    // ************
    // **********************************************************************************
    // **********************************************************************************

    /**
     * This method is used by TestNG in the invocation of the
     * {@link #getNeighbors(long, long, long[][])} test.
     *
     * @return A list of locations, and the expected value for neighbors of that location.
     */
    @DataProvider
    Object[][] neighborsTestData() {
        return new Object[][]{
                /*
                 {x, y, expectedNeighborList}
                 */
                {0, 0, new long[][]{
                        {4, 4},
                        {4, 0},
                        {4, 1},
                        {0, 4},
                        {0, 1},
                        {1, 4},
                        {1, 0},
                        {1, 1}}
                },
                {0, 1, new long[][]{
                        {4, 0},
                        {4, 1},
                        {4, 2},
                        {0, 0},
                        {0, 2},
                        {1, 0},
                        {1, 1},
                        {1, 2},
                }},
                {0, 2, new long[][]{
                        {4, 1},
                        {4, 2},
                        {4, 3},
                        {0, 1},
                        {0, 3},
                        {1, 1},
                        {1, 2},
                        {1, 3},
                }},
                {0, 3, new long[][]{
                        {4, 2},
                        {4, 3},
                        {4, 4},
                        {0, 2},
                        {0, 4},
                        {1, 2},
                        {1, 3},
                        {1, 4},
                }},
                {1, 0, new long[][]{
                        {0, 4},
                        {0, 0},
                        {0, 1},
                        {1, 4},
                        {1, 1},
                        {2, 4},
                        {2, 0},
                        {2, 1},
                }},
                {2, 1, new long[][]{
                        {1, 0},
                        {1, 1},
                        {1, 2},
                        {2, 0},
                        {2, 2},
                        {3, 0},
                        {3, 1},
                        {3, 2},
                }},
                {3, 2, new long[][]{
                        {2, 1},
                        {2, 2},
                        {2, 3},
                        {3, 1},
                        {3, 3},
                        {4, 1},
                        {4, 2},
                        {4, 3},
                }},
                {4, 3, new long[][]{
                        {3, 2},
                        {3, 3},
                        {3, 4},
                        {4, 2},
                        {4, 4},
                        {0, 2},
                        {0, 3},
                        {0, 4},
                }},
                {4, 4, new long[][]{
                        {3, 3},
                        {3, 4},
                        {3, 0},
                        {4, 3},
                        {4, 0},
                        {0, 3},
                        {0, 4},
                        {0, 0},
                }}
        };
    }

    @DataProvider
    Object[][] neighborsCountData() {

        return new Object[][]{
                /*
                {x, y, expectedPopulationCount}
                 */
                {0, 0, 4},
                {0, 1, 3},
                {0, 2, 3},
                {0, 3, 2},
                {1, 0, 3},
                {2, 1, 3},
                {3, 2, 3},
                {4, 3, 3},
                {4, 4, 4}
        };
    }
}
