package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnAwakeEvent;
import smashdudes.ecs.events.RespawnEvent;

public class RespawnSystem extends GameSystem
{
    private final ArrayMap<PlayerHandle, Array<Component>> removedComponents = new ArrayMap<>();

    public RespawnSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PlayerResetComponent.class);
        registerComponentType(PlayerComponent.class);

        registerEventType(RespawnEvent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerResetComponent res = entity.getComponent(PlayerResetComponent.class);
        PlayerComponent play = entity.getComponent(PlayerComponent.class);

        if(res.currDuration >= res.maxDuration)
        {
            for (Component c : removedComponents.get(play.handle))
            {
                entity.addComponent(c);
            }

            removedComponents.get(play.handle).clear();
            entity.removeComponent(PlayerResetComponent.class);
            engine.addEvent(new RespawnAwakeEvent(entity).setImmediate());
        }

        res.currDuration += dt;
    }

    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof RespawnEvent)
        {
            RespawnEvent e = (RespawnEvent)event;

            e.entity.getComponent(VelocityComponent.class).velocity.setZero();
            e.entity.getComponent(HealthComponent.class).health = 0;

            float maxDuration = 1.5f;
            e.entity.addComponent(new PlayerResetComponent(maxDuration));

            PlayerHandle handle = e.entity.getComponent(PlayerComponent.class).handle;

            if(!removedComponents.containsKey(handle))
            {
                removedComponents.put(handle, new Array<>());
            }
            removedComponents.get(handle).add(e.entity.removeComponent(VelocityComponent.class));
            removedComponents.get(handle).add(e.entity.removeComponent(PlayerControllerComponent.class));
            removedComponents.get(handle).add(e.entity.removeComponent(AttackableComponent.class));
        }
    }
}
