#ifdef GL_ES
    #define LOWP lowp		
    precision mediump float;
#else
    #define LOWP
#endif

//v_color.a will hold the blend factor between the texture color and vertex color
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main()
{
    vec4 textureColor = texture2D(u_texture, v_texCoords); 
    gl_FragColor = lerp(v_color, textureColor, v_color.a);

    gl_FragColor.a = textureColor.a;
}