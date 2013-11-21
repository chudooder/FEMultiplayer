package chu.engine;




//Entity with a specific lifetime. It removes itself after its lifetime expires.
public class Particle extends Entity {
	
	private float timer;
	private int lifetime;
	
	public Particle(float x, float y, int lifetime) {
		super(x,y);
		this.lifetime = lifetime;
		timer = 0;
	}
	
	public void onStep() {
		super.onStep();
	}

	@Override
	public void beginStep() {
		timer += Game.getDeltaSeconds();
		if(timer > lifetime) {
			this.destroy();
		}
	}

	@Override
	public void endStep() {
		
	}

}
