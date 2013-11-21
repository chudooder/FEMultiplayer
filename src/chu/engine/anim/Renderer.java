package chu.engine.anim;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Renderer {

	private static Camera camera;

	static {
		camera = new Camera(null, 0, 0);
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
		Color.white.bind();
		t.bind();

		// draw quad
		glBegin(GL_QUADS);
		glTexCoord2f(tx0, ty0);
		glVertex3f(x0, y0, depth);
		glTexCoord2f(tx1, ty0);
		glVertex3f(x1, y0, depth);
		glTexCoord2f(tx1, ty1);
		glVertex3f(x1, y1, depth);
		glTexCoord2f(tx0, ty1);
		glVertex3f(x0, y1, depth);
		glEnd();
	}

	public static void renderTransformed(Texture t, float tx0, float ty0,
			float tx1, float ty1, float x0, float y0, float x1, float y1,
			float depth, Transform transform) {
		t.bind();
		Color c = transform.color;
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

		// draw quad
		glBegin(GL_QUADS);
		glTexCoord2f(tx0, ty0);
		glVertex3f(x0, y0, depth);
		glTexCoord2f(tx1, ty0);
		glVertex3f(x1, y0, depth);
		glTexCoord2f(tx1, ty1);
		glVertex3f(x1, y1, depth);
		glTexCoord2f(tx0, ty1);
		glVertex3f(x0, y1, depth);
		glEnd();

		glPopMatrix();

	}

	public static void drawSquare(float x, float y, float s, float depth,
			Color c) {
		drawRectangle(x, y, x + s, y + s, depth, c);
	}

	public static void drawRectangle(float x0, float y0, float x1, float y1,
			float depth, Color c) {
		c.bind();
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
	}

	public static void drawRectangle(float x0, float y0, float x1, float y1,
			float depth, Color c0, Color c1, Color c2, Color c3) {
		glDisable(GL_TEXTURE_2D);
		// glLoadIdentity();
		glBegin(GL_QUADS);
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
	}

	public static void drawLine(float x0, float y0, float x, float y,
			float width, float depth, Color c1, Color c2) {
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);

		// glLoadIdentity();
		glBegin(GL_LINES);
		glColor4f(c1.r, c1.g, c1.b, c1.a);
		glVertex3f(x0, y0, depth);
		glColor4f(c2.r, c2.g, c2.b, c2.a);
		glVertex3f(x, y, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);

	}

	public static void drawLine(double x0, double y0, double x, double y,
			float width, float depth, Color c1, Color c2) {
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);

		// glLoadIdentity();
		glBegin(GL_LINES);
		glColor4f(c1.r, c1.g, c1.b, c1.a);
		glVertex3d(x0, y0, depth);
		glColor4f(c2.r, c2.g, c2.b, c2.a);
		glVertex3d(x, y, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}

	public static void drawTriangle(float x0, float y0, float x, float y,
			float x2, float y2, float depth, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glColor4f(c.r, c.g, c.b, c.a);
		glBegin(GL_TRIANGLES);
		glVertex3f(x0, y0, depth);
		glVertex3f(x, y, depth);
		glVertex3f(x2, y2, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);

	}

	public static void drawTriangle(double x0, double y0, double x1, double y1,
			double x2, double y2, float depth, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		glColor4d(c.r, c.g, c.b, c.a);
		glBegin(GL_TRIANGLES);
		glVertex3d(x0, y0, depth);
		glVertex3d(x1, y1, depth);
		glVertex3d(x2, y2, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}

	public static void setCamera(Camera c) {
		camera = c;
	}

	public static Camera getCamera() {
		return camera;
	}

}
