package chu.engine.anim;

import org.newdawn.slick.openal.Audio;

public class AudioPlayer {

	static Camera camera;
	static float globalGain;

	public static void setCamera(Camera c) {
		camera = c;
		globalGain = -0.5f;
	}

	public static void playAudio(Audio audio, float pitch, float gain, float x,
			float y, float z, float fade) {
		audio.playAsSoundEffect(pitch, gain + globalGain, false, 
				(x - camera.getX()) / fade,
				(y - camera.getY()) / fade, z);
	}
}
