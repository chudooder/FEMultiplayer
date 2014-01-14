varying vec4 vertColor;
uniform sampler2D texture0;

void main() {
	vec4 col = texture2D(texture0, gl_TexCoord[0].st)*vertColor;
    gl_FragColor = vec4(sqrt(col.r), sqrt(col.g), sqrt(col.b), col.a);
}