package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class DirectionSystem extends GameSystem
{
    public DirectionSystem(Engine engine)
    {
        super(engine);

        registerComponentType(CharacterInputComponent.class);
        registerComponentType(PlayerComponent.class);
        registerComponentType(DrawComponent.class);
    }

    public void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        PlayerComponent p = entity.getComponent(PlayerComponent.class);
        DrawComponent d = entity.getComponent(DrawComponent.class);

        if(!(ci.currentState.left && ci.currentState.right))
        {
            if(ci.currentState.left)
            {
                p.facingLeft = true;
            }
            else if(ci.currentState.right)
            {
                p.facingLeft = false;
            }
        }

        d.facingLeft = p.facingLeft;
    }
}
