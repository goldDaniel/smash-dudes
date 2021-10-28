package smashdudes.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.systems.*;

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
        systems.add(new TerrainCollisionSystem(this));
        systems.add(new MovementSystem(this));
        systems.add(rs);
    }

    public Entity createEntity()
    {
        Entity e = new Entity();
        entities.add(e);

        return e;
    }

    public Array<Entity> getEntities(Array<Class<? extends Component>> components)
    {
        Array<Entity> result = new Array<>();

        for(Entity entity : entities)
        {
            boolean valid = true;
            for(Class<? extends Component> component : components)
            {
                if(entity.getComponent(component) == null)
                {
                    valid = false;
                }
            }

            if(valid)
            {
                result.add(entity);
            }
        }

        return result;
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
