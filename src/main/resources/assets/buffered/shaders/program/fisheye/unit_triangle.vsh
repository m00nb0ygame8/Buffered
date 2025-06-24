#version 150

in vec4 Position;    // (x, y, z, w) in pixel units
uniform vec2 ScreenSize; // (f, g)

void main() {
    // Convert from pixel to NDC: [-1, 1]
    vec2 ndc = (Position.xy / ScreenSize) * 2.0 - 1.0;
    gl_Position = vec4(ndc, 0.0, 1.0);
}
