/**
 *
 * NeoInsanity
 * Sanitarium Inc. CopyRight 2010
 */
package proto.sloth.rest;

import proto.sloth.api.world.World;

/**
 *
 */
public interface SessionWorldContainer {
    World getWorld();

    void setWorld(World world);
}
