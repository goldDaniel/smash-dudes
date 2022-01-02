package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

import java.lang.reflect.GenericArrayType;

public class PlayerIdleSystem extends GameSystem
{
    public PlayerIdleSystem(Engine engine)
    {
        super(engine);
        registerComponentType(CharacterInputComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(PlayerIdleComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);

        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        if(entity.getComponent(AnimationComponent.class) != container.idle)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.idle);
        }

        v.velocity.x *= 40 * dt;
        if(i.currentState.left || i.currentState.right)
        {
            entity.removeComponent(PlayerIdleComponent.class);
            entity.addComponent(new PlayerRunningComponent());
        }
    }
}
