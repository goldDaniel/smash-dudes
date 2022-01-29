package smashdudes.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ArrayMap;

public class RenderResources
{
    private static boolean hasInitialized = false;

    private static SpriteBatch s;
    private static ShapeRenderer sh;

    private static ArrayMap<String, ShaderProgram> shaders;

    private static ArrayMap<String, Texture> textures;

    private static ArrayMap<String, BitmapFont> fonts;

    public static void init()
    {
        if(hasInitialized) throw new IllegalStateException("Render Resources already initialized");
        hasInitialized = true;

        s = new SpriteBatch();
        s.enableBlending();

        sh = new ShapeRenderer();


        textures = new ArrayMap<>();
        shaders = new ArrayMap<>();
        fonts = new ArrayMap<>();
    }

    public static Texture getTexture(String fileName)
    {
        if (textures.containsKey(fileName))
        {
            return textures.get(fileName);
        }

        Texture t = new Texture(Gdx.files.internal(fileName), true);
        textures.put(fileName, t);

        return t;
    }

    public static ShaderProgram getShader(String vertexPath, String fragmentPath)
    {
        String key = vertexPath + "##" + fragmentPath;
        if(shaders.containsKey(key))
        {
            return shaders.get(key);
        }

        FileHandle vertHandle = Gdx.files.internal(vertexPath);
        FileHandle fragHandle = Gdx.files.internal(fragmentPath);

        ShaderProgram shader = new ShaderProgram(vertHandle, fragHandle);
        if (!shader.isCompiled())
        {
            throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        }

        return shader;
    }

    public static SpriteBatch getSpriteBatch()
    {
        return s;
    }

    public static ShapeRenderer getShapeRenderer()
    {
        return sh;
    }

    public static BitmapFont getFont(String fontName, int fontSize)
    {
        String fileName = "fonts/" + fontName + ".ttf";
        String key = fileName + "##" + fontSize;
        if(fonts.containsKey(key))
        {
            return fonts.get(key);
        }

        BitmapFont font = createFont(fileName, fontSize);
        fonts.put(key, font);

        return font;
    }

    private static BitmapFont createFont(String filename, int fontSize)
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(filename));

        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = fontSize;

        return generator.generateFont(params);
    }
}
