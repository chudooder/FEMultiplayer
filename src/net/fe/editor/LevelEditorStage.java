package net.fe.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.fe.Party;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.KeyboardEvent;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class LevelEditorStage extends Stage {

	private static Texture palette;
	private static Tileset tileset;
	private int selectedID;
	private int[][] tiles;
	private String levelName;
	private Set<SpawnPoint> spawns;

	static {
		try {
			palette = TextureLoader
					.getTexture("PNG", ResourceLoader
							.getResourceAsStream("res/terrain_tiles.png"));
			tileset = new Tileset(palette, 16, 16);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LevelEditorStage(String levelName) {
		super(null);
		selectedID = 0;
		this.levelName = levelName;
		tiles = new int[3][3];
		spawns = new HashSet<SpawnPoint>();
		try {
            FileInputStream in = new FileInputStream(new File("levels/"+levelName+".lvl"));
            ObjectInputStream ois = new ObjectInputStream(in);
            Level level = (Level) ois.readObject();
            tiles = level.tiles;
            spawns = level.spawns;
            if(spawns == null) spawns = new HashSet<SpawnPoint>();
            ois.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void beginStep() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (Mouse.isButtonDown(0)) {
				selectedID = Math.min((Game.getWindowHeight() - Mouse.getY())
						/ 16 * 25 + Mouse.getX() / 16, 25 * 40 - 1);
			}
		} else {
			if (Mouse.isButtonDown(0)) {
				try {
					tiles[(Game.getWindowHeight() - Mouse.getY()) / 16][Mouse
							.getX() / 16] = selectedID;
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("Tried to place tile out of bounds");
				}
			} else if (Mouse.isButtonDown(1)) {
				try {
					tiles[(Game.getWindowHeight() - Mouse.getY()) / 16][Mouse
						.getX() / 16] = 0;
				} catch (ArrayIndexOutOfBoundsException e) {
					System.err.println("Tried to place tile out of bounds");
				}
			}
			List<KeyboardEvent> keys = Game.getKeys();
			for (KeyboardEvent ke : keys) {
				if (ke.state) {
					if (ke.key == Keyboard.KEY_S) {
						modifySize(0, 1);
					} else if (ke.key == Keyboard.KEY_W) {
						modifySize(0, -1);
					} else if (ke.key == Keyboard.KEY_A) {
						modifySize(-1, 0);
					} else if (ke.key == Keyboard.KEY_D) {
						modifySize(1, 0);
					} else if (ke.key == Keyboard.KEY_Z) {
						SpawnPoint spawn = new SpawnPoint(
								Mouse.getX() / 16,
								(Game.getWindowHeight() - Mouse.getY()) / 16,
								Party.TEAM_BLUE);
						if(!spawns.add(spawn)) {
							System.err.println("Spawnpoint already exists");
						} else {
							System.out.println("Spawnpoint added at ("+spawn.x+", "+spawn.y+")");
						}
					} else if (ke.key == Keyboard.KEY_X) { 
						SpawnPoint spawn = new SpawnPoint(
								Mouse.getX() / 16,
								(Game.getWindowHeight() - Mouse.getY()) / 16,
								Party.TEAM_RED);
						if(!spawns.add(spawn)) {
							System.err.println("Spawnpoint already exists");
						} else {
							System.out.println("Spawnpoint added at ("+spawn.x+", "+spawn.y+")");
						}
					} else if (ke.key == Keyboard.KEY_C) { 
						SpawnPoint spawn = new SpawnPoint(
								Mouse.getX() / 16,
								(Game.getWindowHeight() - Mouse.getY()) / 16,
								Party.TEAM_GREEN);
						if(!spawns.add(spawn)) {
							System.err.println("Spawnpoint already exists");
						} else {
							System.out.println("Spawnpoint added at ("+spawn.x+", "+spawn.y+")");
						}
					} else if (ke.key == Keyboard.KEY_V) { 
						spawns.remove(new SpawnPoint(
								Mouse.getX() / 16,
								(Game.getWindowHeight() - Mouse.getY()) / 16,
								null));
					} else if (ke.key == Keyboard.KEY_F1) { 
						Level level = new Level(tiles[0].length, tiles.length, tiles, spawns);
						File file = new File("levels/"+levelName+".lvl");
		                FileOutputStream fo;
		                ObjectOutputStream oos;
		                try {
		                        fo = new FileOutputStream(file);
		                        oos = new ObjectOutputStream(fo);
		                        oos.writeObject(level);
		                        oos.close();
		                        System.out.println("Level serialization successful.");
		                } catch (FileNotFoundException e) {
		                        System.out.println("Invalid file path!");
		                        e.printStackTrace();
		                } catch (IOException e) {
		                        System.err.println("Failed to create object output stream");
		                        e.printStackTrace();
		                }
					}
				}
			}
		}
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			Renderer.render(palette, 0, 0, 1, 1, 0, 0, 400, 640, 0.1f);
			int x = selectedID % 25 * 16;
			int y = selectedID / 25 * 16;
			Renderer.drawLine(x - 1, y - 1, x + 17, y - 1, 1, 0, Color.red,
					Color.red);
			Renderer.drawLine(x + 17, y - 1, x + 17, y + 17, 1, 0, Color.red,
					Color.red);
			Renderer.drawLine(x - 1, y + 17, x + 17, y + 17, 1, 0, Color.red,
					Color.red);
			Renderer.drawLine(x - 1, y - 1, x - 1, y + 17, 1, 0, Color.red,
					Color.red);
		} else {
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					tileset.render(j * 16, i * 16, tiles[i][j] % 25,
							tiles[i][j] / 25, 0.5f);
				}
			}
			
			for(SpawnPoint sp : spawns) {
				Color c = new Color(sp.team);
				c.a = 0.3f;
				Renderer.drawSquare(sp.x*16, sp.y*16, 16, 0.4f, c);
			}

			int x = tiles[0].length * 16;
			int y = tiles.length * 16;
			Renderer.drawLine(0, 0, x, 0, 1, 0, Color.red, Color.red);
			Renderer.drawLine(x, 0, x, y, 1, 0, Color.red, Color.red);
			Renderer.drawLine(0, y, x, y, 1, 0, Color.red, Color.red);
			Renderer.drawLine(0, 0, 0, y, 1, 0, Color.red, Color.red);
		}
	}

	private void modifySize(int dx, int dy) {
		int width = Math.max(0, tiles[0].length + dx);
		int height = Math.max(0, tiles.length + dy);
		int[][] newTiles = new int[height][width];
		int a = Math.min(height, tiles.length);
		int b = Math.min(width, tiles[0].length);
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				newTiles[i][j] = tiles[i][j];
			}
		}
		tiles = newTiles;
	}

}
