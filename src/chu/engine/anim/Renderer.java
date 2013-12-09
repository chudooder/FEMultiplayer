package chu.engine.anim;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import chu.engine.Game;
import chu.engine.Resources;

public class Renderer {

	private static Camera camera;
	private static RectClip clip;
	private static final int SCALE_FILTER = GL_NEAREST;
	private static int scaleX;
	private static int scaleY;
	private static Color color;

	static {
		camera = new Camera(null, 0, 0);
		clip = null;
		color = Color.white;
	}

	/***
	 * Draws the given subtexture at the given coordinates.
	 * 
	 * @param t
	 *            Texture to be drawn
	 * @param tx0
	 *            First texture x coord
	 * @param ty0
	 *            First texture y coord
	 * @param tx1
	 *            Second texture x coord
	 * @param ty1
	 *            Second texture y coord
	 * @param x0
	 *            First render x coord
	 * @param y0
	 *            First render y coord
	 * @param x1
	 *            Second render x coord
	 * @param y1
	 *            Second render y coord
	 */
	public static void render(Texture t, float tx0, float ty0, float tx1,
			float ty1, float x0, float y0, float x1, float y1, float depth) {
		color.bind();
		t.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, SCALE_FILTER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, SCALE_FILTER);
		// Compensation for non power of 2 images
		float txi = tx0*t.getImageWidth()/t.getTextureWidth();
		float tyi = ty0*t.getImageHeight()/t.getTextureHeight();
		float txf = tx1*t.getImageWidth()/t.getTextureWidth();
		float tyf = ty1*t.getImageHeight()/t.getTextureHeight();

		// draw quad
		glBegin(GL_QUADS);
		glTexCoord2f(txi, tyi);
		glVertex3f(x0, y0, depth);
		glTexCoord2f(txf, tyi);
		glVertex3f(x1, y0, depth);
		glTexCoord2f(txf, tyf);
		glVertex3f(x1, y1, depth);
		glTexCoord2f(txi, tyf);
		glVertex3f(x0, y1, depth);
		glEnd();
		if(clip != null && !clip.persistent) clip.destroy();
	}

	public static void renderTransformed(Texture t, float tx0, float ty0,
			float tx1, float ty1, float x0, float y0, float x1, float y1,
			float depth, Transform transform) {
		t.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, SCALE_FILTER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, SCALE_FILTER);
		Color c = transform.color.multiply(color);
		glColor4f(c.r, c.g, c.b, c.a);
		glPushMatrix();
		glTranslatef(x0, y0, depth);
		glTranslatef(transform.translateX, transform.translateY, 0);
		glScalef(transform.scaleX, transform.scaleY, 0);
		glTranslatef(-x0 + (x0 + x1) / 2, -y0 + (y0 + y1) / 2, 0);
		glRotatef(transform.rotation / (float) Math.PI * 180, 0, 0, 1);
		glTranslatef(-(x0 + x1) / 2, -(y0 + y1) / 2, -depth);

		// do flip operations
		if (transform.flipHorizontal) {
			float temp = tx0;
			tx0 = tx1;
			tx1 = temp;
		}

		if (transform.flipVertical) {
			float temp = ty0;
			ty0 = ty1;
			ty1 = temp;
		}
		
		// Compensation for non power of 2 images
		float txi = tx0*t.getImageWidth()/t.getTextureWidth();
		float tyi = ty0*t.getImageHeight()/t.getTextureHeight();
		float txf = tx1*t.getImageWidth()/t.getTextureWidth();
		float tyf = ty1*t.getImageHeight()/t.getTextureHeight();

		// draw quad
		glBegin(GL_QUADS);
		glTexCoord2f(txi, tyi);
		glVertex3f(x0, y0, depth);
		glTexCoord2f(txf, tyi);
		glVertex3f(x1, y0, depth);
		glTexCoord2f(txf, tyf);
		glVertex3f(x1, y1, depth);
		glTexCoord2f(txi, tyf);
		glVertex3f(x0, y1, depth);
		glEnd();

		glPopMatrix();
		if(clip != null && !clip.persistent) clip.destroy();
	}

	public static void drawSquare(float x, float y, float s, float depth,
			Color c) {
		drawRectangle(x, y, x + s, y + s, depth, c);
	}

	public static void drawRectangle(float x0, float y0, float x1, float y1,
			float depth, Color c) {
		c = c.multiply(color);
		glDisable(GL_TEXTURE_2D);
		glColor4f(c.r, c.g, c.b, c.a);

		// glLoadIdentity();
		glBegin(GL_QUADS);
		glVertex3f(x0, y0, depth);
		glVertex3f(x1, y0, depth);
		glVertex3f(x1, y1, depth);
		glVertex3f(x0, y1, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if(clip != null && !clip.persistent) clip.destroy();
	}

	public static void drawRectangle(float x0, float y0, float x1, float y1,
			float depth, Color c0, Color c1, Color c2, Color c3) {
		glDisable(GL_TEXTURE_2D);
		// glLoadIdentity();
		glBegin(GL_QUADS);
		c0 = c0.multiply(color);
		c1 = c1.multiply(color);
		c2 = c2.multiply(color);
		c3 = c3.multiply(color);
		glColor4f(c0.r, c0.g, c0.b, c0.a);
		glVertex3f(x0, y0, depth);
		glColor4f(c1.r, c1.g, c1.b, c1.a);
		glVertex3f(x1, y0, depth);
		glColor4f(c2.r, c2.g, c2.b, c2.a);
		glVertex3f(x1, y1, depth);
		glColor4f(c3.r, c3.g, c3.b, c3.a);
		glVertex3f(x0, y1, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if(clip != null && !clip.persistent) clip.destroy();
	}

	public static void drawLine(float x0, float y0, float x, float y,
			float width, float depth, Color c1, Color c2) {
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);

		// glLoadIdentity();
		glBegin(GL_LINES);
		c1 = c1.multiply(color);
		c2 = c2.multiply(color);
		glColor4f(c1.r, c1.g, c1.b, c1.a);
		glVertex3f(x0, y0, depth);
		glColor4f(c2.r, c2.g, c2.b, c2.a);
		glVertex3f(x, y, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if(clip != null && !clip.persistent) clip.destroy();
	}

	public static void drawTriangle(float x0, float y0, float x, float y,
			float x2, float y2, float depth, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		c = c.multiply(color);
		glColor4f(c.r, c.g, c.b, c.a);
		glBegin(GL_TRIANGLES);
		glVertex3f(x0, y0, depth);
		glVertex3f(x, y, depth);
		glVertex3f(x2, y2, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if(clip != null && !clip.persistent) clip.destroy();
	}
	
	public static void drawString(String fontName, String string, float x, float y, float depth) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, SCALE_FILTER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, SCALE_FILTER);
		Resources.getBitmapFont(fontName).render(string, x, y, depth);
		if(clip != null && !clip.persistent) clip.destroy();
	}
	
	public static void drawTransformedString(String fontName, String string, float x, float y, float depth, Transform t) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, SCALE_FILTER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, SCALE_FILTER);
		Resources.getBitmapFont(fontName).renderTransformed(string, x, y, depth, t);
		if(clip != null && !clip.persistent) clip.destroy();
	}
	
	public static void translate(float x, float y) {
		glTranslatef(x, y, 0);
	}
	
	public static void scale(int x, int y) {
		scaleX = x;
		scaleY = y;
		glScalef(x, y, 0);
	}
	
	public static void setColor(Color c) {
		if(c == null) color = Color.white;
		else color = c;
	}
	
	public static void pushMatrix() {
		glPushMatrix();
	}
	
	public static void popMatrix() {
		glPopMatrix();
	}
	
	
	
	public static void setCamera(Camera c) {
		camera = c;
	}

	public static Camera getCamera() {
		return camera;
	}
    
    public static void addClip(int x0, int y0, int w, int h, boolean persistent) {
    	clip = new RectClip(x0, y0, w, h, persistent);
    }
    
    public static void removeClip() {
    	if(clip != null) clip.destroy();
    }
    
    static class RectClip {
    	boolean persistent;
    	public RectClip(int x0, int y0, int w, int h, boolean p) {
    		persistent = p;
    		glEnable(GL_SCISSOR_TEST);
//    		System.out.println(scaleX*x0+" "+(Game.getWindowHeight()-y0-scaleY*h)+" "+scaleX*w+" "+scaleY*h);
    		glScissor(scaleX*x0, Game.getWindowHeight()-y0-scaleY*h, scaleX*w, scaleY*h);
    	}
    	
    	public void destroy() {
    		glDisable(GL_SCISSOR_TEST);
    	}
    }

}
