package chu.engine;

public interface Collidable {
	/**
	 * Method that deals with any collisions with the entities in
	 * the array.
	 * @param e
	 * @return
	 */

	public void doCollisionWith(Entity entity);
}
