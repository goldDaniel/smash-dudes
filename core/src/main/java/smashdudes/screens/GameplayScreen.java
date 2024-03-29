package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import org.libsdl.SDL;
import smashdudes.content.ContentLoader;
import smashdudes.content.DTO;
import smashdudes.core.PlayerHandle;
import smashdudes.core.PlayerLobbyInfo;
import smashdudes.core.WorldUtils;
import smashdudes.core.input.MenuNavigator;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.gameplay.BodyBox;
import smashdudes.gameplay.state.State;
import smashdudes.gameplay.state.playerstate.air.*;
import smashdudes.gameplay.state.playerstate.ground.*;
import smashdudes.graphics.AnimationFrame;
import smashdudes.graphics.RenderResources;
import smashdudes.util.CharacterData;

public class GameplayScreen extends GameScreen
{
    private final int WORLD_WIDTH = 20;
    private final int WORLD_HEIGHT = 12;

    private Engine ecsEngine;

    private float accumulator = 0;

    private float delta = 0;
    private float alpha = 0;

    public GameplayScreen(Game game, Array<PlayerLobbyInfo> players, Array<CharacterData> characterData)
    {
        super(game);
        //this.inputHandler = desc.gameInput;
        ecsEngine = new Engine(WORLD_WIDTH, WORLD_HEIGHT, () -> transitionTo(MainMenuScreen.class));

        DTO.Stage stage = ContentLoader.loadStage("stage.json");

        WorldUtils.setStage(stage);
        for (DTO.Terrain terrain : stage.terrain)
        {
            buildTerrain(terrain);
        }

        for (PlayerLobbyInfo p : players)
        {
            CharacterData loadedData = characterData.get(p.selectedCharacterIndex);
            DTO.Character character = new Json().fromJson(DTO.Character.class, loadedData.jsonData);
            Entity player = buildPlayer(loadedData.texture, p.handle, p.color, character, stage.spawnPoints);

            PlayerControllerComponent pc = new PlayerControllerComponent(p.input);
            player.addComponent(pc);

            addInputProcessor(p.input);
        }

        buildParallaxBackground();
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void hide()
    {
        super.hide();
    }

    @Override
    public void buildUI(Table table, Skin skin, MenuNavigator menuNavigator)
    {

    }

    @Override
    public void update(float dt)
    {
        delta = dt;
        for (Controller c : Controllers.getControllers())
        {
            if (c.getButton(SDL.SDL_CONTROLLER_BUTTON_START))
            {
                game.setScreen(new PauseScreen(game, this));
                return;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            game.setScreen(new PauseScreen(game, this));
        }
        float stepSize = 1 / 60f;

        accumulator += dt;
        while (accumulator >= stepSize)
        {
            ecsEngine.update(stepSize);

            accumulator -= stepSize;
        }

        alpha = accumulator / stepSize;
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(Color.SKY);
        ecsEngine.render(delta, alpha);
    }

    @Override
    public void resize(int width, int height)
    {
        ecsEngine.resize(width, height);
    }

    private void buildParallaxBackground()
    {
        for(int i = 0; i < 10; i++)
        {

            for(int adjustment = -1; adjustment <= 1; adjustment++)
            {
                PositionComponent p = new PositionComponent();
                BackgroundComponent background = new BackgroundComponent();
                background.offset.y = 6;

                background.parallax.x = -0.05f * i;

                DrawComponent draw = new DrawComponent();
                draw.scale.x = 30;
                background.offset.x = draw.scale.x * adjustment;

                draw.texture = RenderResources.getTexture("textures/background/layer" + i + ".png");
                draw.zIndex = -10 + i;

                Entity entity = ecsEngine.createEntity();
                entity.addComponent(p);
                entity.addComponent(background);
                entity.addComponent(draw);
            }
        }
    }

    private Entity buildPlayer(Texture portrait, PlayerHandle handle, Color playerColor, DTO.Character characterData, Array<Vector2> spawnPoints)
    {
        Entity player = ecsEngine.createEntity();

        PlayerComponent pc = new PlayerComponent(handle, characterData.name);
        pc.lives = 10;
        player.addComponent(pc);

        int spawnIdx = MathUtils.random(0, spawnPoints.size - 1);
        Vector2 pos = spawnPoints.removeIndex(spawnIdx);
        player.addComponent(new PositionComponent(pos));

        player.addComponent(new JumpComponent(characterData.jumpStrength));
        player.addComponent(new GravityComponent(characterData.gravity));
        player.addComponent(new HealthComponent());
        player.addComponent(new UIComponent(portrait, playerColor));

        VelocityComponent vc = new VelocityComponent();
        vc.runSpeed = characterData.runSpeed;
        vc.airSpeed = characterData.airSpeed;
        vc.deceleration = characterData.deceleration;
        player.addComponent(vc);

        CharacterInputComponent i = new CharacterInputComponent();
        player.addComponent(i);

        AnimationContainerComponent<State> animContainer = new AnimationContainerComponent<>();

        animContainer.put(GroundIdleState.class, loadPlayerAnimation(characterData, "idle", Animation.PlayMode.LOOP));
        animContainer.put(GroundRunningState.class, loadPlayerAnimation(characterData, "run", Animation.PlayMode.LOOP));
        animContainer.put(JumpState.class, loadPlayerAnimation(characterData,"jump", Animation.PlayMode.NORMAL));
        animContainer.put(FallingState.class, loadPlayerAnimation(characterData, "fall", Animation.PlayMode.LOOP));
        animContainer.put(GroundAttackState.class, loadPlayerAnimation(characterData,"attack_1", Animation.PlayMode.NORMAL));
        animContainer.put(GroundStunnedState.class, loadPlayerAnimation(characterData, "stunned", Animation.PlayMode.NORMAL));
        animContainer.put(AirStunnedState.class, loadPlayerAnimation(characterData, "stunned", Animation.PlayMode.NORMAL));
        animContainer.put(AirAttackState.class, loadPlayerAnimation(characterData, "attack_air", Animation.PlayMode.NORMAL));
        animContainer.put(RespawnState.class, loadPlayerAnimation(characterData, "idle", Animation.PlayMode.LOOP));
        animContainer.put(BlockState.class, loadPlayerAnimation(characterData, "fall", Animation.PlayMode.NORMAL));
        animContainer.put(BlockStunnedState.class, loadPlayerAnimation(characterData, "stunned", Animation.PlayMode.NORMAL));
        animContainer.setDefault(GroundIdleState.class);
        player.addComponent(animContainer);

        State.setFireEventCallback(event -> ecsEngine.addEvent(event));
        StateComponent s = new StateComponent(new FallingState(player));
        player.addComponent(s);

        DrawComponent sd = new DrawComponent();
        sd.scale.x = characterData.scale;
        player.addComponent(sd);

        player.addComponent(new DebugDrawComponent());

        TerrainColliderComponent collider = new TerrainColliderComponent(characterData.terrainCollider);
        player.addComponent(collider);

        BlockComponent b = new BlockComponent();
        b.blockBox = new BodyBox(characterData.terrainCollider);
        b.blockBox.height *= 0.9f;
        b.blockBox.width *= 0.9f;
        player.addComponent(b);

        player.addComponent(new AttackableComponent());

        Entity attachment = ecsEngine.createEntity();
        attachment.addComponent(new PositionComponent());
        attachment.addComponent(new PositionAttachmentComponent(player, new Vector2(0, characterData.terrainCollider.height * 0.75f)));

        DrawComponent attachmentDraw = new DrawComponent();
        attachmentDraw.texture = RenderResources.getTexture("textures/player_indicator.png");
        attachmentDraw.getColor().set(playerColor);
        attachmentDraw.scale.x = 0.5f;
        attachment.addComponent(attachmentDraw);

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
            frames.add(new AnimationFrame(RenderResources.getTexture("textures/default.png"), new Array<>(), new Array<>(), new Array<>()));

            return new AnimationComponent(frames, 1, mode);
        }

        for (DTO.AnimationFrame dtoFrame : anim.frames)
        {
            AnimationFrame frame = new AnimationFrame(RenderResources.getTexture(dtoFrame.texturePath), dtoFrame.attackboxes, dtoFrame.bodyboxes, dtoFrame.events);

            frames.add(frame);
        }

        return new AnimationComponent(frames,  anim.animationDuration, mode);
    }

    public Entity buildTerrain(DTO.Terrain terrainData)
    {
        Entity terrain = ecsEngine.createEntity();

        PositionComponent tp = new PositionComponent();
        tp.position.set(terrainData.position);
        terrain.addComponent(tp);

        StaticTerrainComponent t = new StaticTerrainComponent();
        t.width = terrainData.collisionWidth;
        t.height = terrainData.collisionHeight;
        terrain.addComponent(t);

        DebugDrawComponent dd = new DebugDrawComponent();
        terrain.addComponent(dd);

        DrawComponent d = new DrawComponent();
        d.texture = RenderResources.getTexture(terrainData.textureFilePath);
        d.maintainAspectRatio = false;
        d.scale.x = terrainData.textureWidth;
        d.scale.y = terrainData.textureHeight;
        d.zIndex = 1;
        terrain.addComponent(d);

        return terrain;
    }
}
