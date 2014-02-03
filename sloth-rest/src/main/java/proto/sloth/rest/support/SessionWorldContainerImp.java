/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.rest.support;

import proto.sloth.api.world.World;
import proto.sloth.rest.SessionWorldContainer;

/**
 *
 */
public class SessionWorldContainerImp
        implements SessionWorldContainer {

    private World world;

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public World getWorld() {
        return world;
    }

    /**
     * {@inheritDoc}
     *
     * @param world {@inheritDoc}
     */
    @Override
    public void setWorld(World world) {

        if(world == null){

        }

        this.world = world;
    }
}
