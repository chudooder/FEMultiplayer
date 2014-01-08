
varying vec4 vertColor;
uniform sampler2D texture0;
uniform sampler1D texture1;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_TexCoord[1] = gl_MultiTexCoord1;
    vertColor = gl_Color;
}