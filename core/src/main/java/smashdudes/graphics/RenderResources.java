package smashdudes.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ArrayMap;

public class RenderResources
{
    private static boolean hasInitialized = false;

    private static SpriteBatch s;
    private static ShapeRenderer sh;
    private static BitmapFont font;

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
    }

    public static Texture getTexture(String fileName)
    {
        if (textures.containsKey(fileName))
        {
            return textures.get(fileName);
        }
        else
        {
            Texture t = new Texture(fileName);
            textures.put(fileName, t);

            return t;
        }
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
