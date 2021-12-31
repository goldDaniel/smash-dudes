package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.Collisions;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.LandingEvent;
import smashdudes.ecs.events.TerrainCollisionEvent;


public class TerrainCollisionSystem extends GameSystem
{
    private ArrayMap<Entity, Boolean> onGroundLastFrame = new ArrayMap<>();

    private class Terrain
    {
        public PositionComponent pos;
        public StaticTerrainComponent terrain;
    }

    private Array<Terrain> terrain;

    public TerrainCollisionSystem(Engine engine)
    {
        super(engine);
        terrain = new Array<>();
        registerComponentType(PositionComponent.class);
        registerComponentType(TerrainColliderComponent.class);
    }

    @Override
    public void preUpdate()
    {
        Array<Entity> entities = engine.getEntities(PositionComponent.class, StaticTerrainComponent.class);
        for(Entity e : entities)
        {
            Terrain t = new Terrain();
            t.pos = e.getComponent(PositionComponent.class);
            t.terrain = e.getComponent(StaticTerrainComponent.class);

            terrain.add(t);
        }
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        if (!onGroundLastFrame.containsKey(entity))
        {
            onGroundLastFrame.put(entity, false);
        }

        PositionComponent p = entity.getComponent(PositionComponent.class);
        TerrainColliderComponent c = entity.getComponent(TerrainColliderComponent.class);

        Rectangle r0 = new Rectangle();
        r0.x = p.position.x - c.collider.width/ 2 + c.collider.x;
        r0.y = p.position.y - c.collider.height / 2 + c.collider.y;
        r0.width = c.collider.width;
        r0.height = c.collider.height;

        boolean touchedGround = false;

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
                    p.position.y = t.pos.position.y + c.collider.height / 2 - c.collider.y + t.terrain.height / 2;

                    if (!onGroundLastFrame.get(entity))
                    {
                        engine.addEvent(new LandingEvent(entity));
                    }

                    touchedGround = true;
                }
                else if(side == Collisions.CollisionSide.Bottom)
                {
                    p.position.y = t.pos.position.y - c.collider.height / 2 - c.collider.y - t.terrain.height / 2;
                }
                else if(side == Collisions.CollisionSide.Left)
                {
                    p.position.x = t.pos.position.x - c.collider.width / 2 - c.collider.x - t.terrain.width / 2;
                }
                else if(side == Collisions.CollisionSide.Right)
                {
                    p.position.x = t.pos.position.x + c.collider.width / 2 - c.collider.x + t.terrain.width / 2;
                }
            }
        }

        onGroundLastFrame.put(entity, touchedGround);
    }

    @Override
    public void postUpdate()
    {
        terrain.clear();
    }
}
