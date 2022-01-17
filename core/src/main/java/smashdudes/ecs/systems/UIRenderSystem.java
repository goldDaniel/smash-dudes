package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class UIRenderSystem extends GameSystem
{
    private class CharacterDisplay implements Comparable
    {
        final String name;
        final int ID;
        float health;

        public CharacterDisplay(String name, int ID, float health)
        {
            this.name = name;
            this.ID = ID;
            this.health = health;
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

    public UIRenderSystem(Engine engine, SpriteBatch sb, BitmapFont font)
    {
        super(engine);
        this.sb = sb;
        this.font = font;

        worldWidth = 1280;
        worldHeight = 720;
        layout = new GlyphLayout(font, "");

        this.viewport = new ExtendViewport(worldWidth, worldHeight);
        this.camera = (OrthographicCamera)viewport.getCamera();

        registerComponentType(PlayerComponent.class);
        registerComponentType(HealthComponent.class);
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

        String name = play.name.substring(0, 1).toUpperCase() + play.name.substring(1);
        CharacterDisplay portrait = new CharacterDisplay(name, entity.ID, health.health);

        players.add(portrait);
        players.sort();
    }

    @Override
    public void postUpdate()
    {
        sb.begin();
        int sections = players.size;
        for(int i = 0; i < players.size; i++)
        {
            float xOffset = (i + 1) * worldWidth / (sections + 1) - worldWidth / 2;

            layout.setText(font, players.get(i).name);
            float nameWidth = layout.width;
            float nameHeight = layout.height;
            font.draw(sb, players.get(i).name, xOffset - nameWidth / 2, nameHeight - worldHeight / 2);

            String healthValue = String.format("%4.2f", players.get(i).health);
            layout.setText(font, healthValue);
            float healthWidth = layout.width;
            float healthHeight = layout.height;
            font.draw(sb, healthValue, xOffset - healthWidth / 2, healthHeight + nameHeight - worldHeight / 2);
        }
        sb.end();

        players.clear();
    }
}