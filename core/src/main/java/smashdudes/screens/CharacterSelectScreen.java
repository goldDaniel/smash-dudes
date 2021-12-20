package smashdudes.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ScreenUtils;
import smashdudes.content.LoadContent;
import smashdudes.core.AudioResources;
import smashdudes.core.PlayerHandle;
import smashdudes.core.RenderResources;
import smashdudes.core.input.GameInputAssigner;
import smashdudes.core.input.GameInputHandler;
import smashdudes.core.input.GameInputRetriever;

public class CharacterSelectScreen extends GameScreen
{

    private ArrayMap<PlayerHandle, Circle> playerTokens = new ArrayMap<>();
    private ArrayMap<PlayerHandle, Color> playerColors = new ArrayMap<>();

    public GameInputAssigner inputAssigner = new GameInputAssigner();

    public CharacterSelectScreen(Game game)
    {
        super(game);
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
            if(!playerTokens.containsKey(p))
            {
                playerTokens.put(p, new Circle(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 32));
                playerColors.put(p, new Color(MathUtils.random(0f, 1f),MathUtils.random(0f, 1f),MathUtils.random(0f, 1f), 1f));
            }

            GameInputRetriever r = inputAssigner.getGameInputHandler().getGameInput(p);
            float speed = 128;
            if(r.getLeft())
            {
                playerTokens.get(p).x -= speed * dt;
            }
            if(r.getRight())
            {
                playerTokens.get(p).x += speed * dt;
            }
            if(r.getUp())
            {
                playerTokens.get(p).y += speed * dt;
            }
            if(r.getDown())
            {
                playerTokens.get(p).y -= speed * dt;
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

        Matrix4 proj = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sh.setColor(Color.BLACK);
        sh.setProjectionMatrix(proj);
        sh.begin(ShapeRenderer.ShapeType.Line);

        int x = 10;
        for(PlayerHandle h : inputAssigner.getPlayerHandles())
        {
            sh.setColor(playerColors.get(h));
            sh.rect(x, Gdx.graphics.getHeight() / 2 - 32, 180, 180);
            x += 200;

            sh.setColor(playerColors.get(h));
            Circle c = playerTokens.get(h);
            sh.circle(c.x, c.y, c.radius);
        }

        sh.end();

        s.setProjectionMatrix(proj);
        s.begin();
        font.getData().setScale(2);

        font.draw(s, "Press -SPACE- on keyboard to join", Gdx.graphics.getWidth() / 2 - 220, Gdx.graphics.getHeight() - 60);
        font.draw(s, "Press -A- on controller to join", Gdx.graphics.getWidth() / 2 - 160, Gdx.graphics.getHeight() - 120);

        x = 20;
        for(PlayerHandle h : inputAssigner.getPlayerHandles())
        {
            font.draw(s, "PlayerID: " + h.ID, x, Gdx.graphics.getHeight() / 2);
            x += 200;
        }
        font.draw(s, "Press enter to play", Gdx.graphics.getWidth() / 2 - 120, Gdx.graphics.getHeight() / 4);
        s.end();
    }
}
