package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import smashdudes.ecs.Component;
import smashdudes.graphics.RenderPass;

public class DrawComponent extends Component
{
    private final Color currentColor = Color.WHITE.cpy();

    public RenderPass pass = RenderPass.Default;

    public Texture texture;
    public float scale;

    public boolean facingLeft = false;

    public Color getColor()
    {
        return currentColor;
    }
}
