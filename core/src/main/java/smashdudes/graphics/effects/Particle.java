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
}
