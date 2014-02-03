/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import proto.sloth.api.world.World;
import proto.sloth.api.world.WorldFactory;

import javax.ws.rs.*;

/**
 *
 */
@Path("creator")
@Produces("text/plain")
public class WorldFactoryPlainRestPoint
  extends AbstractRestPoint {

    private static final Log log = LogFactory.getLog(WorldFactoryPlainRestPoint.class);

    private WorldFactory worldFactory;

    @Required
    public void setWorldFactory(WorldFactory worldFactory) {

        this.worldFactory = worldFactory;
    }

    @POST
    @Path("world")
    public String createWorld(
      @QueryParam("upperBound") @DefaultValue("5") final int upperBound
    ) {

        if (log.isDebugEnabled()) log.debug("createWorld(" + upperBound + ")");

        final World world = worldFactory.createWorld(upperBound);
        getSessionWorldContainer().setWorld(world);

        return world.toString();
    }
}
