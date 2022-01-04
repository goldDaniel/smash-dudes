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
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);

        AnimationComponent current = entity.getComponent(AnimationComponent.class);
        if(current != container.jumping || current != container.falling)
        {
            entity.removeComponent(AnimationComponent.class);
            if(v.velocity.y > 0)
            {
                entity.addComponent(container.jumping);
            }
            else
            {
                entity.addComponent(container.falling);
            }
        }

        v.velocity.x *= v.deceleration * dt;
        if(ci.currentState.left || ci.currentState.right)
        {
            float speed = v.airSpeed;
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
