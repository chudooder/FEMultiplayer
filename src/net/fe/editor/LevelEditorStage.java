package net.fe.editor;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;
import chu.engine.anim.Tileset;

public class LevelEditorStage extends Stage {

	private static Texture palette;
	private static Tileset tileset;
	private int selectedID;
	private int[][] tiles;

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

	public LevelEditorStage() {
		selectedID = 0;
		tiles = new int[3][3];
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
				tiles[(Game.getWindowHeight() - Mouse.getY()) / 16][Mouse
						.getX() / 16] = selectedID;
			} else if (Mouse.isButtonDown(1)) {
				tiles[(Game.getWindowHeight() - Mouse.getY()) / 16][Mouse
						.getX() / 16] = 0;
			}
			HashMap<Integer, Boolean> keys = Game.getKeys();
			for (int key : keys.keySet()) {
				if (keys.get(key)) {
					if (key == Keyboard.KEY_S) {
						modifySize(0, 1);
					} else if (key == Keyboard.KEY_W) {
						modifySize(0, -1);
					} else if (key == Keyboard.KEY_A) {
						modifySize(-1, 0);
					} else if (key == Keyboard.KEY_D) {
						modifySize(1, 0);
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
