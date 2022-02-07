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
        }

        res.currDuration += dt;
    }

    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof RespawnEvent)
        {
            RespawnEvent e = (RespawnEvent)event;

            VelocityComponent vel = e.entity.getComponent(VelocityComponent.class);
            PlayerComponent play = e.entity.getComponent(PlayerComponent.class);

            vel.velocity.setZero();

            float maxDuration = 1.5f;
            e.entity.addComponent(new PlayerResetComponent(maxDuration));

            if(!removedComponents.containsKey(play.handle))
            {
                removedComponents.put(play.handle, new Array<>());
            }
            removedComponents.get(play.handle).add(e.entity.removeComponent(VelocityComponent.class));
            removedComponents.get(play.handle).add(e.entity.removeComponent(PlayerControllerComponent.class));
        }
    }
}
