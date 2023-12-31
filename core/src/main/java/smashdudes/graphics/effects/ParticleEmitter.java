package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ParticleEmitter
{
    private float spawnTimer;
    private final ParticleEmitterConfig config;
    private final Pool<Particle> particlePool;
    private final Array<Particle> activeParticles;
    private final Array<Particle> deadParticles;

    public ParticleEmitter(ParticleEmitterConfig config, Pool<Particle> particlePool)
    {
        this.config = config;
        this.particlePool = particlePool;
        this.activeParticles = new Array<>();
        this.deadParticles = new Array<>();

        spawnTimer = 1.0f / config.emissionRate;
    }

    public void update(float dt)
    {
        while(canSpawnParticle())
        {
            Particle p = ParticleEmitterConfig.configureParticle(particlePool.obtain(), config);
            activeParticles.add(p);
        }
        spawnTimer += dt;

        for(Particle p : activeParticles)
        {
            updateParticle(p, dt);
            if(p.life <= 0)
            {
                deadParticles.add(p);
            }
        }

        activeParticles.removeAll(deadParticles, true);
        particlePool.freeAll(deadParticles);
        deadParticles.clear();
    }

    public void render(ShapeRenderer sh)
    {
        for(Particle p : activeParticles)
        {
            sh.setColor(p.r, p.g, p.b, p.a);
            sh.circle(p.x, p.y, p.scale, 16);
        }
    }

    public boolean canSpawnParticle()
    {
        boolean result = spawnTimer >= 1.0 / config.emissionRate;

        if(result)
        {
            spawnTimer -= 1.0f / config.emissionRate;
        }

        return result;
    }

    public void updateParticle(Particle p, float dt)
    {
        float t = 1.0f  - p.life / p.initialLife;

        p.x += p.vX * dt;
        p.y += p.vY * dt;

        p.scale = MathUtils.lerp(p.scaleStart, p.scaleEnd, t);

        p.r = MathUtils.lerp(p.rStart, p.rEnd, t);
        p.g = MathUtils.lerp(p.gStart, p.gEnd, t);
        p.b = MathUtils.lerp(p.bStart, p.bEnd, t);
        p.a = MathUtils.lerp(p.aStart, p.aEnd, t);

        p.life -= dt;
    }
}
