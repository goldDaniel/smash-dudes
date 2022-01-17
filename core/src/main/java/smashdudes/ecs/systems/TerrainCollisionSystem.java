package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.Collisions;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.DebugDrawComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.components.StaticTerrainComponent;
import smashdudes.ecs.components.TerrainColliderComponent;
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

            if(e.hasComponent(DebugDrawComponent.class))
            {
                DebugDrawComponent debug = e.getComponent(DebugDrawComponent.class);

                Rectangle r = new Rectangle();
                r.x = t.pos.position.x - t.terrain.width / 2;
                r.y = t.pos.position.y - t.terrain.height / 2;
                r.width = t.terrain.width;
                r.height = t.terrain.height;

                debug.pushShape(ShapeRenderer.ShapeType.Filled, r, Color.FOREST.cpy().sub(0.1f, 0.1f, 0.1f, 0.0f));
            }
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

        if(entity.hasComponent(DebugDrawComponent.class))
        {
            DebugDrawComponent debug = entity.getComponent(DebugDrawComponent.class);

            Rectangle r = new Rectangle();
            r.x = p.position.x + c.collider.x - c.collider.width / 2;
            r.y = p.position.y + c.collider.y - c.collider.height / 2;
            r.width = c.collider.width;
            r.height = c.collider.height;

            debug.pushShape(ShapeRenderer.ShapeType.Line, r, Color.GOLD);
        }

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
                        Vector2 landingPoint = p.position.cpy().sub(0, r0.height / 2);
                        engine.addEvent(new LandingEvent(entity, landingPoint));
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
