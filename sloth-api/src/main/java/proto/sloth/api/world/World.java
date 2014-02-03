/**
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.api.world;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.UUID;

/**
 * The World is a grid of locations. Each location can hold the state of
 * 0 or 1(True or False). The grid can be conceptualized as running
 * along an axis (x, y).
 * <p/>
 * The World iterations will observer John Conway's Game of Life rules.
 * <ol>
 * <b>For a space that is 'populated':</b>
 * <li>Each cell with one or no neighbors dies, as if by loneliness.</li>
 * <li>Each cell with four or more neighbors dies, as if by overpopulation.</li>
 * <li>Each cell with two or three neighbors survives.</li>
 * </ol>
 * <ol>
 * <b>For a space that is 'empty' or 'unpopulated'</b>
 * <li>Each cell with three neighbors becomes populated.</li>
 * </ol>
 */
@XmlRootElement
public interface World {

    /**
     * DEVELOPER NOTE:
     * <p/>
     * The UUID must be guaranteed to be unique for the domain space
     * of the World implementer.
     *
     * @return A unique identifier to a given world instance.
     */
    @XmlElement
    UUID getId();

    /**
     * This method will return count of the current iteration. Zero is
     * te initial start date of the world instance.
     *
     * @return This value may never be negative.
     */
    @XmlAttribute
    long getDate();

    /**
     * This method will spin the World one day. Each spin invokes an iteration
     * of the game rules on all {@link Location} of the globe.
     * <p/>
     * DEVELOPER NOTE: As of the writing of this specification, thread-safety
     * of the spin method is left to the World implementer.
     *
     * @param count The number of spins of thw world to perform.
     */
    void spin(int count);

    /**
     * This method will return the upperBound for a given World. This upper
     * bound is the number of location along a coordinate axis.
     * <p/>
     * Keep in mind that location lookup begins at 0, so that the largest
     * coordinate value for a given world location would be
     * (upperBound -1, upperBound -1).
     *
     * @return The returned value may never be 0 or a negative number.
     */
    long getUpperBound();

    /**
     * This method will return a list of the locations for the
     * current iteration. The returned list must be in order of
     * (abscissa, ordinate) ascending. In addition the returned list will only
     * contain the state of the locations at the time of method invocation.
     * <p/>
     * DEVELOPER NOTE: At the time of the writing of this
     * specification, the thread safety of this method during
     * the invocation of the {@link #spin(int)} method is left
     * to the implementer of this API.
     *
     * @return List of locations, that may naver be {@code null}.
     */
    List<Location> locations();

    /**
     * This method will return the Location at a given x, y coordinate.
     *
     * @param x The abscissa of the target location coordinate.
     * @param y The ordinate of the target location coordinate.
     * @return This method will return {@code null} if there is no
     *         location at the given coordinate.
     */
    Location getLocation(final long x, final long y);
}
