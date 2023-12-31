package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ParticleEmitterConfig
{
    // Emission
    public float emissionRate;

    // Life
    public float initialLife;
    public float initialLifeVar;

    // Velocity
    public Vector2 velocity = new Vector2();
    public Vector2 velocityVar = new Vector2();

    // Color
    public Color startColor = new Color();
    public Color startColorVar = new Color();

    public Color endColor = new Color();
    public Color endColorVar = new Color();

    // Scale
    public float scaleStart;
    public float scaleStartVar;

    public float scaleEnd;
    public float scaleEndVar;


    public static Particle configureParticle(Particle result, ParticleEmitterConfig config)
    {
        result.initialLife = config.initialLife + config.initialLifeVar * range();
        result.life = result.initialLife;

        result.x = 0;
        result.y = 0;

        result.vX = config.velocity.x + config.velocityVar.x * range();
        result.vY = config.velocity.y + config.velocityVar.y * range();

        result.scaleStart = config.scaleStart + config.scaleStartVar * range();
        result.scaleEnd   = config.scaleEnd + config.scaleEndVar * range();

        result.rStart = config.startColor.r + config.startColorVar.r * range();
        result.gStart = config.startColor.g + config.startColorVar.g * range();
        result.bStart = config.startColor.b + config.startColorVar.b * range();
        result.aStart = config.startColor.a + config.startColorVar.a * range();

        result.rEnd = config.endColor.r + config.endColorVar.r * range();
        result.gEnd = config.endColor.g + config.endColorVar.g * range();
        result.bEnd = config.endColor.b + config.endColorVar.b * range();
        result.aEnd = config.endColor.a + config.endColorVar.a * range();

        return result;
    }

    public static float range()
    {
        return MathUtils.random(-1f, 1f);
    }
}
