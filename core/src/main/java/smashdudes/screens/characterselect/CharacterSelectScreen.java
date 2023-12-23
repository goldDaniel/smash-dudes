package smashdudes.screens.characterselect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import smashdudes.core.AudioResources;
import smashdudes.core.PlayerLobbyInfo;
import smashdudes.core.input.CharacterSelectInputAssigner;
import smashdudes.core.input.IGameInputListener;
import smashdudes.core.input.MenuNavigator;
import smashdudes.graphics.RenderResources;
import smashdudes.screens.GameScreen;
import smashdudes.screens.GameplayScreen;
import smashdudes.ui.characterselect.AvailableCharactersDisplay;
import smashdudes.ui.characterselect.SelectedCharacterDisplay;
import smashdudes.util.CharacterData;

public class CharacterSelectScreen extends GameScreen
{
    private Array<CharacterData> characterData;
    private CharacterSelectInputAssigner assigner;
    private SelectedCharacterDisplay selectedCharacterDisplay;
    private AvailableCharactersDisplay availableCharactersDisplay;
    private final PlayerLobby lobby;

    private Image readyBanner;

    public CharacterSelectScreen(Game game)
    {
        super(game);
        lobby = new PlayerLobby((input, handle) -> // on leaving game
        {
            removeInputProcessor(input);

            selectedCharacterDisplay.removeDisplay(handle);
            assigner.requestLeave(handle);
            AudioResources.getSoundEffect("audio/ui/lobby_leave.ogg").play();
        });

        assigner = new CharacterSelectInputAssigner(
            (device, handle) -> // on joining game
            {
                lobby.join(assigner, device, handle);

                IGameInputListener input = lobby.getPlayer(handle).input;
                addInputProcessor(input);

                selectedCharacterDisplay.addDisplay(handle, lobby.getPlayerColor(handle));
                AudioResources.getSoundEffect("audio/ui/lobby_join.ogg").play();
            }
         );
    }

    @Override
    public void buildUI(Table table, Skin skin, MenuNavigator navigator)
    {
        Stack uiStack = new Stack();

        Table uiTable = new Table();
        uiTable.setFillParent(true);

        availableCharactersDisplay = new AvailableCharactersDisplay();

        characterData = CharacterData.loadAllCharacters();
        for(CharacterData data : characterData)
        {
            availableCharactersDisplay.insertCharacter(data);
        }
        uiTable.add(availableCharactersDisplay).padTop(16).padBottom(64).row();

        selectedCharacterDisplay = new SelectedCharacterDisplay(getViewport().getWorldWidth(), getViewport().getWorldHeight());
        uiTable.add(selectedCharacterDisplay).expandX().fillX().fillY().height(192);

        readyBanner = new Image(RenderResources.getTexture("textures/character_select_ready.png"));
        readyBanner.setVisible(false);
        uiStack.add(uiTable);
        uiStack.add(readyBanner);

        table.add(uiStack).grow();
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
        removeInputProcessor(assigner);
        Controllers.clearListeners();
    }

    @Override
    public void update(float dt)
    {
        handleLobbyReady();

        lobby.handlePlayerInput();
        availableCharactersDisplay.updateSelection(lobby.getPlayers());
        selectedCharacterDisplay.updateSelection(lobby.getPlayers(), characterData);
    }

    @Override
    public void render()
    {
        SpriteBatch sb = RenderResources.getSpriteBatch();
        BitmapFont font = RenderResources.getFont("crimes", 32);

        ScreenUtils.clear(0,0,0,1);

        sb.setProjectionMatrix(getViewport().getCamera().combined);
        sb.setColor(Color.WHITE);
        sb.begin();
        sb.draw(RenderResources.getTexture("textures/character_select.jpg"), 0,0, getViewport().getWorldWidth(), getViewport().getWorldHeight());

        final String message = "Choose your Character";
        final GlyphLayout layout = new GlyphLayout(font, message);
        font.draw(sb, message, getViewport().getWorldWidth() / 2 - layout.width / 2, getViewport().getWorldHeight() - layout.height);

        sb.end();
    }

    private void handleLobbyReady()
    {
        if(lobby.allPlayersLockedIn())
        {
            if(!readyBanner.isVisible())
            {
                readyBanner.setVisible(true);
            }
            else
            {
                for (PlayerLobbyInfo player : lobby.getPlayers())
                {
                    if(player.input.confirmPressed())
                    {
                        game.setScreen(new GameplayScreen(game, lobby.getPlayers(), characterData));
                        break;
                    }
                }

            }
        }
        else
        {
            if(readyBanner.isVisible())
            {
                readyBanner.setVisible(false);
            }
        }
    }
}
