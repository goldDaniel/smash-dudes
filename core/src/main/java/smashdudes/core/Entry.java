package smashdudes.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Entry implements ApplicationListener
{

    Engine ecsEngine;

    private void buildPlayer()
    {
        Entity player = ecsEngine.createEntity();

        player.addComponent(new PositionComponent());
        player.addComponent(new VelocityComponent());
        player.addComponent(new JumpComponent(20));
        player.addComponent(new GravityComponent(25));

        InputConfigComponent i = new InputConfigComponent();
        i.config = new InputConfig(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S);
        player.addComponent(i);

        DrawComponent d = new DrawComponent();
        d.color = Color.GOLD;
        d.width = 2;
        d.height = 2;
        player.addComponent(d);

        TerrainColliderComponent collider = new TerrainColliderComponent();
        collider.colliderWidth = 2;
        collider.colliderHeight = 2;
        player.addComponent(collider);
    }

    public void buildTerrain(float x, float y, float w, float h)
    {
        Entity terrain = ecsEngine.createEntity();

        PositionComponent tp = new PositionComponent();
        tp.position.x = x;
        tp.position.y = y;
        terrain.addComponent(tp);

        StaticTerrainComponent t = new StaticTerrainComponent();
        t.width = w;
        t.height = h;
        terrain.addComponent(t);

        DrawComponent td = new DrawComponent();
        td.width = w;
        td.height = h;
        td.color = Color.GREEN;

        terrain.addComponent(td);
    }

    @Override
    public void create()
    {
        ecsEngine = new Engine();

        buildPlayer();

        buildTerrain(0, -5, 30, 0.75f);
        buildTerrain(6, 2.5f, 5, 0.5f);
        buildTerrain(-6, 2.5f, 5, 0.5f);
    }

    @Override
    public void resize(int width, int height)
    {
        ecsEngine.resize(width, height);
    }

    @Override
    public void render()
    {
        ecsEngine.update();
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}