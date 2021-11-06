package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
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

        buildTerrain(0, -5, 30, 0.75f);
        buildTerrain(6, 2.5f, 5, 0.1f);
        buildTerrain(-6, 2.5f, 5, 0.1f);
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

        Array<AnimationComponent.AnimationFrame> frames = new Array<>();
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_1.png")));
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_2.png")));
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_3.png")));
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_4.png")));

        AnimationComponent anim = new AnimationComponent(frames);
        player.addComponent(anim);

        SpriteDrawComponent sd = new SpriteDrawComponent();
        sd.width = 2;
        sd.height = 2;
        player.addComponent(sd);

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
