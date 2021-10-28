package smashdudes.ecs.systems;

import com.badlogic.gdx.Gdx;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.GameSystem;
import smashdudes.ecs.components.InputConfigComponent;
import smashdudes.ecs.components.VelocityComponent;

public class InputSystem extends GameSystem
{
    public InputSystem(Engine engine)
    {
        super(engine);
        registerComponentType(InputConfigComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        InputConfigComponent i = entity.getComponent(InputConfigComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);

        v.velocity.x = 0;
        if(Gdx.input.isKeyPressed(i.config.left))
        {
            v.velocity.x--;
        }
        if(Gdx.input.isKeyPressed(i.config.right))
        {
            v.velocity.x++;
        }

        v.velocity.y = 0;
        if(Gdx.input.isKeyPressed(i.config.down))
        {
            v.velocity.y--;
        }
        if(Gdx.input.isKeyPressed(i.config.up))
        {
            v.velocity.y++;
        }

        v.velocity.nor().scl(5f);
    }
}
