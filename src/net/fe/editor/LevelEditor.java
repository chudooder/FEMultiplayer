package net.fe.editor;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class LevelEditor extends Game {
	
	private static Stage currentStage;

	public static void main(String[] args) {
		LevelEditor game = new LevelEditor();
		game.init(960, 640, "Fire Emblem Level Editor");
		game.loop();
	}
	
	@Override
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		currentStage = new LevelEditorStage("fort");
	}
	
	@Override
	public void loop() {
		while(!Display.isCloseRequested()) {
			time = System.nanoTime();
			glClear(GL_COLOR_BUFFER_BIT |
			        GL_DEPTH_BUFFER_BIT |
			        GL_STENCIL_BUFFER_BIT);
			glClearDepth(1.0f);
			getInput();
			glPushMatrix();
			if(!paused) {
				currentStage.beginStep();
				currentStage.onStep();
				currentStage.processAddStack();
				currentStage.processRemoveStack();
				Renderer.getCamera().lookThrough();
				currentStage.render();
				currentStage.endStep();
			}
			glPopMatrix();
			Display.update();
			timeDelta = System.nanoTime()-time;
		}
		AL.destroy();
		Display.destroy();
	}

}
