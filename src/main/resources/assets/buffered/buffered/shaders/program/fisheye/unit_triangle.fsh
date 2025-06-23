#version 150
out vec4 fragColor;

uniform vec2   ScreenSize;
uniform float  Strength;
uniform sampler2D TextureSampler;

void main() {
    vec2 uv = gl_FragCoord.xy / ScreenSize;
    vec2 direction  = uv - vec2(0.5);
    float dist_from_center  = length(direction);
    float scale = 1.0 + Strength * pow(dist_from_center, 2.0);
    vec2 warped_uv  = vec2(0.5) + direction * scale;

    bool out_of_bounds = false;
    if (warped_uv.x < 0.0 || warped_uv.x > 1.0 || warped_uv.y < 0.0 || warped_uv.y > 1.0) {
        out_of_bounds = true;
    }

    vec3 color = out_of_bounds
    ? vec3(0.0)
    : texture(TextureSampler, warped_uv).rgb;

    fragColor = vec4(color, 1.0);
}
