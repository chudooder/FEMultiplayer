package chu.engine.anim;

import org.newdawn.slick.openal.Audio;

import chu.engine.Game;
import chu.engine.Resources;

public class AudioPlayer {

	static Camera camera;
	static float globalGain;

	public static void setCamera(Camera c) {
		camera = c;
		globalGain = 0f;
	}

	public static void playAudio(String name, float pitch, float gain, float x,
			float y, float z, float fade) {
		Audio audio = Resources.getAudio(name);
		float cx, cy;
		if(camera == null) {
			cx = Game.getWindowWidth()/2;
			cy = Game.getWindowHeight()/2;
		} else {
			cx = camera.getX();
			cy = camera.getY();
		}
		audio.playAsSoundEffect(pitch, gain + globalGain, false, 
				(x - cx) / fade, (y - cy) / fade, z);
	}
	
	public static void playAudio(String name, float pitch, float gain) {
		Audio audio = Resources.getAudio(name);
		audio.playAsMusic(pitch, gain + globalGain, false);
	}
}
