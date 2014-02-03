/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.core.world.imp;

import proto.sloth.api.world.World;

import java.util.UUID;

/**
 *  This class is an abstract helper class to implement the {@code World}} interface.
 */
public abstract class AbstractWorld
        implements World {

    /**
     * This property is used to store the unique identifier that will
     * be created with this world.
     */
    protected UUID id;

    /**
     * This is the upperBound for the coordinate axis.
     * The maximum allowable location coordinate is (upperBound -1, upperBound -1).
     */
    protected long upperBound;

    /**
     * This property is used to mark the iteration currently being
     * created, or having just been completed if there is no iteration
     * currently being generated.
     */
    protected long iteration = 0;

    protected Globe currentGlobe = new Globe(DEFAULT_GLOBE_UPPER_BOUND);
    protected Globe futureGlobe = new Globe(DEFAULT_GLOBE_UPPER_BOUND);

    /**
     * This setting will create the representation of a grid of {@link proto.sloth.core.world.imp.LocationImp}
     * instances. The locations can be accessed by the use of the {@link WorldImp#getLocation(long, long)} method,
     * where the coordinate boundaries are min(0,0) and max(upperBound-1,upperBound-1).
     * <p/>
     * DEVELOPER NOTE:
     * This method will wipe out the current and future world state for any
     * {@link proto.sloth.core.world.imp.LocationImp} instances.
     * This method is not thread-safe.
     *
     * @param upperBound The upperBound of the world is the number of locations that
     *                   will be created along the x-axis and y-axis.
     * @return Returns {@code this} object as a helper method to string sets together.
     *         i.e. {@code world = new WorldImp().setUpperBound(n);}
     */
    public AbstractWorld setUpperBound(final long upperBound) {

        this.upperBound = upperBound;

        currentGlobe = new Globe(upperBound);
        futureGlobe = new Globe(upperBound);

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public long getUpperBound() {

        return upperBound;
    }

    /**
     * The method is overridden to provide visibility to the class during development
     * and debugging. It's utility is to get a representation of the WorldImp data,
     * prior to any transformation due to persistence or transmission of WorldImp
     * state.
     *
     * @return A JSON-like string representation of a WorldImp instance.
     */
    @Override
    public String toString() {
        return "AbstractWorld{" +
                "id=" + id +
                ", iteration=" + iteration +
                ", currentGlobe=" + currentGlobe +
                ", futureGlobe=" + futureGlobe +
                '}';
    }

    private static final long DEFAULT_GLOBE_UPPER_BOUND = 5;
}
