package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.graphics.RenderPass;


public class PlayerStunnedSystem extends GameSystem
{
    public PlayerStunnedSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerStunnedComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        PlayerStunnedComponent stun = entity.getComponent(PlayerStunnedComponent.class);

        entity.removeComponent(PlayerIdleComponent.class);
        entity.removeComponent(PlayerInAirComponent.class);
        entity.removeComponent(PlayerOnGroundAttackStateComponent.class);
        entity.removeComponent(PlayerRunningComponent.class);

        stun.update(dt);
        if(!stun.isStunned())
        {
            entity.removeComponent(PlayerStunnedComponent.class);
            entity.addComponent(new PlayerInAirComponent());
            if(entity.hasComponent(DrawComponent.class))
            {
                DrawComponent d = entity.getComponent(DrawComponent.class);
                d.pass = RenderPass.Default;
                d.getColor().set(Color.WHITE);
            }
        }
        else
        {
            if(entity.hasComponent(DrawComponent.class))
            {
                DrawComponent d = entity.getComponent(DrawComponent.class);
                d.pass = RenderPass.Stunned;

                Color outputColor = Color.RED.cpy();
                outputColor.lerp(Color.WHITE, stun.getPercentageComplete());

                d.getColor().set(outputColor);
                d.getColor().a = stun.getPercentageComplete();
            }
        }
    }
}
