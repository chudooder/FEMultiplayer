package net.fe.overworldStage;

import net.fe.FEResources;

import org.newdawn.slick.Color;

import chu.engine.GriddedEntity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class Tile extends GriddedEntity{
	private Terrain terrain;
	private int id;
	private static transient Tileset tileset;
	/**
	 * P - Plain
	 * A - pAth
	 * M - Mountain
	 * F - Forest
	 * W - Wall
	 * S - Sea
	 * L - fLoor
	 * T - forT
	 * K - peaK
	 * I - pIllar
	 * D - Desert
	 */
	private static String terrainMap =
			" PPPPPPAAAAAAMMFPMFPAAWWW" +
			"PPPPPAAFSSAMMMMMMMAAAAWWL" +
			"PPPPWPSSFPPAPPMWWWAAWWWWW" +
			"WPWWWTTFPWWWPPPWWWAAAAPWW" +
			"KKWWAWWWWLLLLWWWWWAAWPPWW" +
			"PKWAAAAAPWWWWWKWWWAAWPAWW" +
			"KKAWWWWWKKKKSSTWWWAAWWWWW" +
			"KKAWWWWWTWWW AAAAAPPAAWWW" +
			"KKTTPMMFFWWWAAAAPAPPAAFWA" +
			"KPTTPMFFMPPPA WSSPPPAAPPW" +
			"MPPSFSSSSFFPPFSSPAWWWWWWL" +
			"WWLLWWWWWWPASSSSSSWLLLWLL" +
			"IIWLWWWWWWWWWWTWPPLWWWPPW" +
			"LLWWPPPWWWWWWWTWPPPPWPWWK" +
			"AAWW SSSSSSPSPPPPPKKKKKPK" +
			"AAAWAAAWPWPSWWWWSAKKTFFFP" +
			"WWIWWAWWPWPSWWWAAAPPPPSSS" +
			"                  PTSSSSS" +
			"                  ASSSPSS" +
			"                  PPAAAAS" +
			" F WWWFF          TPPPPFP" +
			"WAAWWWAIATDDDDDDDDFPPPSSS" +
			" LLLFMKKKKPWWWWSSDSSSSSSS" +
			"LLLAPMAAMMMWWWPSSWSSSSWWW" +
			"FFFFAAAAAAAWWWPPMMSFAAWWW" +
			"WWLLLLLLLLLLILL PWKKKKWWW" +
			"PAASAAASPFSSASSFFAAWWWWPP" +
			"PSASAASSSFSSSSSAWWWWWWWPW" +
			"WAAAAWWWWWWWWWWWPFWFFWFWP" +
			"WAAPAWWWWWWWWWWWPFWFFWFFF";
	static {
		tileset = new Tileset(FEResources.getTexture("terrain_tiles"), 16, 16);
	}
	public Tile(int x, int y, int id) {
		super(x,y);
		renderDepth = OverworldStage.TILE_DEPTH;
		this.id = id;
		char ch = terrainMap.charAt(id);
		if(ch == 'P') setTerrain(Terrain.PLAIN);
		else if(ch == 'W') setTerrain(Terrain.WALL);
		else if(ch == 'A') setTerrain(Terrain.PATH);
		else if(ch == 'S') setTerrain(Terrain.SEA);
		else if(ch == 'F') setTerrain(Terrain.FOREST);
		else if(ch == 'L') setTerrain(Terrain.FLOOR);
		else if(ch == 'K') setTerrain(Terrain.PEAK);
		else if(ch == 'M') setTerrain(Terrain.MOUNTAIN);
		else if(ch == 'T') setTerrain(Terrain.FORT);
		else setTerrain(Terrain.NONE);
	}
	
	public void render(){
		tileset.render(x, y, id%25, id/25, renderDepth);
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
}
