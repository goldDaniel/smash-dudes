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
import com.badlogic.gdx.utils.ScreenUtils;
import org.libsdl.SDL;
import smashdudes.content.ContentRepo;
import smashdudes.content.DTO;
import smashdudes.content.ContentLoader;
import smashdudes.core.PlayerHandle;
import smashdudes.core.Projectile;
import smashdudes.core.WorldUtils;
import smashdudes.core.input.GameInputHandler;
import smashdudes.core.input.IGameInputRetriever;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.systems.GameOverSystem;
import smashdudes.graphics.AnimationFrame;
import smashdudes.graphics.RenderResources;
import smashdudes.util.CharacterSelectDescription;

public class GameplayScreen extends GameScreen
{
    private final int WORLD_WIDTH = 20;
    private final int WORLD_HEIGHT = 12;

    private GameInputHandler inputHandler;
    private Engine ecsEngine;

    public GameplayScreen(Game game, CharacterSelectDescription desc)
    {
        super(game);
        this.inputHandler = desc.gameInput;
        ecsEngine = new Engine(WORLD_WIDTH, WORLD_HEIGHT, () -> transitionTo(new MainMenuScreen(game)));

        DTO.Stage stage = ContentLoader.loadStage("stage.json");

        WorldUtils.setStage(stage);

        for (DTO.Terrain data : stage.terrain)
        {
            buildTerrain(data);
        }


        for (CharacterSelectDescription.PlayerDescription p : desc.descriptions)
        {
            DTO.Character characterData = ContentRepo.loadCharacter(p.identifier);
            Entity player = buildPlayer(p.portrait, p.handle, characterData, stage.spawnPoints);

            IGameInputRetriever retriever = inputHandler.getGameInput(p.handle);

            PlayerControllerComponent pc = new PlayerControllerComponent(retriever);
            player.addComponent(pc);
        }

        buildParallaxBackground();
    }

    @Override
    public void show()
    {
        super.show();
        addInputProcessor(inputHandler.getInputProcessor());
    }

    @Override
    public void hide()
    {
        super.hide();
    }

    @Override
    public void buildUI(Table table, Skin skin)
    {

    }

    @Override
    public void update(float dt)
    {
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
        float maxStep = 1 / 30f;

        if (dt > maxStep) dt = maxStep;
        ecsEngine.update(dt);
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(Color.SKY);
        ecsEngine.render();
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

                background.parallax.x = 0.05f * i;

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

    private Entity buildPlayer(Texture portrait, PlayerHandle handle, DTO.Character characterData, Array<Vector2> spawnPoints)
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
        player.addComponent(new UIComponent(portrait));

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
        sd.scale.x = characterData.scale;
        player.addComponent(sd);

        player.addComponent(new DebugDrawComponent());

        TerrainColliderComponent collider = new TerrainColliderComponent(characterData.terrainCollider);
        player.addComponent(collider);

        player.addComponent(new AttackableComponent());

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
        else
        {
            for (DTO.AnimationFrame dtoFrame : anim.frames)
            {
                Array<Projectile> projectiles = new Array<>();
                for(DTO.Projectile projectile : dtoFrame.projectiles)
                {
                    projectiles.add(new Projectile(projectile.speed, projectile.dim, projectile.pos,
                                                   projectile.knockback, projectile.damage, projectile.lifeTime,
                                                   RenderResources.getTexture(projectile.texturePath)));
                }
                AnimationFrame frame =
                        new AnimationFrame(RenderResources.getTextureDownsampled(dtoFrame.texturePath, 64),
                                           dtoFrame.attackboxes, dtoFrame.bodyboxes, projectiles);

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
