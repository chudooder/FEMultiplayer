varying vec4 vertColor;
uniform sampler2D texture0;
uniform float arg0;

void main() {
	vec4 col = texture2D(texture0, gl_TexCoord[0].st)*vertColor;
    gl_FragColor = vec4(pow(col.r, arg0), pow(col.g, arg0), pow(col.b, arg0), col.a);
}