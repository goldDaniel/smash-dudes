package smashdudes.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Entry implements ApplicationListener
{
    public static float WORLD_WIDTH = 20;
    public static float WORLD_HEIGHT = 12;

    Viewport viewport;
    OrthographicCamera camera;
    ShapeRenderer sh;

    Player player = new Player();
    Terrain[] terrain = new Terrain[3];

    @Override
    public void create()
    {
        sh = new ShapeRenderer();
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);
        terrain[0] = new Terrain(-8, 1, 3, 0.5f);
        terrain[1] = new Terrain(5, 1, 3, 0.5f);
        terrain[2] = new Terrain(-10, -5, 20, 0.5f);
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.apply();
    }

    @Override
    public void render()
    {
        float dt = Gdx.graphics.getDeltaTime();

        player.update(dt);

        for (Terrain t : terrain)
        {
            Rectangle rect = player.getCollisionRect();
            t.isTouching(rect);
        }

        float cameraSpeed = 4f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            camera.translate(-dt * cameraSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            camera.translate(dt * cameraSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            camera.translate(0, dt * cameraSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            camera.translate(0, -dt * cameraSpeed);
        }
        camera.update();


        ScreenUtils.clear(Color.BLACK);

        sh.setProjectionMatrix(camera.combined);
        sh.begin(ShapeRenderer.ShapeType.Filled);

        player.draw(sh);

        for (Terrain t : terrain)
        {
            t.draw(sh);
        }

        sh.end();
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