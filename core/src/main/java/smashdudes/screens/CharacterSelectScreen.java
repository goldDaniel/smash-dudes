package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import smashdudes.core.PlayerHandle;
import smashdudes.core.RenderResources;
import smashdudes.core.input.GameInputAssigner;

public class CharacterSelectScreen extends GameScreen
{

    public GameInputAssigner inputAssigner = new GameInputAssigner();


    public CharacterSelectScreen(Game game)
    {
        super(game);
        inputAssigner.startListening();
    }

    @Override
    public void update(float dt)
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            game.setScreen(new GameplayScreen(game, inputAssigner.getPlayerHandles(), inputAssigner.getGameInputHandler()));
            inputAssigner.stopListening();
        }
    }

    @Override
    public void render()
    {
        SpriteBatch s = RenderResources.getSpriteBatch();
        ShapeRenderer sh = RenderResources.getShapeRenderer();
        BitmapFont font = RenderResources.getFont();

        sh.setColor(Color.BLACK);
        sh.setProjectionMatrix(new Matrix4());
        sh.begin(ShapeRenderer.ShapeType.Filled);
        sh.rect(-1,-1,2,2);
        sh.end();

        Matrix4 proj = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        s.setProjectionMatrix(proj);
        s.begin();
        font.getData().setScale(2);

        font.draw(s, "Press -SPACE- on keyboard to join", Gdx.graphics.getWidth() / 2 - 220, Gdx.graphics.getHeight() - 60);
        font.draw(s, "Press -A- on controller to join", Gdx.graphics.getWidth() / 2 - 160, Gdx.graphics.getHeight() - 120);

        int x = 10;
        for(PlayerHandle h : inputAssigner.getPlayerHandles())
        {
            font.draw(s, "PlayerID: " + h.ID, x, Gdx.graphics.getHeight() / 2);
            x += 200;
        }


        font.draw(s, "Press enter to play", Gdx.graphics.getWidth() / 2 - 120, Gdx.graphics.getHeight() / 4);
        s.end();
    }
}
