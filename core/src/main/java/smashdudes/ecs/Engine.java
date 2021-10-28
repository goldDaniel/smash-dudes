package smashdudes.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.systems.GravitySystem;
import smashdudes.ecs.systems.InputSystem;
import smashdudes.ecs.systems.MovementSystem;
import smashdudes.ecs.systems.RenderSystem;

public class Engine
{
    private Array<Entity> entities = new Array<>();
    private Array<GameSystem> systems = new Array<>();

    private RenderSystem rs;

    public Engine()
    {
        rs = new RenderSystem(this);

        systems.add(new InputSystem(this));
        systems.add(new GravitySystem(this));
        systems.add(new MovementSystem(this));
        systems.add(rs);
    }

    public Entity createEntity()
    {
        Entity e = new Entity();
        entities.add(e);

        return e;
    }


    public Array<Entity> getEntities()
    {
        return entities;
    }

    public void update()
    {
        float dt = Gdx.graphics.getDeltaTime();

        for(GameSystem s : systems)
        {
            s.update(dt);
        }
    }

    public void resize(int w, int h)
    {
        rs.resize(w, h);
    }
}
