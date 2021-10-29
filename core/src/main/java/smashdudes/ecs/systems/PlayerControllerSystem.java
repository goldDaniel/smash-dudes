package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import smashdudes.core.KeyState;
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

        KeyState nextState = new KeyState();
        nextState.left = Gdx.input.isKeyPressed(pc.config.left);
        nextState.right = Gdx.input.isKeyPressed(pc.config.right);
        nextState.up = Gdx.input.isKeyJustPressed(pc.config.up);
        nextState.down = Gdx.input.isKeyPressed(pc.config.down);

        ci.currentState = nextState;
    }
}
