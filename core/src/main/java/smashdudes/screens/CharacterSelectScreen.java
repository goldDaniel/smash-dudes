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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import smashdudes.core.PlayerHandle;
import smashdudes.core.input.*;
import smashdudes.graphics.RenderResources;
import smashdudes.graphics.ui.GameSkin;
import smashdudes.util.CharacterData;

public class CharacterSelectScreen extends GameScreen
{
    //RENDERING================================================
    private final float worldWidth = 1280;
    private final float worldHeight = 720;
    private FitViewport viewport = new FitViewport(worldWidth, worldHeight);

    //SELECTION================================================
    private CharacterSelectInputAssigner assigner = null;

    final int charactersPerRow = 4;
    private Table playerTable;
    private Array<Stack> characterEntries;

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
        viewport.getCamera().translate(worldWidth / 2, worldHeight / 2, 0);
        viewport.getCamera().update();

        availableColors.add(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);

        assigner = new CharacterSelectInputAssigner(
            (device, handle) -> // on joining game
            {
                SelectScreenPlayer joinedPlayer = CreatePlayerEntry(device, handle);
                joinedPlayers.put(handle, joinedPlayer);

                playerTable.add(joinedPlayer.playerImageStack).size(192, 192).padLeft(worldWidth / 20).padRight(worldHeight / 20);
                if(device == CharacterSelectInputAssigner.InputDevice.Keyboard)
                {
                    addInputProcessor((InputAdapter)joinedPlayer.input);
                }
                else
                {
                    Controllers.addListener((ControllerListener)joinedPlayer.input);
                }
            },
            (device, handle) -> // on leaving game
            {
                SelectScreenPlayer player = joinedPlayers.removeKey(handle);

                Cell<?> cell = playerTable.getCell(player.playerImageStack);
                cell.size(0,0);
                cell.padLeft(0);
                cell.padRight(0);
                player.playerImageStack.remove();

                availableColors.add(player.color);

                if(device == CharacterSelectInputAssigner.InputDevice.Keyboard)
                {
                    removeInputProcessor((InputAdapter)player.input);
                }
                else
                {
                    Controllers.removeListener((ControllerListener)player.input);
                }
            }
         );

        setViewport(viewport);
    }

    @Override
    public void buildUI(Table table, Skin skin)
    {
        table.setDebug(true);

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

                Image characterImage = new Image(data.texture);
                Label characterName = new Label(data.name, skin, "splash_continue");

                Stack stack = new Stack();
                Table characterGroup = new Table();
                characterGroup.add(characterImage).grow().row();
                characterGroup.add(characterName).bottom();
                stack.add(characterGroup);

                characterEntries.add(stack);

                availableCharacters.add(stack).width(128).height(128).pad(20).top();

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
        sb.draw(RenderResources.getTexture("textures/character_select.png"), 0,0, viewport.getWorldWidth(), viewport.getWorldHeight());


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

    private SelectScreenPlayer CreatePlayerEntry(CharacterSelectInputAssigner.InputDevice device, PlayerHandle handle)
    {
        SelectScreenPlayer joinedPlayer = new SelectScreenPlayer();

        if(device == CharacterSelectInputAssigner.InputDevice.Keyboard)
        {
            joinedPlayer.input = new KeyboardInputListener(new InputConfig(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.Z));
        }
        else if(device == CharacterSelectInputAssigner.InputDevice.Controller)
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
        for(Stack entry : characterEntries)
        {
            while(entry.getChildren().size > 1)
            {
                entry.removeActorAt(entry.getChildren().size - 1, false);
            }
        }

        for(int i = 0; i < joinedPlayers.size; ++i)
        {
            SelectScreenPlayer player = joinedPlayers.getValueAt(i);
            Stack entry = characterEntries.get(player.selectedCharacterIndex);


            Image image = new Image(RenderResources.getColor1x1(player.color));
            Container<Image> container = new Container<>(image);
            container.minSize(32, 32);

            if(i == 0) container.align(Align.topLeft);
            if(i == 1) container.align(Align.topRight);
            if(i == 2) container.align(Align.bottomLeft);
            if(i == 3) container.align(Align.bottomRight);

            entry.add(container);
        }
    }

    private void playerSelection()
    {
        for(int i = 0; i < joinedPlayers.size; ++i)
        {
            SelectScreenPlayer player = joinedPlayers.getValueAt(i);

            if(!player.lockedIn && player.input.confirmPressed())
            {
                player.lockedIn = true;

                // TODO (daniel): find way to access gameSkin more appropriately
                Label lockedInLabel = new Label("Locked In", new GameSkin(), "splash_continue");
                player.lockedInContainer = new Container<>(lockedInLabel).align(Align.bottom);
                player.playerImageStack.add(player.lockedInContainer);
            }
            else if(player.lockedIn && player.input.cancelPressed())
            {
                player.lockedIn = false;
                player.playerImageStack.removeActor(player.lockedInContainer);
                player.lockedInContainer = null;
            }

            if(!player.lockedIn)
            {
                player.selectedCharacterIndex = getPlayerSelection(player.input, player.selectedCharacterIndex);
            }
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
