/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.api.world;

/**
 * A Location is the state of the world at a given coordinate on the globe.
 * <ol>
 * <b>Location Requirements</b>
 * <li>
 * A Location is defined as a representation of a point in a Cartesian
 * coordinate system.
 * </li>
 * <li>
 * A Location must be able to identify itself via coordinates.
 * </li>
 * <li>
 * A Location must also be able to inform if it is currently populated or not.
 * </li>
 * <li>
 * It is the responsibility of the application instantiating
 * a LocationImp to manage uniqueness and scope of coordinate k eys.
 * </li>
 * </ol>
 */
public interface Location {

    /**
     * The abscissa coordinate for a given Location instance.
     *
     * @return The abscissa key for the LocationImp coordinate.
     *         Only non-negative integers are valid.
     */
    long getX();

    /**
     * The ordinate coordinate for a given Location instance.
     *
     * @return The ordinate key for the LocationImp coordinate.
     *         Only non-negative integers are valid.
     */
    long getY();

    /**
     * The means to determine if a given location instance is populated.
     *
     * @return Returns true if the location is populated,
     *         else will return false;
     */
    boolean isPopulated();
}
