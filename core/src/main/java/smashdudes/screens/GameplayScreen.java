package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import smashdudes.core.InputConfig;
import smashdudes.core.PlayerHandle;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class GameplayScreen extends GameScreen
{
    private Engine ecsEngine;

    public GameplayScreen(Game game)
    {
        super(game);
        ecsEngine = new Engine();

        buildPlayer(new InputConfig(Input.Keys.A,Input.Keys.D,Input.Keys.W,Input.Keys.S), Color.GOLD);

        Entity ai = buildPlayer(new InputConfig(Input.Keys.J,Input.Keys.L,Input.Keys.I,Input.Keys.K), Color.RED);
        ai.removeComponent(PlayerControllerComponent.class);
        ai.addComponent(new AIControllerComponent());


        buildTerrain(0, -5, 30, 0.75f);
        buildTerrain(6, 2.5f, 5, 0.1f);
        buildTerrain(-6, 2.5f, 5, 0.1f);

        ecsEngine.update(0);
    }

    @Override
    public void update(float dt)
    {
        ScreenUtils.clear(Color.BLACK);
        ecsEngine.update(dt);
    }

    @Override
    public void render()
    {

    }

    @Override
    public void resize(int width, int height)
    {
        ecsEngine.resize(width, height);
    }

    private Entity buildPlayer(InputConfig config, Color color)
    {
        Entity player = ecsEngine.createEntity();

        player.addComponent(new PlayerComponent(new PlayerHandle()));
        player.addComponent(new PositionComponent());
        player.addComponent(new VelocityComponent());
        player.addComponent(new JumpComponent(30));
        player.addComponent(new GravityComponent(60));

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);

        PlayerControllerComponent pc = new PlayerControllerComponent(config);
        player.addComponent(pc);

        DebugDrawComponent d = new DebugDrawComponent();
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

        DebugDrawComponent td = new DebugDrawComponent();
        td.width = w;
        td.height = h;
        td.color = Color.GREEN;

        terrain.addComponent(td);

        return terrain;
    }
}
