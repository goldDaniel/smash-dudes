package smashdudes.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.components.InputConfigComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.components.VelocityComponent;

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

        InputConfigComponent i = new InputConfigComponent();
        i.config = new InputConfig(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S);
        player.addComponent(i);

        DrawComponent d = new DrawComponent();
        d.color = Color.GOLD;
        d.width = 2;
        d.height = 2;
        player.addComponent(d);
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