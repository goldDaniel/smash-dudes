package smashdudes.ecs.systems;

import com.badlogic.gdx.math.MathUtils;
import org.graalvm.compiler.loop.MathUtil;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.GravityComponent;
import smashdudes.ecs.components.JumpComponent;
import smashdudes.ecs.components.VelocityComponent;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.TerrainCollisionEvent;

public class GravitySystem extends GameSystem
{
    private final float terminalVelocity = -50f;

    public GravitySystem(Engine engine)
    {
        super(engine);
        registerComponentType(VelocityComponent.class);
        registerComponentType(GravityComponent.class);

        registerEventType(TerrainCollisionEvent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        GravityComponent g = entity.getComponent(GravityComponent.class);

        v.velocity.y -= g.gravityStrength * dt;
        MathUtils.clamp(v.velocity.y, terminalVelocity, Float.MAX_VALUE);
    }


    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof TerrainCollisionEvent)
        {
            TerrainCollisionEvent e = (TerrainCollisionEvent)event;

            if(e.collisionSide == TerrainCollisionSystem.CollisionSide.Top ||
               e.collisionSide == TerrainCollisionSystem.CollisionSide.Bottom)
            {
                VelocityComponent v = e.entity.getComponent(VelocityComponent.class);
                v.velocity.y = 0;
            }
        }
    }
}