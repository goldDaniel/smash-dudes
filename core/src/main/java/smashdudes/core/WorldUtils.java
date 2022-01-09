package smashdudes.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.text.View;

public class WorldUtils
{
    private Viewport viewport;

    public WorldUtils(Viewport viewport)
    {
        this.viewport = viewport;
    }

    public Vector2 getWorldFromScreen(Vector2 pos)
    {
        return viewport.unproject(pos);
    }
}
