package smashdudes.core.characterselect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import smashdudes.content.ContentRepo;
import smashdudes.content.LoadContent;
import smashdudes.core.PlayerHandle;
import smashdudes.graphics.RenderResources;
import smashdudes.util.CharacterSelectDescription;

public class CharacterSelector
{
    private final float worldWidth;
    private final float worldHeight;

    private GlyphLayout layout;

    private class CharacterPortrait
    {
        public String identifier;
        public Texture texture;
        public Rectangle rect;
        public String name;
    }

    private class PlayerPortrait
    {
        public PlayerHandle handle;
        public Rectangle rect;
        public boolean lockedIn;
        public String identifier = null;
        public Texture texture;
    }

    private Array<CharacterPortrait> portraits = new Array<>();
    private ArrayMap<PlayerHandle, Cursor> cursors = new ArrayMap<>();
    private Array<PlayerPortrait> players = new Array<>();

    public CharacterSelector(float worldWidth, float worldHeight, BitmapFont font)
    {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        layout = new GlyphLayout(font, "");

        float portraitWidth = worldWidth / 8;
        float portraitHeight = portraitWidth;

        Array<String> identifiers = initIdentifier();
        int numBoxes = identifiers.size;
        int maxWidth = MathUtils.ceil(numBoxes / 2f);
        if (numBoxes > 6)
        {
            maxWidth = 5;
        }
        int rowNum = 1;
        float xOffset = (worldWidth - portraitWidth * maxWidth) / 2;


        float portraitY = worldHeight / 2;
        for (int i = 0; i < numBoxes; i++)
        {
            if(i % maxWidth == 0 && i > 0)
            {
                rowNum++;
                portraitY -= portraitHeight;
                if ((numBoxes - rowNum * maxWidth) < 0)
                {
                    xOffset = (worldWidth - portraitWidth * (maxWidth + (numBoxes - rowNum * maxWidth))) / 2;
                }
                else
                {
                    xOffset = (worldWidth - portraitWidth * maxWidth) / 2;
                }
            }
            CharacterPortrait p = new CharacterPortrait();
            p.rect = new Rectangle(xOffset + (i % maxWidth) * portraitWidth, portraitY, portraitWidth, portraitHeight);
            p.identifier = identifiers.get(i);
            p.name = getNameFromIdentifier(identifiers.get(i));

            p.texture = getPortraitForCharacter(identifiers.get(i));
            portraits.add(p);
        }
    }

    private String getNameFromIdentifier(String path)
    {
        String[] split = path.split("/");
        String result = split[split.length - 1];
        return result.replace(".json", "");
    }

    private Texture getPortraitForCharacter(String path)
    {
        path = path.replace(".json", "/portrait/portrait.png");
        return RenderResources.getTexture(path);
    }

    private Array<String> initIdentifier()
    {
        FileHandle handle = Gdx.files.internal("characters");
        Array<String> filepaths = new Array<>();
        for (FileHandle entry : handle.list())
        {
            String path = entry.path();
            if(path.contains(".json"))
            {
                filepaths.add(path);
            }
        }

        return filepaths;
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
                        p.texture = character.texture;
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

    public void render(ShapeRenderer sh, SpriteBatch sb)
    {
        sb.begin();
        for(CharacterPortrait p : portraits)
        {
            if(p.texture != null)
            {
                sb.draw(p.texture, p.rect.x, p.rect.y, p.rect.width, p.rect.height);
            }
        }

        for(PlayerPortrait p : players)
        {
            if(p.texture != null)
            {
                sb.draw(p.texture, p.rect.x, p.rect.y, p.rect.width, p.rect.height);
            }
        }
        sb.end();

        sh.begin(ShapeRenderer.ShapeType.Line);
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
        sh.end();
    }

    public Array<CharacterSelectDescription.PlayerDescription> getPlayerDescriptions()
    {
        if(!areAllPlayersLockedIn()) throw new IllegalStateException("all players must be locked in first");

        Array<CharacterSelectDescription.PlayerDescription> result = new Array<>();

        for(PlayerPortrait p : players)
        {
            CharacterSelectDescription.PlayerDescription desc =
                    new CharacterSelectDescription.PlayerDescription(p.identifier, p.handle);

            result.add(desc);
        }

        return result;
    }


    public void render(SpriteBatch s, BitmapFont f)
    {
        for(CharacterPortrait p : portraits)
        {
            layout.setText(f, p.name);
            float width = layout.width;
            float height = layout.height;
            f.draw(s, p.name, p.rect.x + width / 2, p.rect.y + height);
        }

//        for(PlayerPortrait p : players)
//        {
//            if(p.identifier != null)
//            {
//                f.draw(s, p.name, p.rect.x + p.rect.width / 2, p.rect.y + p.rect.height / 2);
//            }
//        }
    }
}
