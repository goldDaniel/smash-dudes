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
        player.addComponent(new PlayerIdleComponent());

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);


        PlayerAnimationContainerComponent animContainer = new PlayerAnimationContainerComponent();
        animContainer.idle = loadPlayerAnimation(characterData, "idle");
        animContainer.running = loadPlayerAnimation(characterData, "run");
        player.addComponent(animContainer);

        player.addComponent(animContainer.idle);

        AnimationDebugComponent ad = new AnimationDebugComponent();
        player.addComponent(ad);


        DrawComponent sd = new DrawComponent();
        sd.width = 2;
        sd.height = 2;
        player.addComponent(sd);

        DebugDrawComponent dd = new DebugDrawComponent();
        dd.width = 2;
        dd.height = 2;
        player.addComponent(dd);



        TerrainColliderComponent collider = new TerrainColliderComponent();
        collider.colliderWidth = characterData.terrainCollider.x;
        collider.colliderHeight = characterData.terrainCollider.y;
        player.addComponent(collider);

        return player;
    }

    public AnimationComponent loadPlayerAnimation(DTO.Character characterData, String animationName)
    {
        DTO.Animation anim = null;
        for(DTO.Animation a : characterData.animations)
        {
            if(a.animationName == animationName)
            {
                anim = a;
                break;
            }
        }

        Array<AnimationComponent.AnimationFrame> frames = new Array<>();
        for (DTO.AnimationFrame dtoFrame : anim.frames)
        {
            AnimationComponent.AnimationFrame frame =
                    new AnimationComponent.AnimationFrame(new Texture(dtoFrame.texturePath), dtoFrame.hitboxes, dtoFrame.hurtboxes);
            frames.add(frame);
        }

        float duration = 0;
        if(animationName == "idle") duration = 1/8f;
        else                        duration = 1/16f;

        return new AnimationComponent(frames, duration);
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
