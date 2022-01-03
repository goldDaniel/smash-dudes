#ifdef GL_ES
    #define LOWP lowp		
    precision mediump float;
#else
    #define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main()
{
    vec4 inputColor = v_color * texture2D(u_texture, v_texCoords);

    gl_FragColor.rgb = vec3(1.0, 1.0, 1.0) - inputColor.rgb;
    gl_FragColor.a = inputColor.a;
}