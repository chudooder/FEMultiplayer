#version 120

uniform sampler2D texture1;
uniform sampler2D texture2;
varying highp vec4 vertColor;

void main() {
	vec4 fragmentColor = texture2D(texture1, gl_TexCoord[0].st);
	vec4 finalColor = fragmentColor;
	for(int i=0; i<4; i++) {
		vec4 sample = texture2D(texture2, vec2((i+0.5)/16.0, 0.0));
		sample.a = 1.0;
		if(all(equal(sample, fragmentColor))) {
			vec4 swap = texture2D(texture2, vec2((i+4.5)/16.0, 0.0));
			swap.a = 1.0;
			finalColor = swap;
		}
	}
	gl_FragColor = finalColor*vertColor;
}