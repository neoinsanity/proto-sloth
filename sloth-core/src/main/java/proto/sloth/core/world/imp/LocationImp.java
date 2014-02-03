/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.core.world.imp;

import proto.sloth.api.world.Location;

/**
 * This is an implementation of the the {@code LocationImp} class.
 */
public class LocationImp
        implements Location {

    private final long x;

    private final long y;

    /**
     * A LocationImp is NOT populated upon instantiation.
     */
    private boolean isPopulated = false;

    /**
     * @param x See {@link #getX()} for description and requirements.
     * @param y See {@link #getY()} for description and requirements.
     */
    public LocationImp(final long x, final long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@link #isPopulated}
     *
     * @param isPopulated Set to true if the LocationImp instance is populated,
     *                    else set to false.
     */
    public void setPopulated(final boolean isPopulated) {
        this.isPopulated = isPopulated;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public boolean isPopulated() {
        return isPopulated;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public long getX() {
        return x;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}.
     */
    @Override
    public long getY() {
        return y;
    }

    /**
     * This override ensures that LocationImp instances are considered equal
     * based on the x and y coordinate key value.
     *
     * @param o The other LocationImp to test against for equality.
     * @return True if this(x,y)=o(x,y), else returns false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationImp)) return false;

        LocationImp that = (LocationImp) o;

        return x == that.x && y == that.y;
    }

    /**
     * A hash code based on the (x,y) coordinate of the given LocationImp instance.
     *
     * @return {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = (int) (x ^ (x >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        return result;
    }

    /**
     * The method is overridden to provide visibility to the class during
     * development and debugging. It's utility is to get a representation of
     * the LocationImp data, prior to any transformation due to persistence or
     * transmission of LocationImp state.
     *
     * @return A JSON-like string representation of a LocationImp instance.
     */
    @Override
    public String toString() {
        return "LocationImp{" +
                "x=" + x +
                ", y=" + y +
                ", isPopulated=" + isPopulated +
                '}';
    }
}
