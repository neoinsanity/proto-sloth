/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import proto.sloth.api.world.Location;
import proto.sloth.api.world.World;
import proto.sloth.api.world.WorldFactory;

import javax.ws.rs.*;

/**
 * This class provides the "text/plain" media typs of the World resource.
 */
@Path("world")
@Produces("text/plain")
public class WorldPlainRestPoint
  extends AbstractRestPoint {

    private static final Log log = LogFactory.getLog(WorldPlainRestPoint.class);

    private WorldFactory worldFactory;

    @Required
    public void setWorldFactory(final WorldFactory worldFactory) {

        this.worldFactory = worldFactory;
    }

    /**
     * This method is the 'world' REST resource point.
     *
     * @return This REST point returns the current state of the world.
     */
    @GET
    public String world() {

        if (log.isDebugEnabled()) log.debug("world()");

        World world = getSessionWorldContainer().getWorld();

        //todo: raul = bit of a hack, as this source return a resource not found exception.
        if (world == null) {
            world = worldFactory.createWorld(5);
            getSessionWorldContainer().setWorld(world);
        }

        return getSessionWorldContainer().getWorld().toString();
    }

    @GET
    @Path("location")
    public String location(
      @QueryParam("x") final Long x,
      @QueryParam("y") final Long y
    ) {

        if (log.isDebugEnabled()) log.debug("location(" + x + ", " + y + ")");

        if (x == null)
            throw new IllegalArgumentException("The 'x' query parameter is missing, with a valid integer value.");
        if (y == null)
            throw new IllegalArgumentException("The 'y' query parameter is missing, with a valid integer value.");

        final Location location = getSessionWorldContainer().getWorld().getLocation(x, y);

        if (location == null) {
            // todo: raul - this should be handled as part of a general error handling framework to ensure proper http error code return.
            throw new RuntimeException("The location with given coordinates does not exist.");
        }

        return location.toString();
    }

    @GET
    @Path("iteration")
    public String iteration() {

        if (log.isDebugEnabled()) log.debug("iteration()");

        return Long.toString(getSessionWorldContainer().getWorld().getDate());
    }

    @POST
    @Path("spin")
    public String spin(
      @QueryParam("count") @DefaultValue("1") final int count
    ) {

        if (log.isDebugEnabled()) log.debug("spin(" + count + ")");

        final World world = getSessionWorldContainer().getWorld();

        world.spin(count);

        if (log.isDebugEnabled()) log.debug("iterated to " + world.getDate());

        return world.toString();
    }
}
