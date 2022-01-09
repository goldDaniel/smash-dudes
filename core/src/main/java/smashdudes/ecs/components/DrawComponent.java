package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import smashdudes.ecs.Component;
import smashdudes.graphics.RenderPass;
import smashdudes.graphics.RenderResources;

public class DrawComponent extends Component
{
    private final Color currentColor = Color.WHITE.cpy();

    public RenderPass pass = RenderPass.Default;

    public Texture texture = RenderResources.getTexture("textures/default.png");
    public float scale;

    public boolean facingLeft = false;

    public int zIndex = 10;

    public Color getColor()
    {
        return currentColor;
    }
}
