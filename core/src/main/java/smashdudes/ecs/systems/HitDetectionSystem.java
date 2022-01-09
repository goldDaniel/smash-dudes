package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.AttackEvent;
import smashdudes.graphics.AnimationFrame;

public class HitDetectionSystem extends GameSystem
{
    private class AttackResult
    {
        public Vector2 direction = new Vector2();
        public Rectangle collisionArea = new Rectangle();

        public float stunTime = 0;
    }

    private Array<Entity> entities = new Array<>();

    public HitDetectionSystem(Engine engine)
    {
        super(engine);

         registerComponentType(AnimationComponent.class);
         registerComponentType(PositionComponent.class);
         registerComponentType(PlayerComponent.class);
    }

    @Override
    protected void preUpdate()
    {
        entities = engine.getEntities(AnimationComponent.class, PositionComponent.class, PlayerComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        for (Entity other : entities)
        {
            if(entity == other) continue;

            AttackResult result = hasEntityAttackedOther(entity, other);
            if (result != null)
            {
                if(entity.hasComponent(DebugDrawComponent.class))
                {
                    entity.getComponent(DebugDrawComponent.class).pushShape(ShapeRenderer.ShapeType.Filled, result.collisionArea, Color.WHITE);
                }

                submitAttackResolutionEntity(entity, other, result.direction, result.collisionArea);
            }
        }
    }

    private void submitAttackResolutionEntity(Entity attacker, Entity attacked, Vector2 dir, Rectangle collisionArea)
    {
        Entity entity = engine.createEntity();

        engine.addEvent(new AttackEvent(attacker, attacked, collisionArea));

        HitResolutionComponent resolution = new HitResolutionComponent(attacker, attacked, dir.nor(), collisionArea, 0.2f);
        entity.addComponent(resolution);
    }

    private AttackResult hasEntityAttackedOther(Entity attacker, Entity attacked)
    {
        AttackResult result = null;

        PositionComponent thisPos = attacker.getComponent(PositionComponent.class);
        PlayerComponent thisPlayer = attacker.getComponent(PlayerComponent.class);
        AnimationComponent thisAnim = attacker.getComponent(AnimationComponent.class);

        PositionComponent otherPos = attacked.getComponent(PositionComponent.class);
        PlayerComponent otherPlayer = attacked.getComponent(PlayerComponent.class);
        AnimationComponent otherAnim = attacked.getComponent(AnimationComponent.class);

        AnimationFrame thisCurrentFrame = thisAnim.getCurrentFrame();
        AnimationFrame otherCurrentFrame = otherAnim.getCurrentFrame();

        Array<Rectangle> attackboxes = thisCurrentFrame.getAttackboxesRelativeTo(thisPos.position, thisPlayer.facingLeft);
        for(Rectangle attack: attackboxes)
        {
            Array<Rectangle> bodyboxes = otherCurrentFrame.getBodyboxesRelativeTo(otherPos.position, otherPlayer.facingLeft);
            for(Rectangle body : bodyboxes)
            {
                if(attack.overlaps(body))
                {
                    AttackResult attackRes = new AttackResult();

                    attackRes.direction.set(body.x - body.width / 2, body.y - body.height / 2).sub(attack.x - attack.width / 2, attack.y - attack.height / 2).nor();
                    attackRes.collisionArea.set(calculateOverlapRectangle(body, attack));

                    if(result != null)
                    {
                        if(attackRes.collisionArea.area() > result.collisionArea.area())
                        {
                            result = attackRes;
                        }
                    }
                    else
                    {
                        result = attackRes;
                    }
                }
            }
        }
        return result;
    }

    private Rectangle calculateOverlapRectangle(Rectangle a, Rectangle b)
    {
        float left = Math.max(a.x, b.x);
        float right = Math.min(a.x + a.width, b.x + b.width);

        float top = Math.max(a.y, b.y);
        float bottom = Math.min(a.y + a.height, b.y + b.height);


        return new Rectangle(left, top, right - left, bottom - top);
    }
}
