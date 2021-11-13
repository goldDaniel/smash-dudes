package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class PlayerRunningSystem extends GameSystem
{
    public PlayerRunningSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerRunningComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(CharacterInputComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v  = entity.getComponent(VelocityComponent.class);

        if(!ci.currentState.left && !ci.currentState.right)
        {
            entity.removeComponent(PlayerRunningComponent.class);
            entity.addComponent(new PlayerIdleComponent());

            PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.idle);
        }
        else
        {
            float speed = 10;
            v.velocity.x = 0;
            if(ci.currentState.left)
            {
                v.velocity.x -= speed;
            }
            if(ci.currentState.right)
            {
                v.velocity.x += speed;
            }
        }
    }
}
