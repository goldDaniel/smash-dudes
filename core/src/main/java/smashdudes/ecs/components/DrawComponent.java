package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import smashdudes.ecs.Component;
import smashdudes.graphics.RenderPass;

public class DrawComponent extends Component
{
    private final Color targetColor = Color.WHITE.cpy();
    private final Color hitColor = Color.RED.cpy();

    private final Color currentColor = targetColor.cpy();

    private float interpTimer = 1;

    public RenderPass pass = RenderPass.None;

    public Texture texture;
    public float scale;

    public boolean facingLeft = false;

    public void hasBeenHit()
    {
        currentColor.set(hitColor);
        interpTimer = 0;
    }

    public void update(float dt)
    {
        interpTimer += dt;
        if(interpTimer > 1) interpTimer = 1;

        currentColor.set(hitColor).lerp(targetColor, interpTimer);
    }

    public Color getColor()
    {
        return currentColor;
    }
}
