package chu.engine.anim;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

/**
 * Manages a set of animations, and allows
 * the user to switch between animations.
 * @author Shawn
 *
 */
public class Sprite {
	
	private HashMap<String, Animation> animations;
	protected Animation currentAnimation;
	
	public Sprite() {
		animations = new HashMap<String, Animation>();
	}
	
	/**
	 * Add a new animation to the sprite by creating an animation with the given parameters.
	 * @param name Name of the animation
	 * @param tex Texture to use as the spritesheet
	 * @param width Width of a single frame in pixels
	 * @param height Height of a single frame in pixels
	 * @param frames Number of frames in the animation
	 * @param speed Speed of the animation in frames per second
	 */
	public void addAnimation(String name, Texture tex, int width, int height, int rows, int columns, int speed) {
		Animation anim = new Animation(tex, width, height, rows, columns, speed);
		animations.put(name, anim);
		currentAnimation = anim;
	}
	
	/**
	 * Add a new single-image animation.
	 * @param name Name of the animation
	 * @param tex Texture to use as the static image
	 */
	public void addAnimation(String name, Texture tex) {
		Animation anim = new Animation(tex);
		animations.put(name, anim);
		currentAnimation = anim;
	}
	
	/**
	 * Add an existing animation to the sprite
	 * @param name Name of the animation
	 * @param anim Existing animation to add
	 */
	public void addAnimation(String name, Animation anim) {
		animations.put(name, anim);
		currentAnimation = anim;
	}
	
	/**
	 * Gets the animation with the given name.
	 * @param name Name of the animation
	 * @return Animation that corresponds with that name
	 */
	public Animation getAnimation(String name) {
		return animations.get(name);
	}
	
	public Animation getCurrentAnimation(){
		return currentAnimation;
	}
	
	/**
	 * Sets the current, rendering animation to the animation with the
	 * given name.
	 * @param name Name of the animation
	 */
	public void setAnimation(String name) {
		currentAnimation = animations.get(name);
	}
	
	/**
	 * @return Frame the current animation is on
	 */
	public int getFrame() {
		return currentAnimation.getFrame();
	}
	
	/**
	 * Sets the current animation to the given frame
	 * @param frame Frame to set the current animation to
	 */
	public void setFrame(int frame) {
		currentAnimation.setFrame(frame % currentAnimation.getLength());
	}
	
	/**
	 * @return Number of animations in the sprite
	 */
	public int size() {
		return animations.size();
	}
	
	/**
	 * Draws the sprite at the specified coordinates.
	 * @param x 
	 * @param y
	 * @param depth
	 */
	public void render(float x, float y, float depth) {
		if(currentAnimation == null) return;
		
		int width = currentAnimation.getWidth();
		int height = currentAnimation.getHeight();
		int frameX = currentAnimation.getFrame() % currentAnimation.getColumns();
		int frameY = currentAnimation.getFrame() / currentAnimation.getColumns();
		float x0 = ((float)frameX * width)/currentAnimation.getImageWidth();
		float x1 = ((float)(frameX+1) * width)/currentAnimation.getImageWidth();
		float y0 = ((float)frameY * height)/currentAnimation.getImageHeight();
		float y1 = ((float)(frameY+1) * height)/currentAnimation.getImageHeight();
		Texture texture = currentAnimation.getTexture();
		
		Renderer.render(texture, x0, y0, x1, y1, x, y, x+width, y+height, depth);
		
	}

	/**
	 * Updates the current animation
	 */
	public void update() {
		if(currentAnimation == null) return;
		currentAnimation.update();
	}

	/**
	 * Draws the sprite 
	 * @param x
	 * @param y
	 * @param depth
	 * @param transform
	 */
	public void renderTransformed(float x, float y, float depth, Transform transform) {
		if(currentAnimation == null) return;
		
		int width = currentAnimation.getWidth();
		int height = currentAnimation.getHeight();
		int frameX = currentAnimation.getFrame() % currentAnimation.getColumns();
		int frameY = currentAnimation.getFrame() / currentAnimation.getColumns();
		float x0 = ((float)frameX * width)/currentAnimation.getImageWidth();
		float x1 = ((float)(frameX+1) * width)/currentAnimation.getImageWidth();
		float y0 = ((float)frameY * height)/currentAnimation.getImageHeight();
		float y1 = ((float)(frameY+1) * height)/currentAnimation.getImageHeight();
		Texture texture = currentAnimation.getTexture();
		Renderer.renderTransformed(texture, x0, y0, x1, y1, x, y, x+width, y+height, depth, transform);
	}

	public void setSpeed(int i) {
		currentAnimation.setSpeed(i);
	}
	
}
