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

    @Override
    public void create()
    {
        ecsEngine = new Engine();

        Entity player = ecsEngine.createEntity();

        player.addComponent(new PositionComponent());
        player.addComponent(new VelocityComponent());
        player.addComponent(new GravityComponent(2));

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


        Entity terrain = ecsEngine.createEntity();
        float terrainWidth = 20f;
        float terrainHeight = 0.75f;

        PositionComponent tp = new PositionComponent();
        tp.position.y = -5;
        terrain.addComponent(tp);

        StaticTerrainComponent t = new StaticTerrainComponent();
        t.width = terrainWidth;
        t.height = terrainHeight;
        terrain.addComponent(t);

        DrawComponent td = new DrawComponent();
        td.width = terrainWidth;
        td.height = terrainHeight;
        td.color = Color.GREEN;

        terrain.addComponent(td);
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