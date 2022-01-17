package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;

public class ParticleComponent extends Component
{
    private final float lifetime;
    private float currentLifetime;

    private final Array<Color> colors;

    private final float startSize;
    private final float endSize;

    private final Vector2 velocity;

    public ParticleComponent(float lifetime, Array<Color> colors, Vector2 velocity)
    {
        this(lifetime, colors, velocity, 1, 1);
    }

    public ParticleComponent(float lifetime, Array<Color> colors, Vector2 velocity, float startSize, float endSize)
    {
        if(colors.isEmpty()) throw new IllegalArgumentException("Must have at least 1 color");

        this.lifetime = lifetime;
        this.currentLifetime = 0;

        this.colors = colors;

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
        if(colors.size == 1) return colors.first().cpy();

        float totalPercentage = MathUtils.clamp(currentLifetime / lifetime, 0.f, 1.0f);
        int intervalCount = colors.size - 1;

        int indexA = MathUtils.floor(totalPercentage * intervalCount);
        int indexB = indexA + 1;

        float intervalPercentage = 1.0f / intervalCount;
        float tValue = totalPercentage % intervalPercentage;

        float t = MathUtils.map(0, intervalPercentage, 0.0f, 1.0f, tValue);

        Color result = colors.get(indexA).cpy();
        result.lerp(colors.get(indexB), t);

        return result;
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