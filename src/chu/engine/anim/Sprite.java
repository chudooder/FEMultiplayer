package chu.engine.anim;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

public class Sprite {
	
	private HashMap<String, Animation> animations;
	protected Animation currentAnimation;
	
	public Sprite() {
		animations = new HashMap<String, Animation>();
	}
	
	public void addAnimation(String s, Texture t, int width, int height, int frames, int speed) {
		Animation anim = new Animation(t, width, height, frames, speed);
		animations.put(s, anim);
		currentAnimation = anim;
	}
	
	public void addAnimation(String s, Texture t) {
		Animation anim = new Animation(t);
		animations.put(s, anim);
		currentAnimation = anim;
	}
	
	public void addAnimation(String s, Animation a) {
		animations.put(s, a);
		currentAnimation = a;
	}
	
	public Animation getAnimation(String s) {
		return animations.get(s);
	}
	
	public void setAnimation(String s) {
		currentAnimation = animations.get(s);
	}
	
	public int getFrame() {
		return currentAnimation.getFrame();
	}
	
	public void setFrame(int i) {
		currentAnimation.setFrame(i % currentAnimation.getLength());
	}
	
	public int size() {
		return animations.size();
	}
	
	public void render(float x, float y, float depth) {
		if(currentAnimation == null) return;
		
		int width = currentAnimation.getWidth();
		int height = currentAnimation.getHeight();
		int fakelength = currentAnimation.getImageWidth()/width;
		float x0 = (float)(currentAnimation.getFrame())/(float)(fakelength);
		float x1 = (float)(currentAnimation.getFrame()+1)/(float)(fakelength);
		Texture texture = currentAnimation.getTexture();
		
		Renderer.render(texture, x0, 0, x1, 1, (int)x, (int)y, (int)(x+width), (int)(y+height), depth);
		
	}
	
	public void renderRotated(int x, int y, float depth, float angle) {
		if(currentAnimation == null) return;
		
		int width = currentAnimation.getWidth();
		int height = currentAnimation.getHeight();
		int fakelength = currentAnimation.getImageWidth()/width;
		float x0 = (float)(currentAnimation.getFrame())/(float)(fakelength);
		float x1 = (float)(currentAnimation.getFrame()+1)/(float)(fakelength);
		
		Texture texture = currentAnimation.getTexture();
		
		Transform t = new Transform();
		t.setRotation(angle);
		Renderer.renderTransformed(texture, x0, 0, x1, 1, x, y, x+width, y+height, depth, t);
	}

	public void update() {
		if(currentAnimation == null) return;
		currentAnimation.update();
	}

	public void renderTransformed(float x, float y, float depth, Transform t) {
		if(currentAnimation == null) return;
		
		int width = currentAnimation.getWidth();
		int height = currentAnimation.getHeight();
		int fakelength = currentAnimation.getImageWidth()/width;
		float x0 = (float)(currentAnimation.getFrame())/(float)(fakelength);
		float x1 = (float)(currentAnimation.getFrame()+1)/(float)(fakelength);
		
		Texture texture = currentAnimation.getTexture();
		Renderer.renderTransformed(texture, x0, 0, x1, 1, (int)x, (int)y, (int)x+width, (int)y+height, depth, t);
	}

	public void setSpeed(int i) {
		currentAnimation.setSpeed(i);
	}
	
}
