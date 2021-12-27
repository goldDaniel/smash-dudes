package smashdudes.core.characterselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import smashdudes.core.PlayerHandle;

public class CharacterSelector
{
    private final float worldWidth;
    private final float worldHeight;

    private class CharacterPortrait
    {
        public String identifier;
        public Rectangle rect;
    }

    private class PlayerPortrait
    {
        public PlayerHandle handle;
        public Rectangle rect;
        public boolean lockedIn;
        public String identifier = null;
    }

    private Array<CharacterPortrait> portraits = new Array<>();
    private ArrayMap<PlayerHandle, Cursor> cursors = new ArrayMap<>();
    private Array<PlayerPortrait> players = new Array<>();

    public CharacterSelector(float worldWidth, float worldHeight)
    {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        float portraitWidth = worldWidth / 8;
        float portraitHeight = portraitWidth;

        float numBoxes = 4;
        float xOffset = (worldWidth - portraitWidth * numBoxes) / 2;

        for (int i = 0; i < numBoxes; i++)
        {
            CharacterPortrait p = new CharacterPortrait();
            p.rect = new Rectangle(xOffset + i * portraitWidth, worldHeight / 2, portraitWidth, portraitHeight);
            p.identifier = "" + (char) ('a' + i * 2);
            portraits.add(p);
            portraits.add(p);

            p = new CharacterPortrait();
            p.rect = new Rectangle(xOffset + i * portraitWidth, worldHeight / 2 - portraitHeight, portraitWidth, portraitHeight);
            p.identifier = "" + (char) ('a' + (i) * 2 + 1);
            portraits.add(p);
        }
    }

    public void updateCursorPosition(PlayerHandle p, Vector2 direction, float dt)
    {
        cursors.get(p).updatePosition(direction, dt);
    }

    public void attemptSelect(PlayerHandle handle)
    {
        if (!hasPlayerHandle(handle)) throw new IllegalStateException("Player not registered");

        for(PlayerPortrait p : players)
        {
            if(p.handle == handle)
            {
                Cursor c = cursors.get(handle);

                for(CharacterPortrait character : portraits)
                {
                    if(character.rect.contains(c.circle.x, c.circle.y))
                    {
                        p.identifier = character.identifier;
                        p.lockedIn = true;
                    }
                }
            }
        }
    }

    public void attemptCancel(PlayerHandle handle)
    {
        for(PlayerPortrait p : players)
        {
            if(p.handle == handle)
            {
                p.lockedIn = false;
                p.identifier = null;
            }
        }
    }

    public boolean areAllPlayersLockedIn()
    {
        if(players.isEmpty()) return false;

        for(PlayerPortrait p : players)
        {
            if(!p.lockedIn)
            {
                return false;
            }
        }
        return true;
    }

    public Cursor getCursor(PlayerHandle p)
    {
        if (!hasPlayerHandle(p)) throw new IllegalStateException("Player not registered");

        return cursors.get(p);
    }

    public boolean hasPlayerHandle(PlayerHandle p)
    {
        return cursors.containsKey(p);
    }

    public void insert(PlayerHandle p)
    {
        if(hasPlayerHandle(p)) throw new IllegalStateException("Already registered");

        Circle circle = new Circle(worldWidth / 2, worldHeight / 2, 32);
        Color color = new Color(MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), MathUtils.random(0f, 1f), 1f);
        Cursor c = new Cursor(circle, color);

        cursors.put(p, c);

        PlayerPortrait portrait = new PlayerPortrait();
        portrait.handle = p;
        portrait.lockedIn = false;

        float portraitWidth = worldWidth / 8;
        float portraitHeight = worldHeight / 4;
        if(players.isEmpty())
        {
            Rectangle rect = new Rectangle();
            rect.x = 2;
            rect.y = 2;
            rect.width = portraitWidth;
            rect.height = portraitHeight;

            portrait.rect = rect;
        }
        else
        {
            PlayerPortrait lastPortrait = players.get(players.size - 1);
            Rectangle rect = new Rectangle();
            rect.x = lastPortrait.rect.x + lastPortrait.rect.width + 32;
            rect.y = 2;
            rect.width = portraitWidth;
            rect.height = portraitHeight;

            portrait.rect = rect;
        }
        players.add(portrait);
    }

    public void render(ShapeRenderer sh)
    {
        for(CharacterPortrait p : portraits)
        {
            sh.setColor(Color.RED);
            sh.rect(p.rect.x, p.rect.y, p.rect.width, p.rect.height);
        }

        for(ObjectMap.Entry<PlayerHandle, Cursor> c : cursors)
        {
            sh.setColor(c.value.color);

            Circle circle = c.value.circle;
            sh.circle(circle.x, circle.y, circle.radius);
        }

        for(PlayerPortrait p : players)
        {
            sh.setColor(cursors.get(p.handle).color);

            Rectangle rect = p.rect;
            sh.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    public void render(SpriteBatch s, BitmapFont f)
    {
        for(CharacterPortrait p : portraits)
        {
            f.draw(s, p.identifier, p.rect.x + p.rect.width / 2, p.rect.y + p.rect.height / 2);
        }

        for(PlayerPortrait p : players)
        {
            if(p.identifier != null)
            {
                f.draw(s, p.identifier, p.rect.x + p.rect.width / 2, p.rect.y + p.rect.height / 2);
            }
        }
    }
}
