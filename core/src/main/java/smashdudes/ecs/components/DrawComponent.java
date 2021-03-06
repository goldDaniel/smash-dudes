package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;
import smashdudes.graphics.RenderPass;
import smashdudes.graphics.RenderResources;

public class DrawComponent extends Component
{
    private final Color currentColor = Color.WHITE.cpy();

    public RenderPass pass = RenderPass.Default;

    public Texture texture = RenderResources.getTexture("textures/default.png");
    public final Vector2 scale = new Vector2(1.0f, 1.0f);

    public boolean facingLeft = false;
    public boolean maintainAspectRatio = true;

    public int zIndex = 10;

    public Color getColor()
    {
        return currentColor;
    }
}
