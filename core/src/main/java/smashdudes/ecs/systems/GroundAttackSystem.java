package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class GroundAttackSystem extends GameSystem
{
    public GroundAttackSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerOnGroundAttackStateComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(AnimationComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);

        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        v.velocity.x = 0;

        if(entity.getComponent(AnimationComponent.class) != container.attack_1)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.attack_1);
            container.attack_1.reset();
        }

        if(entity.getComponent(AnimationComponent.class).isFinished())
        {
            entity.removeComponent(PlayerOnGroundAttackStateComponent.class);
            entity.addComponent(new PlayerIdleComponent());
        }
    }
}
