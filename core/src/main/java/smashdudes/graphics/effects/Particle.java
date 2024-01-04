package smashdudes.graphics.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import smashdudes.graphics.RenderResources;

public class Particle
{

    // life ////////////////////////////
    public float initialLife;
    public float life;

    // position //////////////////////
    public float x;
    public float y;

    public float spawnX;
    public float spawnY;

    // velocity //////////////////////
    public float vX;
    public float vY;

    // acceleration /////////////////
    public float radialAcceleration;
    public float tangentialAcceleration;


    // scale ///////////////////////
    public float scaleStart;
    public float scaleEnd;

    // color //////////////////////

    public float rStart;
    public float gStart;
    public float bStart;
    public float aStart;

    public float rEnd;
    public float gEnd;
    public float bEnd;
    public float aEnd;

    public void render(SpriteBatch sb)
    {
        float t = 1.0f  - life / initialLife;

        float scale = MathUtils.lerp(scaleStart, scaleEnd, t);

        float r = MathUtils.lerp(rStart, rEnd, t);
        float g = MathUtils.lerp(gStart, gEnd, t);
        float b = MathUtils.lerp(bStart, bEnd, t);
        float a = MathUtils.lerp(aStart, aEnd, t);

        sb.setColor(r, g, b, a);
        sb.draw(RenderResources.getTexture("textures/particleTexture.png"), x - scale / 2, y - scale / 2, scale, scale);
    }
}
