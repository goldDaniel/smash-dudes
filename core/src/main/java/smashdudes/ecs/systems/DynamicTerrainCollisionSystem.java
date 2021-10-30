package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.core.Collisions;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.TerrainCollisionEvent;

public class DynamicTerrainCollisionSystem extends GameSystem
{
    private class Terrain
    {
        public PositionComponent pos;
        public DynamicTerrainComponent terrain;
    }

    private Array<DynamicTerrainCollisionSystem.Terrain> terrain;

    public DynamicTerrainCollisionSystem(Engine engine)
    {
        super(engine);

        terrain = new Array<>();

        registerComponentType(PositionComponent.class);
        registerComponentType(TerrainColliderComponent.class);
        registerComponentType(OnGroundComponent.class);
    }

    @Override
    public void preUpdate()
    {
        Array<Class<? extends Component>> types = new Array<>();
        types.add(PositionComponent.class);
        types.add(DynamicTerrainComponent.class);
        Array<Entity> terrainEntities = engine.getEntities(types);

        for(Entity e : terrainEntities)
        {
            Terrain t = new Terrain();
            t.pos = e.getComponent(PositionComponent.class);
            t.terrain = e.getComponent(DynamicTerrainComponent.class);

            terrain.add(t);
        }
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PositionComponent p = entity.getComponent(PositionComponent.class);
        TerrainColliderComponent c = entity.getComponent(TerrainColliderComponent.class);
        OnGroundComponent g = entity.getComponent(OnGroundComponent.class);

        Rectangle r0 = new Rectangle();
        r0.x = p.position.x - c.colliderWidth / 2;
        r0.y = p.position.y - c.colliderHeight / 2;
        r0.width = c.colliderWidth;
        r0.height = c.colliderHeight;

        for(Terrain t : terrain)
        {
            Rectangle r1 = new Rectangle();
            r1.x = t.pos.position.x - t.terrain.width / 2;
            r1.y = t.pos.position.y - t.terrain.height / 2;
            r1.width = t.terrain.width;
            r1.height = t.terrain.height;

            if(r0.overlaps(r1))
            {
                Collisions.CollisionSide side = Collisions.getCollisionSide(r0, r1);
                engine.addEvent(new TerrainCollisionEvent(entity, side));

                if(side == Collisions.CollisionSide.Top)
                {
                    p.position.y = t.pos.position.y + c.colliderHeight / 2 + t.terrain.height / 2;

                    Vector2 diff = new Vector2().add(t.pos.position).sub(t.terrain.prevPos);
                    p.position = p.position.add(diff);
                    if (!g.isEnabled())
                    {
                        g.enable();
                    }
                }
                else if(side == Collisions.CollisionSide.Bottom)
                {
                    p.position.y = t.pos.position.y - c.colliderHeight / 2 - t.terrain.height / 2;
                }
                else if(side == Collisions.CollisionSide.Left)
                {
                    p.position.x = t.pos.position.x - c.colliderWidth / 2 - t.terrain.width / 2;
                }
                else if(side == Collisions.CollisionSide.Right)
                {
                    p.position.x = t.pos.position.x + c.colliderWidth / 2 + t.terrain.width / 2;
                }
            }
        }
    }

    @Override
    public void postUpdate()
    {
        terrain.clear();
    }

    @Override
    protected void handleEvent(Event event)
    {

    }
}
