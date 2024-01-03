package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import smashdudes.graphics.RenderResources;

import java.util.stream.StreamSupport;

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
            Particle p ;
            //NOTE (danielg): pool may be shared among threads, so we must sync
            synchronized (particlePool)
            {
                p = particlePool.obtain();
            }

            ParticleEmitterConfig.configureParticle(p, config);
            activeParticles.add(p);
        }
        spawnTimer += dt;

        for(Particle p : activeParticles)
        {
            updateParticle(config, p, dt);
            if(p.life <= 0)
            {
                deadParticles.add(p);
            }
        }

        //NOTE (danielg): pool may be shared among threads, so we must sync
        synchronized (particlePool)
        {
            particlePool.freeAll(deadParticles);
        }
        activeParticles.removeAll(deadParticles, true);
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

    private boolean canSpawnParticle()
    {
        boolean result = spawnTimer >= (1.0 / config.emissionRate);

        if(result)
        {
            spawnTimer -= (1.0f / config.emissionRate);
        }

        return result;
    }

    private static void updateParticle(ParticleEmitterConfig config, Particle p, float dt)
    {
        p.vX += config.gravity.x * dt;
        p.vY += config.gravity.y * dt;

        float dirX = p.x - p.spawnX;
        float dirY = p.y - p.spawnY;
        float mag = Vector2.len(dirX, dirY);
        dirX /= mag;
        dirY /= mag;

        p.x += dirX * p.radialAcceleration * dt;
        p.y += dirY * p.radialAcceleration * dt;

        float tanX = dirY;
        float tanY = -dirX;

        p.x += tanX * p.tangentialAcceleration * dt;
        p.y += tanY * p.tangentialAcceleration * dt;

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
