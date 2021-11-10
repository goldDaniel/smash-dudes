package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import smashdudes.content.DTO;
import smashdudes.content.LoadContent;
import smashdudes.core.RenderResources;
import smashdudes.core.input.GameInputHandler;
import smashdudes.core.input.GameInputRetriever;
import smashdudes.core.PlayerHandle;
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

        DTO.Character characterData = LoadContent.loadCharacterData("Character.json");
        for(PlayerHandle p : players)
        {
            Entity player = buildPlayer(p, Color.GOLD, characterData.jumpStrength, characterData.gravity);

            GameInputRetriever retriever = inputHandler.getGameInput(p);

            PlayerControllerComponent pc = new PlayerControllerComponent(retriever);
            player.addComponent(pc);
        }

        Array<DTO.Terrain> terrainData = LoadContent.loadTerrainData("Terrain.json");
        for (DTO.Terrain data : terrainData)
        {
            buildTerrain(data.position, data.width, data.height, data.textureFilePath);
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
        ScreenUtils.clear(Color.GRAY);

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

    private Entity buildPlayer(PlayerHandle handle, Color color, float jumpStrength, float gravity)
    {
        Entity player = ecsEngine.createEntity();

        player.addComponent(new PlayerComponent(handle));
        player.addComponent(new PositionComponent());
        player.addComponent(new VelocityComponent());
        player.addComponent(new JumpComponent(jumpStrength));
        player.addComponent(new GravityComponent(gravity));

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);

        Array<AnimationComponent.AnimationFrame> frames = new Array<>();
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_1.png")));
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_2.png")));
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_3.png")));
        frames.add(new AnimationComponent.AnimationFrame(new Texture("idle/knight_idle_4.png")));

        AnimationComponent anim = new AnimationComponent(frames);
        player.addComponent(anim);

        DrawComponent sd = new DrawComponent();
        sd.width = 2;
        sd.height = 2;
        player.addComponent(sd);

        DebugDrawComponent dd = new DebugDrawComponent();
        dd.width = 2;
        dd.height = 2;
        player.addComponent(dd);

        TerrainColliderComponent collider = new TerrainColliderComponent();
        collider.colliderWidth = 2;
        collider.colliderHeight = 2;
        player.addComponent(collider);

        return player;
    }

    public Entity buildTerrain(Vector2 position, float w, float h, String textureFilePath)
    {
        Entity terrain = ecsEngine.createEntity();

        PositionComponent tp = new PositionComponent();
        tp.position.set(position);
        terrain.addComponent(tp);

        StaticTerrainComponent t = new StaticTerrainComponent();
        t.width = w;
        t.height = h;
        terrain.addComponent(t);

        DebugDrawComponent dd = new DebugDrawComponent();
        dd.width = w;
        dd.height = h;
        dd.color = Color.GREEN;
        terrain.addComponent(dd);

        DrawComponent d = new DrawComponent();
        d.texture = RenderResources.getTexture(textureFilePath);
        d.width = w;
        d.height = h;
        terrain.addComponent(d);

        return terrain;
    }
}
