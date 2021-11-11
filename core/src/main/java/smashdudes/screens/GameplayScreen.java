package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
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
            Entity player = buildPlayer(p, characterData);

            GameInputRetriever retriever = inputHandler.getGameInput(p);

            PlayerControllerComponent pc = new PlayerControllerComponent(retriever);
            player.addComponent(pc);
        }

        Array<DTO.Terrain> terrainData = LoadContent.loadTerrainData("Terrain.json");
        for (DTO.Terrain data : terrainData)
        {
            buildTerrain(data);
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

    private Entity buildPlayer(PlayerHandle handle, DTO.Character characterData)
    {
        Entity player = ecsEngine.createEntity();

        player.addComponent(new PlayerComponent(handle));
        player.addComponent(new PositionComponent());
        player.addComponent(new VelocityComponent());
        player.addComponent(new JumpComponent(characterData.jumpStrength));
        player.addComponent(new GravityComponent(characterData.gravity));

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);

        DTO.Animation animation = characterData.animations.get(1);

        Array<AnimationComponent.AnimationFrame> frames = new Array<>();
        Array<Array<Rectangle>> hitboxes = new Array<>();
        Array<Array<Rectangle>> hurtboxes = new Array<>();
        for (DTO.AnimationFrame frame : animation.frames)
        {
            frames.add(new AnimationComponent.AnimationFrame(new Texture(frame.texturePath)));
            hitboxes.add(frame.hitboxes);
            hurtboxes.add(frame.hurtboxes);
        }

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

        AnimationDebugComponent ad = new AnimationDebugComponent(hitboxes, hurtboxes);
        player.addComponent(ad);

        TerrainColliderComponent collider = new TerrainColliderComponent();
        collider.colliderWidth = characterData.terrainCollider.x;
        collider.colliderHeight = characterData.terrainCollider.y;
        player.addComponent(collider);

        return player;
    }

    public Entity buildTerrain(DTO.Terrain terrainData)
    {
        Entity terrain = ecsEngine.createEntity();

        PositionComponent tp = new PositionComponent();
        tp.position.set(terrainData.position);
        terrain.addComponent(tp);

        StaticTerrainComponent t = new StaticTerrainComponent();
        t.width = terrainData.width;
        t.height = terrainData.height;
        terrain.addComponent(t);

        DebugDrawComponent dd = new DebugDrawComponent();
        dd.width = terrainData.width;
        dd.height = terrainData.height;
        dd.color = Color.GREEN;
        terrain.addComponent(dd);

        DrawComponent d = new DrawComponent();
        d.texture = RenderResources.getTexture(terrainData.textureFilePath);
        d.width = terrainData.width;
        d.height = terrainData.height;
        terrain.addComponent(d);

        return terrain;
    }
}
