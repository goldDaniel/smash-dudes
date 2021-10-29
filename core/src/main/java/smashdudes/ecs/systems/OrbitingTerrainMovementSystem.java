package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DynamicTerrainComponent;
import smashdudes.ecs.components.OrbitingComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.components.VelocityComponent;

public class OrbitingTerrainMovementSystem extends GameSystem
{
    public OrbitingTerrainMovementSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PositionComponent.class);
        registerComponentType(DynamicTerrainComponent.class);
        registerComponentType(VelocityComponent.class);
        registerComponentType(OrbitingComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        DynamicTerrainComponent d = entity.getComponent(DynamicTerrainComponent.class);
        PositionComponent p = entity.getComponent(PositionComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        OrbitingComponent o = entity.getComponent(OrbitingComponent.class);

        d.prevPos.set(p.position);

        Vector2 posRelToOrigin = p.position.sub(o.orbitOrigin);
        posRelToOrigin.nor();

        float angle;

        angle = (float) Math.atan(posRelToOrigin.y / posRelToOrigin.x);

        if (o.orbitXAxis)
        {
            v.velocity.x += -(float)Math.cos(angle) * dt;
        }
        if (o.orbitYAxis)
        {
            v.velocity.y += -(float)Math.sin(angle) * dt;
        }
    }
}
