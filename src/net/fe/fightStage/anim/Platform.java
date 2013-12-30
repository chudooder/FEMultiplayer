package net.fe.fightStage.anim;

import org.newdawn.slick.opengl.Texture;

import net.fe.FEResources;
import net.fe.fightStage.FightStage;
import net.fe.overworldStage.Terrain;
import chu.engine.Entity;
import chu.engine.anim.Renderer;
import chu.engine.anim.Transform;

public class Platform extends Entity {
	private boolean left;
	private Texture texture;

	public Platform(Terrain t, boolean left, int range) {
		super(left ? 0 : FightStage.CENTRAL_AXIS, FightStage.FLOOR - 16);
		this.left = left;
		String txtName = t.name().toLowerCase();
		if(range > 1){
			txtName += "_far";
		}
		texture = FEResources.getTexture("platform_" + txtName);
		renderDepth = FightStage.PLATFORM_DEPTH;
	}

	public void render() {
		Transform t = new Transform();
		if (!left) {
			t.flipHorizontal();
		}
		Renderer.render(texture, 0, 0, 1, 1, x, y, x + 120, y + 40,
				1, t);
	}

}
