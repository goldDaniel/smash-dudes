package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;

public class ParticleComponent extends Component
{
    private final float lifetime;
    private float currentLifetime;

    private final Color startColor;
    private final Color endColor;

    private final float startSize;
    private final float endSize;

    private final Vector2 velocity;

    public ParticleComponent(float lifetime, Color start, Color end, Vector2 velocity)
    {
        this(lifetime, start, end, velocity, 1, 1);
    }

    public ParticleComponent(float lifetime, Color start, Color end, Vector2 velocity, float startSize, float endSize)
    {
        this.lifetime = lifetime;
        this.currentLifetime = 0;

        this.startColor = start.cpy();
        this.endColor = end.cpy();

        this.velocity = velocity.cpy();

        this.startSize = startSize;
        this.endSize = endSize;
    }

    public void update(float dt)
    {
        currentLifetime += dt;
    }

    public float getSize()
    {
        float percentage = currentLifetime / lifetime;
        return MathUtils.lerp(startSize, endSize, percentage);
    }

    public Color getColor()
    {
        float percentage = currentLifetime / lifetime;

        Color color = new Color().set(startColor).lerp(endColor, percentage);
        color.a = MathUtils.clamp(color.a, 0.0f, 1.0f);

        return color;
    }

    public Vector2 getVelocity()
    {
        float percentage = currentLifetime / lifetime;
        return velocity.cpy().scl(1.0f - percentage);
    }

    public boolean isAlive()
    {
        return currentLifetime < lifetime;
    }
}
