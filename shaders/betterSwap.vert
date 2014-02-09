#version 120

varying vec4 vertColor;
uniform sampler2D texture0;
uniform sampler2D texture1;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    gl_TexCoord[0] = gl_MultiTexCoord0;
    vertColor = gl_Color;
}