package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import smashdudes.content.ContentRepo;
import smashdudes.content.DTO;
import smashdudes.content.LoadContent;
import smashdudes.core.input.GameInputHandler;
import smashdudes.core.input.IGameInputRetriever;
import smashdudes.core.PlayerHandle;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.graphics.AnimationFrame;
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

            DTO.Character characterData = null;
            if(p.identifier.equals("a"))
            {
                characterData = ContentRepo.loadCharacter("Character.json");
            }
            if(p.identifier.equals("b"))
            {
                characterData = ContentRepo.loadCharacter("Knight2.json");
            }
            if(p.identifier.equals("c"))
            {
                characterData = ContentRepo.loadCharacter("Daniel.json");
            }

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
        player.addComponent(new VelocityComponent());
        player.addComponent(new JumpComponent(characterData.jumpStrength));
        player.addComponent(new GravityComponent(characterData.gravity));
        player.addComponent(new PlayerInAirComponent());

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);


        PlayerAnimationContainerComponent animContainer = new PlayerAnimationContainerComponent();
        animContainer.idle = loadPlayerAnimation(characterData, "idle", Animation.PlayMode.LOOP);
        animContainer.running = loadPlayerAnimation(characterData, "run", Animation.PlayMode.LOOP);
        animContainer.jumping = loadPlayerAnimation(characterData,"jump", Animation.PlayMode.LOOP);
        animContainer.falling = loadPlayerAnimation(characterData,"fall", Animation.PlayMode.LOOP);
        //animContainer.attack_1 = loadPlayerAnimation(characterData,"attack_1", Animation.PlayMode.NORMAL);
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

        Array<AnimationFrame> frames = new Array<>();
        for (DTO.AnimationFrame dtoFrame : anim.frames)
        {
            AnimationFrame frame =
                    new AnimationFrame(new Texture(Gdx.files.internal(dtoFrame.texturePath), true), dtoFrame.hitboxes, dtoFrame.hurtboxes);
            frames.add(frame);
        }

        float duration = anim.animationDuration;

        return new AnimationComponent(frames, duration, mode);
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
