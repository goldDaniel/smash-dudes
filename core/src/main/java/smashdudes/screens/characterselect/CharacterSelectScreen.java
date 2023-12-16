package smashdudes.screens.characterselect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import smashdudes.core.input.CharacterSelectInputAssigner;
import smashdudes.core.input.IGameInputListener;
import smashdudes.core.input.InputDeviceType;
import smashdudes.graphics.RenderResources;
import smashdudes.screens.GameScreen;
import smashdudes.screens.GameplayScreen;
import smashdudes.ui.characterselect.AvailableCharactersDisplay;
import smashdudes.ui.characterselect.SelectedCharacterDisplay;
import smashdudes.util.CharacterData;

public class CharacterSelectScreen extends GameScreen
{
    private FitViewport viewport;
    private CharacterSelectInputAssigner assigner;
    private SelectedCharacterDisplay selectedCharacterDisplay;
    private AvailableCharactersDisplay availableCharactersDisplay;
    private final PlayerLobby lobby;
    private float gameStartCountdown = 4;

    public CharacterSelectScreen(Game game)
    {
        super(game);

        lobby = new PlayerLobby((input, handle) -> // on leaving game
        {
            if(input.getDeviceType() == InputDeviceType.Keyboard)
            {
                removeInputProcessor((InputAdapter)input);
            }
            else if(input.getDeviceType() == InputDeviceType.Controller)
            {
                Controllers.removeListener((ControllerListener)input);
            }

            selectedCharacterDisplay.removeDisplay(handle);
            assigner.requestLeave(handle);
        });

        assigner = new CharacterSelectInputAssigner(
            (device, handle) -> // on joining game
            {
                lobby.join(assigner, device, handle);

                IGameInputListener input = lobby.getPlayer(handle).input;
                if(device == InputDeviceType.Controller)
                {
                    Controllers.addListener((ControllerListener)input);
                }
                else if(device == InputDeviceType.Keyboard)
                {
                    addInputProcessor((InputProcessor)input);
                }

                selectedCharacterDisplay.addDisplay(handle, lobby.getPlayerColor(handle));
            }
         );
    }

    @Override
    public void buildUI(Table table, Skin skin)
    {
        viewport = new FitViewport(1280, 720);
        viewport.getCamera().translate(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        setViewport(viewport);

        availableCharactersDisplay = new AvailableCharactersDisplay();
        for(CharacterData data : CharacterData.loadAllCharacters())
        {
            availableCharactersDisplay.insertCharacter(data);
        }
        table.add(availableCharactersDisplay).padTop(120).row();

        selectedCharacterDisplay = new SelectedCharacterDisplay(viewport.getWorldWidth(), viewport.getWorldHeight());
        table.add(selectedCharacterDisplay).expandX().fillX().height(192);
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        viewport.update(width, height);
        viewport.apply();
    }

    @Override
    public void show()
    {
        super.show();
        addInputProcessor(assigner);
        Controllers.addListener(assigner);
    }

    @Override
    public void hide()
    {
        super.hide();
        Controllers.clearListeners();
    }

    @Override
    public void update(float dt)
    {
        lobby.handlePlayerInput();
        availableCharactersDisplay.updateSelection(lobby.getPlayers());

        if(lobby.allPlayersLockedIn())
        {
            gameStartCountdown -= dt;

            if(gameStartCountdown <= 0)
            {
                game.setScreen(new GameplayScreen(game));
            }
        }
        else
        {
            gameStartCountdown = 4;
        }
    }

    @Override
    public void render()
    {
        SpriteBatch sb = RenderResources.getSpriteBatch();
        ShapeRenderer sh = RenderResources.getShapeRenderer();
        BitmapFont font = RenderResources.getFont("crimes", 32);

        ScreenUtils.clear(0,0,0,1);

        Matrix4 proj = viewport.getCamera().combined;
        sh.setProjectionMatrix(proj);
        sb.setProjectionMatrix(proj);
        sb.setColor(Color.WHITE);

        sb.setProjectionMatrix(proj);
        sb.begin();
        sb.draw(RenderResources.getTexture("textures/character_select.jpg"), 0,0, viewport.getWorldWidth(), viewport.getWorldHeight());


        final String message = "Choose your Character";
        final GlyphLayout layout = new GlyphLayout(font, message);
        font.draw(sb, message, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() - layout.height);

        if(lobby.allPlayersLockedIn())
        {
            final String number = "" + Math.max((int)gameStartCountdown, 0);
            final GlyphLayout numberLayout = new GlyphLayout(font, number);
            font.draw(sb, number, viewport.getWorldWidth() / 2 - numberLayout.width / 2, viewport.getWorldHeight() - layout.height - numberLayout.height - 4);
        }

        sb.end();
    }
}
