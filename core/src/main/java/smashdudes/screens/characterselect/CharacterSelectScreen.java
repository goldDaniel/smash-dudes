package smashdudes.screens.characterselect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputAdapter;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import smashdudes.core.input.CharacterSelectInputAssigner;
import smashdudes.core.input.IMenuInputRetriever;
import smashdudes.core.input.InputDeviceType;
import smashdudes.graphics.RenderResources;
import smashdudes.screens.GameScreen;
import smashdudes.screens.GameplayScreen;
import smashdudes.ui.characterselect.CharacterSelectionOverlay;
import smashdudes.ui.characterselect.CharacterSlot;
import smashdudes.ui.characterselect.SelectedCharacterDisplay;
import smashdudes.util.CharacterData;

public class CharacterSelectScreen extends GameScreen
{
    private FitViewport viewport;
    private CharacterSelectInputAssigner assigner = null;
    private final PlayerLobby lobby;
    private SelectedCharacterDisplay selectedCharacterDisplay;
    final int charactersPerRow = 4;
    private Array<CharacterSlot> characterEntries;
    private float gameStartCountdown = 4;

    public CharacterSelectScreen(Game game)
    {
        super(game);
        lobby = new PlayerLobby((input) ->
        {
            if(input.getDeviceType() == InputDeviceType.Keyboard)
            {
                removeInputProcessor((InputAdapter)input);
            }
            else if(input.getDeviceType() == InputDeviceType.Controller)
            {
                Controllers.removeListener((ControllerListener)input);
            }
        });

        assigner = new CharacterSelectInputAssigner(
            (device, handle) -> // on joining game
            {
                lobby.join(assigner, device, handle);
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

        selectedCharacterDisplay = new SelectedCharacterDisplay(viewport.getWorldWidth(), viewport.getWorldHeight());
        characterEntries = new Array<>();

        Table availableCharacters = new Table();
        table.add(availableCharacters).padTop(120).row();

        Array<CharacterData> characterData = CharacterData.loadAllCharacters();
        for(CharacterData data : characterData)
        {
            CharacterSlot selectableCharacter = new CharacterSlot(data);
            characterEntries.add(selectableCharacter);
            availableCharacters.add(selectableCharacter);
            if((availableCharacters.getChildren().size) % charactersPerRow == 0) {
                availableCharacters.row();
            }
        }

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
        playerSelection();
        updateSelectionUI();

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


    private void updateSelectionUI()
    {

        // reset selected entries
        for(CharacterSlot slot : characterEntries)
        {
            slot.resetSelection();
        }

        for(PlayerLobby.PlayerID id : lobby.getPlayers())
        {
            CharacterSlot slot = characterEntries.get(id.selected);
            slot.addSelection(new CharacterSelectionOverlay(id.color, id.playerNumber));
        }
    }

    private void playerSelection()
    {
        /*Array<PlayerHandle> toRemove = new Array<>();
        for(int i = 0; i < joinedPlayers.size; ++i)
        {
            SelectScreenPlayer player = joinedPlayers.getValueAt(i);
            boolean cancelPressed = player.input.cancelPressed();
            boolean confirmPressed = player.input.confirmPressed();

            if(!player.lockedIn && confirmPressed)
            {
                player.lockedIn = true;

                // TODO (daniel): find way to access gameSkin more appropriately
                Label lockedInLabel = new Label("Locked In", GameSkin.Get(), "splash_continue");
                player.lockedInContainer = new Container<>(lockedInLabel).align(Align.bottom);
                player.playerImageStack.add(player.lockedInContainer);
            }
            else if(player.lockedIn && cancelPressed)
            {
                player.lockedIn = false;
                player.playerImageStack.removeActor(player.lockedInContainer);
                player.lockedInContainer = null;
            }
            else if(!player.lockedIn && cancelPressed)
            {
                selectedCharacterDisplay.removeDisplay(handle);
                lobby.leave(handle);
                assigner.requestLeave(handle);

                if(player.input.getDeviceType() == InputDeviceType.Keyboard)
                {
                    removeInputProcessor((InputAdapter)player.input);
                }
                else if(player.input.getDeviceType() == InputDeviceType.Controller)
                {
                    Controllers.removeListener((ControllerListener)player.input);
                }
            }
            else if(!player.lockedIn)
            {
                player.selectedCharacterIndex = getPlayerSelection(player.input, player.selectedCharacterIndex);
            }
        }
        for(PlayerHandle handle : toRemove)
        {
            joinedPlayers.removeKey(handle);
        }*/
    }

    // NOTE (danielg): This only works if each row is the same size
    private int getPlayerSelection(IMenuInputRetriever input, int currentIndex)
    {
        if(input.leftPressed())
        {
            if(currentIndex % charactersPerRow == 0)
            {
                currentIndex += charactersPerRow;
            }
            currentIndex--;
        }
        if(input.rightPressed())
        {
            currentIndex++;
            if(currentIndex % charactersPerRow == 0)
            {
                currentIndex -= charactersPerRow;
            }
        }
        if(input.upPressed())
        {
            currentIndex -= charactersPerRow;
            if(currentIndex < 0)
            {
                currentIndex += characterEntries.size;
            }
        }
        if(input.downPressed())
        {
            currentIndex += charactersPerRow;
            if(currentIndex > (characterEntries.size - 1))
            {
                currentIndex -= characterEntries.size;
            }
        }

        return currentIndex;
    }
}
