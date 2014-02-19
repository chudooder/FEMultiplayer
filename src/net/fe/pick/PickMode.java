package net.fe.pick;

import net.fe.Session;

/**
 * Interface implementing a unit pick mode.
 * Sets up the client and server stages.
 * @author Shawn
 *
 */
public interface PickMode {
	public void setUpClient(Session session);
	public void setUpServer(Session session);
}
