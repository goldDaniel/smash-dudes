package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.HealthComponent;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.UIComponent;
import smashdudes.ecs.events.CountdownEvent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.WinEvent;
import smashdudes.graphics.RenderResources;

public class UIRenderSystem extends GameSystem
{
    private class CharacterDisplay implements Comparable
    {
        final String name;
        final int ID;
        final float health;
        final Texture texture;

        int lives;

        public CharacterDisplay(String name, int ID, float health, Texture texture, int lives)
        {
            this.name = name;
            this.ID = ID;
            this.health = health;
            this.texture = texture;
            this.lives = lives;
        }

        @Override
        public int compareTo(Object o)
        {
            if(o.getClass() != CharacterDisplay.class) throw new IllegalArgumentException("Can only compare similar object types (CharacterDisplay.class)");
            return ID - ((CharacterDisplay) o).ID;
        }
    }

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final float worldWidth;
    private final float worldHeight;

    private final GlyphLayout layout;

    private final SpriteBatch sb;
    private final BitmapFont font;

    private final Array<CharacterDisplay> players = new Array<>();

    private static final String FINISH_COUNTDOWN = "";

    private String countDisplay = " ";
    private float displayTimer;

    public UIRenderSystem(Engine engine, SpriteBatch sb, BitmapFont font)
    {
        super(engine);
        this.sb = sb;
        this.font = font;

        worldWidth = 1280;
        worldHeight = 720;
        layout = new GlyphLayout(font, "");

        displayTimer = 0;

        this.viewport = new ExtendViewport(worldWidth, worldHeight);
        this.camera = (OrthographicCamera)viewport.getCamera();

        registerComponentType(PlayerComponent.class);
        registerComponentType(HealthComponent.class);
        registerComponentType(UIComponent.class);

        registerEventType(CountdownEvent.class);
        registerEventType(WinEvent.class);
    }

    public void resize(int w, int h)
    {
        viewport.update(w, h);
        viewport.apply();
    }

    @Override
    public void preUpdate()
    {
        sb.setProjectionMatrix(camera.combined);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerComponent play = entity.getComponent(PlayerComponent.class);
        HealthComponent health = entity.getComponent(HealthComponent.class);
        UIComponent UI = entity.getComponent(UIComponent.class);

        String name = play.name.substring(0, 1).toUpperCase() + play.name.substring(1);
        CharacterDisplay portrait = new CharacterDisplay(name, play.handle.ID, health.health, UI.tex, play.lives);

        players.add(portrait);
        players.sort();

        if(countDisplay.equals("GO!"))
        {
            displayTimer += dt;

            if(displayTimer >= 1)
            {
                displayTimer = 0;
                countDisplay = FINISH_COUNTDOWN; // after GO! is displayed for 1 second,
            }
        }
    }

    @Override
    public void postUpdate()
    {
        sb.setColor(Color.WHITE);
        sb.begin();
        int sections = players.size;
        for(int i = 0; i < players.size; i++)
        {
            layout.setText(font, players.get(i).name);
            float nameWidth = layout.width;
            float nameHeight = layout.height;

            float xOffset = (i + 1) * worldWidth / (sections + 1) - worldWidth / 2;
            float yOffset = nameHeight; // stock icon size

            Texture texture = players.get(i).texture;
            float ratio = (float) texture.getHeight() / (float) texture.getWidth();
            float width = 1.5f * nameWidth;
            float height = ratio * width;
            sb.draw(players.get(i).texture, xOffset - width / 2, - worldHeight / 2, width, height);

            font.draw(sb, players.get(i).name, xOffset - nameWidth / 2, nameHeight + yOffset - worldHeight / 2);

            int lives = players.get(i).lives;
            if(lives > 0)
            {
                String healthValue = String.format("%4.2f", players.get(i).health);
                layout.setText(font, healthValue);
                float healthWidth = layout.width;
                float healthHeight = layout.height;
                font.draw(sb, healthValue, xOffset - healthWidth / 2, healthHeight + nameHeight + yOffset - worldHeight / 2);
            }

            Texture stockTex = RenderResources.getTexture("textures/circle.png"); // should be changed to a texture corresponding to the player
            float stockRatio = (float) stockTex.getWidth() / (float) stockTex.getHeight();
            float stockHeight = yOffset;
            float stockWidth = stockRatio * stockHeight;
            if(lives < 6) // maximum of 5 stock icons to avoid resizing
            {
                for(int j = 0; j < lives; j++)
                {
                    float stockShift = (j - (float) (lives - 1) / 2) * stockWidth;
                    sb.draw(stockTex, stockShift + xOffset - stockWidth / 2, - worldHeight / 2, stockWidth, stockHeight);
                }
            }
            else
            {
                layout.setText(font, "1");
                float livesWidth = layout.width;
                float livesHeight = layout.height;
                font.draw(sb, "" + lives, xOffset, livesHeight - worldHeight / 2);

                sb.draw(stockTex,  xOffset -(livesWidth + stockWidth / 2), -worldHeight / 2, stockWidth, stockHeight);
            }
        }
        if(!countDisplay.equals(FINISH_COUNTDOWN))
        {
            layout.setText(font, countDisplay);
            float width = layout.width;
            float height = layout.height;
            font.draw(sb, countDisplay, - width / 2, - height / 2);
        }
        sb.end();

        players.clear();
    }

    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof CountdownEvent)
        {
            CountdownEvent c = (CountdownEvent)event;
            countDisplay = c.currTime + "";
            if(c.currTime == 0)
            {
                countDisplay = "GO!";
            }
        }
        if(event instanceof WinEvent)
        {
            WinEvent w = (WinEvent)event;
            countDisplay = "GAME!";
        }
    }
}