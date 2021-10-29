package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AIControllerComponent;
import smashdudes.ecs.components.CharacterInputComponent;

public class AIControllerSystem extends GameSystem
{
    public AIControllerSystem(Engine engine)
    {
        super(engine);

        registerComponentType(AIControllerComponent.class);
        registerComponentType(CharacterInputComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        ci.currentState.up = true;
    }
}
