precision mediump float;
varying vec4 v_color;
varying float depth;
void main() {
	gl_FragColor = vec4( v_color.x * depth, v_color.y * depth, v_color.z * depth, 1.0 );
}
