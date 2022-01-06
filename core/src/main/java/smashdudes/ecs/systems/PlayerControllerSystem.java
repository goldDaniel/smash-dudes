package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import smashdudes.core.input.InputState;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.components.PlayerControllerComponent;

public class PlayerControllerSystem extends GameSystem
{
    public PlayerControllerSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerControllerComponent.class);
        registerComponentType(CharacterInputComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PlayerControllerComponent pc = entity.getComponent(PlayerControllerComponent.class);
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);

        ci.previousState = ci.currentState;

        InputState nextState = new InputState();
        nextState.left = pc.retriever.getLeft();
        nextState.right = pc.retriever.getRight();
        nextState.up = pc.retriever.getUp();
        nextState.down = pc.retriever.getDown();
        nextState.punch = pc.retriever.punch();
        nextState.special = pc.retriever.special();

        ci.currentState = nextState;
    }
}
