package smashdudes.ecs.systems;

import smashdudes.core.WorldUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;

public class BoundsSystem extends GameSystem
{
    public BoundsSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerComponent.class);
        registerComponentType(PositionComponent.class);

        registerEventType(RespawnEvent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerComponent play = entity.getComponent(PlayerComponent.class);
        PositionComponent pos = entity.getComponent(PositionComponent.class);

        if(!WorldUtils.getStageBounds().contains(pos.position))
        {
            play.lives--;
            engine.addEvent(new RespawnEvent(entity, WorldUtils.getRespawnPoint()));
        }

        if(play.lives <= 0)
        {
            engine.destroyEntity(entity);
        }
    }

    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof RespawnEvent)
        {
            RespawnEvent e = (RespawnEvent)event;
            PositionComponent pos = e.entity.getComponent(PositionComponent.class);
            pos.position.set(e.respawnPoint);
        }
    }
}
