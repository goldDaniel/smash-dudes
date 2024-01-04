package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import smashdudes.graphics.RenderResources;

public class ParticleEmitter
{
    private float elapsedTime = 0;
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
        elapsedTime = config.emissionDuration + 1;
    }

    public void update(float dt)
    {
        if(!enabled || depleted()) return;

        elapsedTime += dt;
        spawnTimer += dt;

        while(spawnTimer >= (1.0 / config.emissionRate) && elapsedTime <= config.emissionDuration)
        {
            Particle p ;
            //NOTE (danielg): pool may be shared among threads, so we must sync
            synchronized (particlePool)
            {
                p = particlePool.obtain();
            }

            ParticleEmitterConfig.configureParticle(p, config);
            activeParticles.add(p);
            spawnTimer -= (1.0f / config.emissionRate);
        }

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
            p.render(sb);
        }
    }

    private static void updateParticle(ParticleEmitterConfig config, Particle p, float dt)
    {
        p.vX += config.gravity.x * dt;
        p.vY += config.gravity.y * dt;



        float dirX = p.x - p.spawnX;
        float dirY = p.y - p.spawnY;
        float mag = Vector2.len(dirX, dirY);
        if(!Float.isNaN(mag) && !MathUtils.isZero(mag))
        {
            dirX /= mag;
            dirY /= mag;
        }

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

    public boolean depleted()
    {
        return elapsedTime > config.emissionDuration && activeParticles.size == 0;
    }

    public void reset()
    {
        elapsedTime = 0;
        spawnTimer = 0;

        synchronized (particlePool)
        {
            particlePool.freeAll(activeParticles);
            particlePool.freeAll(deadParticles);
        }

        activeParticles.clear();
        deadParticles.clear();
    }
}
