precision mediump float;
varying vec4 v_color;

void main() {
	gl_FragColor = vec4( v_color.x, v_color.y, v_color.z, 1.0 );
}
