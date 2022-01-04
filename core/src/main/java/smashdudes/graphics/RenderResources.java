package smashdudes.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ArrayMap;

public class RenderResources
{
    private static boolean hasInitialized = false;

    private static SpriteBatch s;
    private static ShapeRenderer sh;
    private static BitmapFont font;

    private static ArrayMap<String, ShaderProgram> shaders;
    private static ArrayMap<String, Texture> textures;

    public static void init()
    {
        if(hasInitialized) throw new IllegalStateException("Render Resources already initialized");
        hasInitialized = true;

        s = new SpriteBatch();
        s.enableBlending();

        sh = new ShapeRenderer();

        font = new BitmapFont();

        textures = new ArrayMap<>();
        shaders = new ArrayMap<>();
    }

    public static Texture getTexture(String fileName)
    {
        if (textures.containsKey(fileName))
        {
            return textures.get(fileName);
        }

        Texture t = new Texture(fileName);
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

    public static BitmapFont getFont()
    {
        return font;
    }
}
