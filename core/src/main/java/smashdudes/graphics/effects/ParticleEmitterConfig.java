package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ParticleEmitterConfig
{
    public static final boolean ENDLESS = false;

    public String name = "";

    public Vector2 origin = new Vector2();

    // Emission
    public float emissionRate = 100;
    public float emissionDuration = 1;
    public ParticleEmitterShape spawnShape = ParticleEmitterShape.Point;
    public float spawnShapeScale = 1.0f;

    // Life
    public float initialLife = 1;
    public float initialLifeVar = 0;

    // Velocity
    public Vector2 velocity = new Vector2(0,0);
    public Vector2 velocityVar = new Vector2(5,5);

    // Acceleration
    public Vector2 gravity = new Vector2();
    public float radialAcceleration = 0;
    public float radialAccelerationVar = 0;
    public float tangentialAcceleration = 0;
    public float tangentialAccelerationVar = 0;

    // Color
    public Color startColor = new Color(1,1,1,1);
    public Color startColorVar = new Color(0,0,0,0);

    public Color endColor = new Color(0,0,0,0);
    public Color endColorVar = new Color(0,0,0,0);

    // Scale
    public float scaleStart = 1;
    public float scaleStartVar = 0;

    // scale
    public float scaleEnd = 0;
    public float scaleEndVar = 0;

    // effect
    public Vector2 effectPosition = new Vector2();

    public static void configureParticle(Particle result, ParticleEmitterConfig config)
    {
        result.initialLife = config.initialLife + config.initialLifeVar * range();
        result.life = result.initialLife;

        setSpawnPosition(result, config.spawnShape, config.effectPosition, config.origin, config.spawnShapeScale);

        result.vX = config.velocity.x + config.velocityVar.x * range();
        result.vY = config.velocity.y + config.velocityVar.y * range();

        result.radialAcceleration = config.radialAcceleration + config.radialAccelerationVar * range();
        result.tangentialAcceleration = config.tangentialAcceleration + config.tangentialAccelerationVar * range();

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
    }

    public static void setSpawnPosition(Particle result, ParticleEmitterShape shape, Vector2 effectOrigin, Vector2 emitterOrigin, float emissionShapeScale)
    {

        switch (shape)
        {
            case Point:
                {
                    result.x = 0;
                    result.y = 0;
                }
                break;
            case Circle:
                {
                    float angle = MathUtils.random(0f, MathUtils.PI2);
                    float radius = (float)Math.sqrt(MathUtils.random());

                    result.x = radius * MathUtils.cos(angle);
                    result.y = radius * MathUtils.sin(angle);
                }
                break;
            case Ring:
                {
                    float angle = MathUtils.random(0f, MathUtils.PI2);
                    result.x = MathUtils.cos(angle);
                    result.y = MathUtils.sin(angle);
                }
                break;
            case Square:
                {
                    int side = MathUtils.random(0, 3);
                    if(side == 0)
                    {
                        result.x = -1;
                        result.y = range();
                    }
                    if(side == 1)
                    {
                        result.x = 1;
                        result.y = range();
                    }
                    if(side == 2)
                    {
                        result.x = range();
                        result.y = -1;
                    }
                    if(side == 3)
                    {
                        result.x = range();
                        result.y = 1;
                    }
                }
                break;
            case Box:
                result.x = range();
                result.y = range();
        }

        result.x *= emissionShapeScale;
        result.y *= emissionShapeScale;

        result.x += effectOrigin.x + emitterOrigin.x;
        result.y += effectOrigin.y + emitterOrigin.y;
        result.spawnX = effectOrigin.x + emitterOrigin.x;
        result.spawnY = effectOrigin.y + emitterOrigin.y;
    }

    public static float range()
    {
        return MathUtils.random(-1f, 1f);
    }
}
