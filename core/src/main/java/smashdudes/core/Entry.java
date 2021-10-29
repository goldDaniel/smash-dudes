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

    private Entity buildPlayer(InputConfig config, Color color)
    {
        Entity player = ecsEngine.createEntity();

        player.addComponent(new PlayerComponent());
        player.addComponent(new PositionComponent());
        player.addComponent(new VelocityComponent());
        player.addComponent(new JumpComponent(20, 12, 2));
        player.addComponent(new GravityComponent(25));

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);

        PlayerControllerComponent pc = new PlayerControllerComponent(config);
        player.addComponent(pc);

        DrawComponent d = new DrawComponent();
        d.color = color;
        d.width = 2;
        d.height = 2;
        player.addComponent(d);

        TerrainColliderComponent collider = new TerrainColliderComponent();
        collider.colliderWidth = 2;
        collider.colliderHeight = 2;
        player.addComponent(collider);

        return player;
    }

    public Entity buildTerrain(float x, float y, float w, float h)
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

        return terrain;
    }

    @Override
    public void create()
    {
        ecsEngine = new Engine();

        buildPlayer(new InputConfig(Input.Keys.A,Input.Keys.D,Input.Keys.W,Input.Keys.S), Color.GOLD);

//        Entity ai = buildPlayer(new InputConfig(Input.Keys.J,Input.Keys.L,Input.Keys.I,Input.Keys.K), Color.RED);
//        ai.removeComponent(PlayerControllerComponent.class);
//        ai.addComponent(new AIControllerComponent());


        buildTerrain(0, -5, 30, 0.75f);
        buildTerrain(6, 2.5f, 5, 0.1f);
        buildTerrain(-6, 2.5f, 5, 0.1f);
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