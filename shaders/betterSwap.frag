#version 120

uniform sampler2D texture1;
uniform sampler2D texture2;
uniform float arg0;		// Pixel width of texture
uniform float arg1;		// Pixel height of texture
uniform float arg2;		// Swap offset (vertical)
uniform float arg3;		// Pixels to be swapped
varying vec4 vertColor;

void main() {
	vec4 fragmentColor = texture2D(texture1, gl_TexCoord[0].st);
	vec4 finalColor = fragmentColor;
	for(int i=0; i<arg3; i++) {
		vec4 sample = texture2D(texture2, vec2((i+0.5)/arg0, 0.5/arg1));
		sample.a = 1.0;
		if(all(equal(sample, fragmentColor))) {
			vec4 swap = texture2D(texture2, vec2((i+0.5)/arg0, (arg2+0.5)/arg1));
			swap.a = 1.0;
			finalColor = swap;
		}
	}
	gl_FragColor = finalColor*vertColor;
	// gl_FragColor = texture2D(texture2, vec2(2.5/arg0, 0.0/arg1));
}