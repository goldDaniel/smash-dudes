package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PlayerComponent;

public class UIRenderSystem extends GameSystem
{
    private class CharacterDisplay implements Comparable
    {
        final String name;
        final int ID;

        public CharacterDisplay(String name, int ID)
        {
            this.name = name;
            this.ID = ID;
        }

        @Override
        public int compareTo(Object o)
        {
            return ID - ((CharacterDisplay) o).ID;
        }

        @Override
        public boolean equals(Object o)
        {
            return ((CharacterDisplay) o).ID == ID;
        }
    }

    private OrthographicCamera camera;
    private Viewport viewport;

    private final SpriteBatch sb;
    private final BitmapFont font;

    private final Array<CharacterDisplay> players = new Array<>();

    public UIRenderSystem(Engine engine, SpriteBatch sb, BitmapFont font)
    {
        super(engine);
        this.sb = sb;
        this.font = font;

        this.viewport = new ExtendViewport(1280, 720);
        this.camera = (OrthographicCamera)viewport.getCamera();

        registerComponentType(PlayerComponent.class);
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
        String name = play.identifier.replace("characters/", "").replace(".json", "");
        CharacterDisplay portrait = new CharacterDisplay(name, entity.ID);

        if(!players.contains(portrait, false))
        {
            players.add(portrait);
            players.sort();
        }
    }

    @Override
    public void postUpdate()
    {
        sb.begin();
        for(CharacterDisplay player : players)
        {
            font.draw(sb, player.name, -800 + 400 * player.ID, -300);
        }
        sb.end();
    }
}
