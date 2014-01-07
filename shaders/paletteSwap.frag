#version 130

precision mediump float;
uniform sampler2D texture1;
uniform sampler2D texture2;
varying highp vec4 vertColor;

void main() {
        vec4 fragmentColor = texture2D(texture1, gl_TexCoord[0].st);
        vec4 finalColor = fragmentColor;
        if(all(equal(fragmentColor, vec4(57.0/255.0, 57.0/255.0, 148.0/255.0, 1.0))) == true) {
                finalColor = vec4(96.0/255.0, 40.0/255.0, 32.0/255.0, 1.0);
        }
        if(all(equal(fragmentColor, vec4(57.0/255.0, 82.0/255.0, 231.0/255.0, 1.0))) == true) {
                finalColor = vec4(168.0/255.0, 48.0/255.0, 40.0/255.0, 1.0);
        }
        if(all(equal(fragmentColor, vec4(41.0/255.0, 165.0/255.0, 255.0/255.0, 1.0))) == true) {
                finalColor = vec4(224.0/255.0, 16.0/255.0, 16.0/255.0, 1.0);
        }
        if(all(equal(fragmentColor, vec4(24.0/255.0, 247.0/255.0, 255.0/255.0, 1.0))) == true) {
                finalColor = vec4(248.0/255.0, 80.0/255.0, 72.0/255.0, 1.0);
        }
        gl_FragColor = finalColor*vertColor;
}