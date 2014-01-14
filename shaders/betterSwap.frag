#version 120

uniform sampler2D texture1;
uniform sampler1D texture2;
varying highp vec4 vertColor;

void main() {
	vec4 fragmentColor = texture2D(texture1, gl_TexCoord[0].st);
	vec4 finalColor = fragmentColor;
	for(int i=0; i<8; i++) {
		vec4 sample = texture1D(texture2, i/16.0);
		sample.a = 1.0;
		if(all(equal(sample, fragmentColor))) {
			vec4 swap = texture1D(texture2, (i+8)/16.0);
			swap.a = 1.0;
			finalColor = swap;
		}
	}
	gl_FragColor = finalColor*vertColor;
	gl_FragColor = texture1D(texture2, 0.0/16.0);
}