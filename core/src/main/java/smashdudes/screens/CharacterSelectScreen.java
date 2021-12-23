package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import smashdudes.core.PlayerHandle;
import smashdudes.core.RenderResources;
import smashdudes.core.characterselect.CharacterSelector;
import smashdudes.core.input.GameInputAssigner;
import smashdudes.core.input.GameInputRetriever;
import smashdudes.core.input.MenuInputRetriever;

public class CharacterSelectScreen extends GameScreen
{
    //RENDERING================================================
    private final float worldWidth = 1280;
    private final float worldHeight = 720;
    private FitViewport viewport = new FitViewport(worldWidth, worldHeight);

    //SELECTION================================================
    private GameInputAssigner inputAssigner = new GameInputAssigner();
    private CharacterSelector selector = new CharacterSelector(worldWidth, worldHeight);

    public CharacterSelectScreen(Game game)
    {
        super(game);
        viewport.getCamera().translate(worldWidth / 2, worldHeight / 2, 0);
        viewport.getCamera().update();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.apply();
    }

    @Override
    public void show()
    {
        inputAssigner.startListening();
    }

    @Override
    public void hide()
    {
        inputAssigner.stopListening();
    }

    @Override
    public void update(float dt)
    {
        for(PlayerHandle p : inputAssigner.getPlayerHandles())
        {
            if(!selector.hasPlayerHandle(p))
            {
                selector.insert(p);
            }

            MenuInputRetriever r = inputAssigner.getMenuInput(p);
            selector.getCursor(p).updatePosition(r, dt);

            if(r.confirmPressed())
            {
                selector.attemptSelect(p);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            game.setScreen(new GameplayScreen(game, inputAssigner.getPlayerHandles(), inputAssigner.getGameInputHandler()));
        }
    }

    @Override
    public void render()
    {
        SpriteBatch s = RenderResources.getSpriteBatch();
        ShapeRenderer sh = RenderResources.getShapeRenderer();
        BitmapFont font = RenderResources.getFont();

        ScreenUtils.clear(0,0,0,1);

        Matrix4 proj = viewport.getCamera().combined;
        sh.setProjectionMatrix(proj);
        sh.begin(ShapeRenderer.ShapeType.Line);
        selector.render(sh);
        sh.end();


        s.setProjectionMatrix(proj);
        s.begin();
        selector.render(s, font);
        font.getData().setScale(2);

        font.draw(s, "Press -SPACE- on keyboard to join", worldWidth / 2 - 220, worldHeight - 60);
        font.draw(s, "Press -A- on controller to join", worldWidth / 2 - 160, worldHeight - 120);


        font.draw(s, "Press enter to play", worldWidth / 2 - 120, worldHeight / 4);
        s.end();
    }
}
