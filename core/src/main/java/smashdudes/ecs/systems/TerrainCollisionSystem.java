package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.TerrainCollisionEvent;


public class TerrainCollisionSystem extends GameSystem
{
    public enum CollisionSide
    {
        Left,
        Right,
        Top,
        Bottom,
    }

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

        for(Terrain t : terrain)
        {
            Rectangle r1 = new Rectangle();
            r1.x = t.pos.position.x - t.terrain.width / 2;
            r1.y = t.pos.position.y - t.terrain.height / 2;
            r1.width = t.terrain.width;
            r1.height = t.terrain.height;

            if(r0.overlaps(r1))
            {
                CollisionSide side = getCollisionSide(r0, r1);
                engine.addEvent(new TerrainCollisionEvent(entity, side));

                if(side == CollisionSide.Top)
                {
                    p.position.y = t.pos.position.y + c.colliderHeight / 2 + t.terrain.height / 2;
                }
                else if(side == CollisionSide.Bottom)
                {
                    p.position.y = t.pos.position.y - c.colliderHeight / 2 - t.terrain.height / 2;
                }
                else if(side == CollisionSide.Left)
                {
                    p.position.x = t.pos.position.x - c.colliderWidth / 2 - t.terrain.width / 2;
                }
                else if(side == CollisionSide.Right)
                {
                    p.position.x = t.pos.position.x + c.colliderWidth / 2 + t.terrain.width / 2;
                }
            }
            else
            {
                engine.addEvent(new TerrainCollisionEvent(entity, null));
            }

        }
    }

    @Override
    public void postUpdate()
    {
        terrain.clear();
    }

    /**
     * Returns side Rectangle is colliding with relative to r0.
     */
    private static CollisionSide getCollisionSide(Rectangle r0, Rectangle r1)
    {
        CollisionSide result = null;
        //horizontal side
        boolean left = r0.x + r0.width / 2 < r1.x + r1.width / 2;
        //vertical side
        boolean above = r0.y + r0.height / 2 > r1.y + r1.height / 2;

        //holds how deep the r1ect is inside the tile on each axis
        float horizontalDif;
        float verticalDif;

        //determine the differences for depth
        if (left)
        {
            horizontalDif = r0.x + r0.width - r1.x;
        }
        else
        {
            horizontalDif = r1.x + r1.width - r0.x;
        }

        if (above)
        {
            verticalDif = r1.y + r1.height - r0.y;
        }
        else
        {
            verticalDif = r0.y + r0.height - r1.y;
        }

        if (horizontalDif < verticalDif)
        {
            if (left)
            {
                result = CollisionSide.Left;
            }
            else
            {
                result = CollisionSide.Right;
            }
        }
        else if (above)
        {
            result = CollisionSide.Top;
        }
        else
        {
            result = CollisionSide.Bottom;
        }

        return result;
    }
}
