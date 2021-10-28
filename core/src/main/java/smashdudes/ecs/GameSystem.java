package smashdudes.ecs;

import com.badlogic.gdx.utils.Array;

public abstract class GameSystem
{
    public final Engine engine;

    private final Array<Class<? extends Component>> components = new Array<>();

    public GameSystem(Engine engine)
    {
        this.engine = engine;
    }

    public void registerComponentType(Class<? extends Component> clazz)
    {
        if(components.contains(clazz, true)) throw new IllegalStateException("System already has component registered");

        components.add(clazz);
    }

    public void update(float dt)
    {
        preUpdate();

        for(Entity e : this.getEntities())
        {
            updateEntity(e, dt);
        }

        postUpdate();
    }

    public void preUpdate() {}

    public abstract void updateEntity(Entity entity, float dt);

    public void postUpdate() {}


    private Array<Entity> getEntities()
    {
        return engine.getEntities(components);
    }
}
