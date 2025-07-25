#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float GameTime;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    
    // 创建霜花效果
    float frost = 0.0;
    for(float i = 0.0; i < 6.0; i++) {
        vec2 offset = vec2(cos(GameTime * 2.0 + i), sin(GameTime * 2.0 + i)) * 0.01;
        frost += texture(DiffuseSampler, texCoord + offset).a;
    }
    frost /= 6.0;
    
    // 添加冰晶效果
    vec2 pixelCoord = texCoord * OutSize;
    float pattern = mod(pixelCoord.x + pixelCoord.y, 2.0);
    float sparkle = sin(GameTime * 8.0 + pattern * 3.14159) * 0.5 + 0.5;
    
    // 混合颜色
    vec4 frostColor = vec4(0.8, 0.9, 1.0, frost * 0.5);
    vec4 sparkleColor = vec4(1.0, 1.0, 1.0, sparkle * frost * 0.3);
    
    fragColor = mix(color, frostColor, frost * 0.5) + sparkleColor;
} 