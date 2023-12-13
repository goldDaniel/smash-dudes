package smashdudes.ecs.systems;

import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.events.Event;

public abstract class GameSystem extends ISystem
{
    public GameSystem(Engine engine)
    {
        super(engine);
    }

    protected void preUpdate() {}

    protected void updateEntity(Entity entity, float dt) { }

    protected void postUpdate() {}

    public final void update(float dt)
    {
        preUpdate();

        for(Entity e : getEntities())
        {
            updateEntity(e, dt);
        }

        postUpdate();
    }
}
