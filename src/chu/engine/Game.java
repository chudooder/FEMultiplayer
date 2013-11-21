package chu.engine;
import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public abstract class Game {
	
	protected static int windowWidth = 640;
	protected static int windowHeight = 480;
	protected boolean paused = false;
	protected static HashMap<Integer, Boolean> keys;
	protected static HashMap<MouseEvent, Boolean> mouseEvents;
	protected long time;
	protected static long timeDelta;
	
	public void init(int width, int height, String name) {
		time = System.nanoTime();
		
		windowWidth = width;
		windowHeight = height;

		try {
			Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
			Display.create();
			Display.setTitle(name);
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//init OpenGL
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.01f);
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glClearDepth(1.0f);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, windowWidth, windowHeight);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, windowWidth, windowHeight, 0, 1, -1);		//It's basically a camera
		glMatrixMode(GL_MODELVIEW);
		
		keys = new HashMap<Integer, Boolean>();
		mouseEvents = new HashMap<MouseEvent, Boolean>();
	}
	
	public abstract void loop();


	public static void getInput() {
		Keyboard.poll();
		keys.clear();
		while(Keyboard.next()) {
			int keyEvent = Keyboard.getEventKey();
			keys.put(keyEvent, Keyboard.getEventKeyState());
		}
		Mouse.poll();
		mouseEvents.clear();
		while(Mouse.next()) {
			MouseEvent me = new MouseEvent(
					Mouse.getEventX(),
					Mouse.getEventY(),
					Mouse.getEventDWheel(),
					Mouse.getEventButton());
			mouseEvents.put(me, Mouse.getEventButtonState());
		}
	}
	
	public static HashMap<Integer, Boolean> getKeys() {
		return keys;
	}
	
	public static HashMap<MouseEvent, Boolean> getMouseEvents() {
		return mouseEvents;
	}

	public static long getDelta() {
		return timeDelta;
	}
	
	public static float getDeltaMillis() {
		return timeDelta/1000000.0f;
	}
	
	public static float getDeltaSeconds() {
		return timeDelta/1000000000.0f;
	}
	
	public static int getWindowWidth() {
		return windowWidth;
	}
	
	public static int getWindowHeight() {
		return windowHeight;
	}
}
