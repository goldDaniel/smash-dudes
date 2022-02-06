package smashdudes.ecs.systems;

import smashdudes.core.WorldUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.HealthComponent;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.components.UIComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;

public class BoundySystem extends GameSystem
{
    public BoundySystem(Engine engine)
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

        if(play.lives <= 0)
        {
            entity.removeAllOtherComponents(PlayerComponent.class, HealthComponent.class, UIComponent.class);
        }
        else if(!WorldUtils.getStageBounds().contains(pos.position))
        {
            play.lives--;
            engine.addEvent(new RespawnEvent(entity, WorldUtils.getRespawnPoint()));
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
