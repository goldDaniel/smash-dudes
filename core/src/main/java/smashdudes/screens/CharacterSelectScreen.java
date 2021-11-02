package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import smashdudes.core.RenderResources;

public class CharacterSelectScreen extends GameScreen
{
    public CharacterSelectScreen(Game game)
    {
        super(game);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void update(float dt)
    {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            game.setScreen(new GameplayScreen(game));
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
        font.draw(s, "Press enter to continue", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        s.end();
    }
}
