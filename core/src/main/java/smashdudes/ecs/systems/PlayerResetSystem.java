package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PlayerControllerComponent;
import smashdudes.ecs.components.PlayerInAirComponent;

public class PlayerResetSystem extends GameSystem
{
    public PlayerResetSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PlayerComponent.class);
        registerComponentType(PlayerControllerComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        if(!entity.hasComponent(PlayerInAirComponent.class))
        {
            entity.addComponent(new PlayerInAirComponent());
        }
    }
}
