package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class PlayerInAirSystem extends GameSystem
{
    public PlayerInAirSystem(Engine engine)
    {
        super(engine);
        registerComponentType(CharacterInputComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(PlayerInAirComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        
    }
}
