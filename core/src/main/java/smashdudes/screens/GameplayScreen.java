package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.content.ContentRepo;
import smashdudes.content.DTO;
import smashdudes.content.LoadContent;
import smashdudes.core.PlayerHandle;
import smashdudes.core.input.GameInputHandler;
import smashdudes.core.input.IGameInputRetriever;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.graphics.AnimationFrame;
import smashdudes.graphics.RenderResources;
import smashdudes.util.CharacterSelectDescription;

public class GameplayScreen extends GameScreen
{
    private GameInputHandler inputHandler;
    private Engine ecsEngine;

    public GameplayScreen(Game game, CharacterSelectDescription desc)
    {
        super(game);
        this.inputHandler = desc.gameInput;
        ecsEngine = new Engine();
        
        for(CharacterSelectDescription.PlayerDescription p : desc.descriptions)
        {
            DTO.Character characterData = ContentRepo.loadCharacter(p.identifier);
            Entity player = buildPlayer(p.handle, p.identifier, characterData);

            IGameInputRetriever retriever = inputHandler.getGameInput(p.handle);

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

        for(int i = 0; i < 200; i++)
        {
            Entity emitter = ecsEngine.createEntity();

            ParticleEmitterComponent comp = new ParticleEmitterComponent();
            comp.emissionRate = 512;
            comp.emissionPoint = new Vector2();

            comp.startColor = Color.ORANGE.cpy();
            comp.endColor = Color.RED.cpy();
            comp.endColor.a = 0.0f;

            comp.lifespanStartRange = 0.1f;
            comp.lifespanEndRange = 0.6f;

            comp.sizeStartRange = new Vector2(0.1f, 0.2f);
            comp.sizeEndRange = new Vector2(0.0f, 0.2f);

            comp.velocityMin = new Vector2(-1.5f, 0.1f);
            comp.velocityMax = new Vector2(1.5f, 6.f);

            comp.zIndex = 5;

            emitter.addComponent(comp);
        }
    }

    @Override
    public void hide()
    {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(float dt)
    {
        float maxStep = 1/30f;
        if(dt > maxStep) dt = maxStep;

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

    private Entity buildPlayer(PlayerHandle handle, String identifier, DTO.Character characterData)
    {
        Entity player = ecsEngine.createEntity();

        player.addComponent(new PlayerComponent(handle, identifier));
        player.addComponent(new PositionComponent(new Vector2(0, 10)));
        player.addComponent(new JumpComponent(characterData.jumpStrength));
        player.addComponent(new GravityComponent(characterData.gravity));
        player.addComponent(new PlayerInAirComponent());

        VelocityComponent vc = new VelocityComponent();
        vc.runSpeed = characterData.runSpeed;
        vc.airSpeed = characterData.airSpeed;
        vc.deceleration = characterData.deceleration;
        player.addComponent(vc);

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);


        PlayerAnimationContainerComponent animContainer = new PlayerAnimationContainerComponent();
        animContainer.idle = loadPlayerAnimation(characterData, "idle", Animation.PlayMode.LOOP);
        animContainer.running = loadPlayerAnimation(characterData, "run", Animation.PlayMode.LOOP);
        animContainer.jumping = loadPlayerAnimation(characterData,"jump", Animation.PlayMode.LOOP);
        animContainer.falling = loadPlayerAnimation(characterData,"fall", Animation.PlayMode.LOOP);
        animContainer.attack_1 = loadPlayerAnimation(characterData,"attack_1", Animation.PlayMode.NORMAL);
        player.addComponent(animContainer);

        player.addComponent(animContainer.idle);

        DrawComponent sd = new DrawComponent();
        sd.scale =  characterData.scale;
        player.addComponent(sd);

        player.addComponent(new DebugDrawComponent());

        TerrainColliderComponent collider = new TerrainColliderComponent();
        collider.collider = characterData.terrainCollider;
        player.addComponent(collider);

        return player;
    }

    public AnimationComponent loadPlayerAnimation(DTO.Character characterData, String animationName, Animation.PlayMode mode)
    {
        DTO.Animation anim = null;
        for(DTO.Animation a : characterData.animations)
        {
            if(a.animationName.equals(animationName))
            {
                anim = a;
                break;
            }
        }

        //if we do not have an animation with the name passed, create an animation with the default texture
        Array<AnimationFrame> frames = new Array<>();
        if(anim == null)
        {
            frames.add(new AnimationFrame(RenderResources.getTexture("textures/default.png"), new Array<>(), new Array<>()));

            return new AnimationComponent(frames, 1, mode);
        }
        else
        {
            for (DTO.AnimationFrame dtoFrame : anim.frames)
            {
                AnimationFrame frame =
                        new AnimationFrame(RenderResources.getTexture(dtoFrame.texturePath), dtoFrame.attackboxes, dtoFrame.bodyboxes);
                frames.add(frame);
            }

            return new AnimationComponent(frames,  anim.animationDuration, mode);
        }
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
        terrain.addComponent(dd);


        return terrain;
    }
}
