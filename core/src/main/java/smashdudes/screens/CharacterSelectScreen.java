package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import smashdudes.core.PlayerHandle;
import smashdudes.core.input.*;
import smashdudes.graphics.RenderResources;
import smashdudes.ui.GameSkin;
import smashdudes.ui.characterselect.CharacterSelectionOverlay;
import smashdudes.ui.characterselect.CharacterSlot;
import smashdudes.util.CharacterData;

public class CharacterSelectScreen extends GameScreen
{
    //RENDERING================================================
    private final FitViewport viewport = new FitViewport(1280, 720);

    //SELECTION================================================
    private CharacterSelectInputAssigner assigner = null;

    private Table playerTable;
    final int charactersPerRow = 4;
    private Array<CharacterSlot> characterEntries;

    static class SelectScreenPlayer
    {
        public Color color;
        Stack playerImageStack;
        public int selectedCharacterIndex;

        public IGameInputListener input;

        public Container<Label> lockedInContainer = null;
        boolean lockedIn = false;
    }

    private final ArrayMap<PlayerHandle, SelectScreenPlayer> joinedPlayers = new ArrayMap<>();

    private final Array<Color> availableColors =new Array<>();

    private float gameStartCountdown = 4;

    public CharacterSelectScreen(Game game)
    {
        super(game);
        viewport.getCamera().translate(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();

        availableColors.add(Color.GOLDENROD, Color.OLIVE, Color.SALMON, Color.FIREBRICK);

        assigner = new CharacterSelectInputAssigner(
            (device, handle) -> // on joining game
            {
                SelectScreenPlayer joinedPlayer = CreatePlayerEntry(device, handle);
                joinedPlayers.put(handle, joinedPlayer);

                playerTable.add(joinedPlayer.playerImageStack).size(192, 192).padLeft(viewport.getWorldWidth() / 20).padRight(viewport.getWorldHeight() / 20);
                if(joinedPlayer.input.getDeviceType() == InputDeviceType.Keyboard)
                {
                    addInputProcessor((InputAdapter)joinedPlayer.input);
                }
                else
                {
                    Controllers.addListener((ControllerListener)joinedPlayer.input);
                }
            }
         );

        setViewport(viewport);
    }

    @Override
    public void buildUI(Table table, Skin skin)
    {
        Table availableCharacters = new Table();
        table.add(availableCharacters).padTop(120).row();

        characterEntries = new Array<>();

        // find character data
        FileHandle handle = Gdx.files.internal("characters");
        FileHandle[] characterDirectories = handle.list();
        for(int temp = 0; temp < 2; ++temp) // used to inflate number of character options
        {
            for(int i = 0; i < characterDirectories.length; ++i)
            {
                FileHandle characterDir = characterDirectories[(i + temp*2) % characterDirectories.length];

                if(!characterDir.isDirectory()) throw new IllegalStateException("Character directory should not contain files!");

                CharacterData data = CharacterData.LoadCharacter(characterDir);

                CharacterSlot selectableCharacter = new CharacterSlot(data);
                characterEntries.add(selectableCharacter);

                availableCharacters.add(selectableCharacter);

                if((availableCharacters.getChildren().size) % charactersPerRow == 0) {
                    availableCharacters.row();
                }
            }
        }

        this.playerTable = new Table();
        table.add(playerTable).expandX().fillX().height(192);
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

        if(doStartGameCountdown())
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

        if(doStartGameCountdown())
        {
            final String number = "" + Math.max((int)gameStartCountdown, 0);
            final GlyphLayout numberLayout = new GlyphLayout(font, number);
            font.draw(sb, number, viewport.getWorldWidth() / 2 - numberLayout.width / 2, viewport.getWorldHeight() - layout.height - numberLayout.height - 4);
        }

        sb.end();
    }

    private SelectScreenPlayer CreatePlayerEntry(InputDeviceType device, PlayerHandle handle)
    {
        SelectScreenPlayer joinedPlayer = new SelectScreenPlayer();

        if(device == InputDeviceType.Keyboard)
        {
            joinedPlayer.input = new KeyboardInputListener(new InputConfig(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.Z));
        }
        else if(device == InputDeviceType.Controller)
        {
            joinedPlayer.input = new ControllerInputListener(assigner.GetController(handle));
        }

        joinedPlayer.color = availableColors.first();
        availableColors.removeValue(joinedPlayer.color, false);
        joinedPlayer.selectedCharacterIndex = 0;

        Image image = new Image(RenderResources.getColor1x1(joinedPlayer.color));
        image.setSize(192, 192);
        joinedPlayer.playerImageStack = new Stack();
        joinedPlayer.playerImageStack.add(image);

        return joinedPlayer;
    }

    private void updateSelectionUI()
    {

        // reset selected entries
        for(CharacterSlot slot : characterEntries)
        {
            slot.resetSelection();
        }

        for(int i = 0; i < joinedPlayers.size; ++i)
        {
            SelectScreenPlayer player = joinedPlayers.getValueAt(i);
            CharacterSlot slot = characterEntries.get(player.selectedCharacterIndex);
            slot.addSelection(new CharacterSelectionOverlay(player.color, i + 1));
        }
    }

    private void playerSelection()
    {
        Array<PlayerHandle> toRemove = new Array<>();
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
                PlayerHandle handle = joinedPlayers.getKeyAt(i);
                assigner.requestLeave(handle);
                toRemove.add(handle);

                Cell<?> cell = playerTable.getCell(player.playerImageStack);
                cell.size(0,0);
                cell.padLeft(0);
                cell.padRight(0);
                player.playerImageStack.remove();

                availableColors.add(player.color);

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
        }
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

    private boolean doStartGameCountdown()
    {
        if(joinedPlayers.size < 2) return false;

        for(int i = 0; i < joinedPlayers.size; ++i)
        {
            SelectScreenPlayer player = joinedPlayers.getValueAt(i);
            if(!player.lockedIn) return false;
        }

        return true;
    }
}
