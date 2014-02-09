package chu.engine.anim;

import org.lwjgl.opengl.GL20;

public class ShaderArgs {
	
	public String programName;
	public float[] args;

	public ShaderArgs(String prog, float... args) {
		this.programName = prog;
		this.args = args;
	}
	
	public ShaderArgs() {
		this.programName = "default";
		args = new float[0];
	}

	public void bindArgs(int program) {
		for(int i=0; i<args.length; i++) {
			int loc = GL20.glGetUniformLocation(program, "arg"+i);
			GL20.glUniform1f(loc, args[i]);
		}
	}
}
