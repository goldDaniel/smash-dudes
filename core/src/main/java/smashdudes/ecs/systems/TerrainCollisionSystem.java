package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.core.Collisions;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.TerrainCollisionEvent;


public class TerrainCollisionSystem extends GameSystem
{
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
        Array<Class<? extends Component>> types = new Array<>();
        types.add(PositionComponent.class);
        types.add(StaticTerrainComponent.class);
        Array<Entity> terrainEntities = engine.getEntities(types);

        for(Entity e : terrainEntities)
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
        PositionComponent p = entity.getComponent(PositionComponent.class);
        TerrainColliderComponent c = entity.getComponent(TerrainColliderComponent.class);

        Rectangle r0 = new Rectangle();
        r0.x = p.position.x - c.colliderWidth / 2;
        r0.y = p.position.y - c.colliderHeight / 2;
        r0.width = c.colliderWidth;
        r0.height = c.colliderHeight;

        boolean onGround = false;
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
                    onGround = true;
                    p.position.y = t.pos.position.y + c.colliderHeight / 2 + t.terrain.height / 2;
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

        if(onGround)
        {
            if(entity.getComponent(OnGroundComponent.class) == null)
            {
                entity.addComponent(new OnGroundComponent());
            }
        }
        else
        {
            if(entity.getComponent(OnGroundComponent.class) != null)
            {
                entity.removeComponent(OnGroundComponent.class);
            }
        }
    }

    @Override
    public void postUpdate()
    {
        terrain.clear();
    }
}
