#version 130

uniform sampler2D texture1;
uniform sampler2D texture2;
varying vec4 vertColor;

void main() {
	vec4 fragmentColor = texture2D(texture1, gl_TexCoord[0].st);
	vec4 finalColor = fragmentColor;
	for(int i=0; i<4; i++)
	{
		vec4 pixel = texture2D(texture2, vec2(i/16.0, 0.0));
		if(fragmentColor.r == pixel.r) {
			finalColor = texture2D(texture2, vec2((i+4.0)/16.0, 0.0));
			break;
		}
	}
	gl_FragColor = finalColor*vertColor;
}