package net.fe.editor;

import java.io.Serializable;
import java.util.Set;

/**
 * Contains a list of entities that are to be added
 * to an OverworldStage upon setup. These entities include:
 * - tiles (id only, terrain info is generated at runtime)
 * - spawn locations
 * - game-mode specific entities
 * Also contains metadata including:
 * - grid dimensions
 * - supported game modes
 * 
 * A level can support multiple game modes by including
 * all relevant entities. The game will only load the entities
 * necessary to run that particular game.
 * @author Shawn
 *
 */
public class Level implements Serializable {
	private static final long serialVersionUID = -3556853678338788517L;
	public int width;
	public int height;
	public int[][] tiles;
	public Set<SpawnPoint> spawns;
	
	public Level(int w, int h, int[][] t, Set<SpawnPoint> s) {
		width = w;
		height = h;
		tiles = t;
		spawns = s;
	}
	
}
