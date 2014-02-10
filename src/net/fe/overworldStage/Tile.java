package net.fe.overworldStage;

import net.fe.FEResources;
import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class Tile extends GriddedEntity implements DoNotDestroy{
	private Terrain terrain;
	private int id;
	private Tileset tileset;
	/**
	 * P - Plain
	 * A - pAth
	 * M - Mountain
	 * ^ - Forest
	 * = - Wall
	 * S - Sea
	 * L - fLoor
	 * T - forT
	 * K - peaK
	 * I - pIllar
	 * D - Desert
	 * - - fence
	 * N - None
	 * V - Village
	 * O - thrOne (or castle entrance)
	 * C - Cliff
	 * ~ - Hill
	 * H - House
	 */
	private static String terrainMap =
			"PPPPAAAAAAA----SSSS      " +
			"PPPPAAAAAAA----SSSS      " +
			"PPPKKPAAA^^====SSSS      " +
			"MKKKAAAANNN====SSSS      " +
			"----AAAANVN==LSSS        " +
			"NNNNNNNN^N^SLLSSS        " +
			"NVNNNNVN^V^SLLSSS        " +
			"KPKKKPPPAAAANNNNNNNNN    " +
			"KPPPKPPPAAAANNNNNNNNN    " +
			"KKKKKPPPAAAANVNNONNON    " +
			"KKKKKAAAPPAAAAAHHSSSS    " +
			"KKKKKAAAAAAAAPTHTSSSS    " +
			"KKKKKAAAAAAAANSSSSS      " +
			"KKKKKKKMMMMCCCSSSNNN     " +
			"KKKKKPK^^^^CPCSSSNVN     " +
			"KKKKKPKKKKKKKCCC PA      " +
			"=======LL=P=LLSSS====    " +
			"=======LL=P=LLSS======   " +
			"LLL====LLLPOLLSS=L=IOI   " +
			"LLLLLILLIILLLLSSSLLLLL   " +
			"LLLLLILLILLLLLSSSLLL     " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         " +
			"                         ";

	public Tile(int x, int y, int id) {
		super(x,y);
		renderDepth = ClientOverworldStage.TILE_DEPTH;
		this.id = id;
		tileset = new Tileset(FEResources.getTexture("terrain_tiles"), 16, 16);
		setTerrain(getTerrainFromID(id));
	}
	
	public void render(){
		Renderer.addClip(0, 0, 368, 240, false);
		ClientOverworldStage c = (ClientOverworldStage)stage;
		tileset.render(x - c.camX, y - c.camY, id%25, id/25, renderDepth);
	}

	public Terrain getTerrain() {
		return terrain;
	}
	
	public static Terrain getTerrainFromID(int id) {
		char ch = terrainMap.charAt(id);
		Terrain t;
		if(ch == 'P') t = Terrain.PLAIN;
		else if(ch == '=') t = Terrain.WALL;
		else if(ch == 'A') t = Terrain.PATH;
		else if(ch == 'S') t = Terrain.SEA;
		else if(ch == '^') t = Terrain.FOREST;
		else if(ch == 'L') t = Terrain.FLOOR;
		else if(ch == 'K') t = Terrain.PEAK;
		else if(ch == 'M') t = Terrain.MOUNTAIN;
		else if(ch == 'T') t = Terrain.FORT;
		else if(ch == 'V') t = Terrain.VILLAGE;
		else if(ch == 'N') t = Terrain.NONE;
		else if(ch == '-') t = Terrain.FENCE;
		else if(ch == 'D') t = Terrain.DESERT;
		else if(ch == 'I') t = Terrain.PILLAR;
		else if(ch == 'O') t = Terrain.THRONE;
		else if(ch == 'H') t = Terrain.HOUSE;
		else if(ch == '~') t = Terrain.HILL;
		
		else t = Terrain.NONE;
		return t;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
}
