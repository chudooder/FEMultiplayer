package net.fe;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class SoundTrack {
	private static String current;
	public static void loop(String name){
		if(name.equals(current)) return;
		try {
			current = name;
			Audio a = AudioLoader.getStreamingAudio("OGG", 
					ResourceLoader.getResource("res/music/"+name+".ogg"));
//			a.playAsMusic(1, 1, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void restart(){
		try {
			Audio a = AudioLoader.getStreamingAudio("OGG", 
					ResourceLoader.getResource("res/music/"+current+".ogg"));
//			a.playAsMusic(1, 1, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
