package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;

public class ParticleEmitterComponent extends Component
{
    private float elapsedTime;
    private float time;

    public float emissionRate;

    public float lifetime = -1;

    public Vector2 emissionPoint;

    public Color startColor;
    public Color endColor;

    public float lifespanStartRange;
    public float lifespanEndRange;

    public Vector2 sizeStartRange;
    public Vector2 sizeEndRange;

    public Vector2 velocityMin;
    public Vector2 velocityMax;

    public int zIndex = 10;

    public void update(float dt)
    {
        time += dt;
        elapsedTime += dt;
    }

    public boolean canSpawn()
    {
        boolean result = time >= 1 / emissionRate;

        if(result)
        {
            time -= 1 / emissionRate;
        }

        return result;
    }

    public boolean isAlive()
    {
        //-1 means infinite life
        if(lifetime == -1)
        {
            return true;
        }

        return elapsedTime <= lifetime;
    }
}
