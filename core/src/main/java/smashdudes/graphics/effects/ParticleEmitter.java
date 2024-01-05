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

        while(checkSetSpawnTimer())
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

    public boolean checkSetSpawnTimer()
    {
        boolean result = false;

        if(spawnTimer >= (1.0f / config.emissionRate) && elapsedTime <= config.emissionDuration)
        {
            spawnTimer -= 1.0f / config.emissionRate;
            result = true;
        }

        return result;
    }

    public void render(SpriteBatch sb)
    {
        if(!enabled) return;

        for(Particle p : activeParticles)
        {
            renderParticle(p, sb);
        }
    }

    // TODO nathan: put this somewhere else
    private void renderParticle(Particle p, SpriteBatch sb)
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
        if(MathUtils.isEqual(config.emissionDuration, ParticleEmitterConfig.ENDLESS))
        {
            return false;
        }

        return elapsedTime > config.emissionDuration && activeParticles.size == 0;
    }

    public void reset()
    {
        elapsedTime = 0;
        spawnTimer = 0;

        particlePool.freeAll(activeParticles);
        particlePool.freeAll(deadParticles);

        activeParticles.clear();
        deadParticles.clear();
    }
}
