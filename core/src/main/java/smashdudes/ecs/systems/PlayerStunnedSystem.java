package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class PlayerStunnedSystem extends GameSystem
{
    public PlayerStunnedSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerStunnedComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PlayerStunnedComponent stun = entity.getComponent(PlayerStunnedComponent.class);

        entity.removeComponent(PlayerIdleComponent.class);
        entity.removeComponent(PlayerInAirComponent.class);
        entity.removeComponent(PlayerOnGroundAttackStateComponent.class);
        entity.removeComponent(PlayerRunningComponent.class);

        stun.update(dt);
        if(!stun.isStunned())
        {
            System.out.println("in air");
            entity.removeComponent(PlayerStunnedComponent.class);
            entity.addComponent(new PlayerInAirComponent());
        }
    }
}
