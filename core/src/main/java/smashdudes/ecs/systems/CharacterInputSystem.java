package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.SprintingComponent;
import smashdudes.ecs.components.VelocityComponent;

public class CharacterInputSystem extends GameSystem
{
    public CharacterInputSystem(Engine engine)
    {
        super(engine);
        registerComponentType(CharacterInputComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);

        v.velocity.x = 0;
        if(i.currentState.left)
        {
            v.velocity.x--;
        }
        if(i.currentState.right)
        {
            v.velocity.x++;
        }
        v.velocity.x *= 10f;

        if(i.currentState.sprint && entity.getComponent(SprintingComponent.class) == null)
        {
            entity.addComponent(new SprintingComponent(3f));
        }
        else if (entity.getComponent(SprintingComponent.class) != null)
        {
            entity.removeComponent(SprintingComponent.class);
        }
    }
}
