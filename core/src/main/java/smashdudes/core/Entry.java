package smashdudes.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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

    private float accumulatedTime = 0;

    Viewport viewport;
    OrthographicCamera camera;
    ShapeRenderer sh;

    Player player1 = new Player(new InputConfig(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S), Color.GOLD);
    Player player2 = new Player(new InputConfig(Input.Keys.J, Input.Keys.L, Input.Keys.I, Input.Keys.K), Color.CYAN);
    Terrain[] terrain = new Terrain[3];

    @Override
    public void create()
    {
        sh = new ShapeRenderer();
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        viewport = new ExtendViewport(camera.viewportWidth, camera.viewportHeight, camera);
        terrain[0] = new Terrain(-8, 1, 3, 0.5f);
        terrain[1] = new Terrain(5, 1, 3, 0.5f);
        terrain[2] = new Terrain(-20, -5, 40, 0.5f);
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
        float step = 1 / 60f;

        accumulatedTime += dt;
        while (accumulatedTime >= step)
        {
            player1.update(step);
            player2.update(step);

            //CollisionResolver.resolve(player2, player1);

            for (Terrain t : terrain)
            {
                CollisionResolver.resolve(player1, t);
                CollisionResolver.resolve(player2, t);
            }
            accumulatedTime -= step;
        }
        camera.position.x = (player1.position.x + player2.position.x) / 2;
        camera.position.y = (player1.position.y + player2.position.y) / 2;

        float dist = player1.position.dst(player2.position) / (WORLD_WIDTH / 2);
        camera.zoom = Math.max(dist, 1.2f);
        camera.update();

        ScreenUtils.clear(Color.BLACK);

        sh.setProjectionMatrix(camera.combined);
        sh.begin(ShapeRenderer.ShapeType.Filled);

        player1.draw(sh);
        player2.draw(sh);

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