package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import smashdudes.content.LoadContent;
import smashdudes.core.input.GameInputHandler;
import smashdudes.core.input.GameInputRetriever;
import smashdudes.core.input.InputConfig;
import smashdudes.core.PlayerHandle;
import smashdudes.core.input.KeyboardInputListener;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class GameplayScreen extends GameScreen
{
    private GameInputHandler inputHandler;
    private Engine ecsEngine;

    public GameplayScreen(Game game, Iterable<PlayerHandle> players, GameInputHandler inputHandler)
    {
        super(game);
        this.inputHandler = inputHandler;
        ecsEngine = new Engine();


        for(PlayerHandle p : players)
        {
            Entity player = buildPlayer(p, Color.GOLD);

            GameInputRetriever retriever = inputHandler.getGameInput(p);

            PlayerControllerComponent pc = new PlayerControllerComponent(retriever);
            player.addComponent(pc);
        }

        Array<LoadContent.TerrainDTO> terrainData = LoadContent.loadTerrainData("build.json");
        for (LoadContent.TerrainDTO data : terrainData)
        {
            buildTerrain(data.position, data.width, data.height);
        }
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(inputHandler.getInputProcessor());
    }

    @Override
    public void hide()
    {
        Gdx.input.setInputProcessor(null);
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

    private Entity buildPlayer(PlayerHandle handle, Color color)
    {
        Entity player = ecsEngine.createEntity();

        player.addComponent(new PlayerComponent(handle));
        player.addComponent(new PositionComponent());
        player.addComponent(new VelocityComponent());
        player.addComponent(new JumpComponent(30));
        player.addComponent(new GravityComponent(60));

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);


        DebugDrawComponent d = new DebugDrawComponent();
        d.width = 2;
        d.height = 2;
        player.addComponent(d);

        TerrainColliderComponent collider = new TerrainColliderComponent();
        collider.colliderWidth = 2;
        collider.colliderHeight = 2;
        player.addComponent(collider);

        return player;
    }

    public Entity buildTerrain(Vector2 position, float w, float h)
    {
        Entity terrain = ecsEngine.createEntity();

        PositionComponent tp = new PositionComponent();
        tp.position.set(position);
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
