package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import smashdudes.graphics.RenderResources;

public class ParticleEmitter
{
    private float spawnTimer;
    private final ParticleEmitterConfig config;
    private final Pool<Particle> particlePool;
    private final Array<Particle> activeParticles;
    private final Array<Particle> deadParticles;

    public boolean enabled = true;

    public ParticleEmitter(ParticleEmitterConfig config, Pool<Particle> particlePool)
    {
        this.config = config;
        this.particlePool = particlePool;
        this.activeParticles = new Array<>();
        this.deadParticles = new Array<>();

        spawnTimer = 0;
    }

    public void release()
    {
        particlePool.freeAll(activeParticles);
    }

    public void update(float dt)
    {
        if(!enabled) return;

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

    public void render(SpriteBatch sb)
    {
        if(!enabled) return;

        for(Particle p : activeParticles)
        {
            drawParticle(sb, p);
        }
    }

    public boolean canSpawnParticle()
    {
        boolean result = spawnTimer >= (1.0 / config.emissionRate);

        if(result)
        {
            spawnTimer -= (1.0f / config.emissionRate);
        }

        return result;
    }

    private static void updateParticle(Particle p, float dt)
    {
        p.x += p.vX * dt;
        p.y += p.vY * dt;
        p.life -= dt;
    }

    private static void drawParticle(SpriteBatch sb, Particle p)
    {
        float t = 1.0f  - p.life / p.initialLife;

        float scale = MathUtils.lerp(p.scaleStart, p.scaleEnd, t);

        float r = MathUtils.lerp(p.rStart, p.rEnd, t);
        float g = MathUtils.lerp(p.gStart, p.gEnd, t);
        float b = MathUtils.lerp(p.bStart, p.bEnd, t);
        float a = MathUtils.lerp(p.aStart, p.aEnd, t);

        sb.setColor(r, g, b, a);
        sb.draw(RenderResources.getTexture("textures/particleTexture.png"), p.x - scale / 2, p.y - scale / 2, scale, scale);
    }
}
