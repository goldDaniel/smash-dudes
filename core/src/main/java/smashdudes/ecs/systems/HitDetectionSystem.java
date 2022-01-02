package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class HitDetectionSystem extends GameSystem
{
    private class AttackResult
    {
        public Vector2 direction = new Vector2();
        public Vector2 collisionPoint = new Vector2();
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
                submitAttackEntity(entity, other, result.direction);
            }
        }
    }

    private void submitAttackEntity(Entity attacker, Entity attacked, Vector2 dir)
    {
        Entity entity = engine.createEntity();

        HitResolutionComponent resolution = new HitResolutionComponent(attacker, attacked, dir.nor(), 0.5f);
        entity.addComponent(resolution);
    }

    private AttackResult hasEntityAttackedOther(Entity attacker, Entity attacked)
    {
        PositionComponent thisPos = attacker.getComponent(PositionComponent.class);
        PlayerComponent thisPlayer = attacker.getComponent(PlayerComponent.class);
        AnimationComponent thisAnim = attacker.getComponent(AnimationComponent.class);

        PositionComponent otherPos = attacked.getComponent(PositionComponent.class);
        PlayerComponent otherPlayer = attacked.getComponent(PlayerComponent.class);
        AnimationComponent otherAnim = attacked.getComponent(AnimationComponent.class);

        AnimationComponent.AnimationFrame thisCurrentFrame = thisAnim.getCurrentFrame();
        AnimationComponent.AnimationFrame otherCurrentFrame = otherAnim.getCurrentFrame();

        Array<Rectangle> hitboxes = thisCurrentFrame.getHitboxesRelativeTo(thisPos.position, thisPlayer.facingLeft);
        for(Rectangle hit: hitboxes)
        {
            Array<Rectangle> hurtboxes = otherCurrentFrame.getHurtboxesRelativeTo(otherPos.position, otherPlayer.facingLeft);
            for(Rectangle hurt : hurtboxes)
            {
                if(hit.overlaps(hurt))
                {
                    AttackResult result = new AttackResult();

                    result.direction.set(hit.x, hit.y);
                    result.direction.sub(hit.x, hit.y);

                    Rectangle overlappingArea = calculateOverlapRectangle(hurt, hit);
                    result.collisionPoint = overlappingArea.getCenter(result.collisionPoint);

                    if(attacker.hasComponent(DebugDrawComponent.class))
                    {
                        attacker.getComponent(DebugDrawComponent.class).pushShape(ShapeRenderer.ShapeType.Filled, overlappingArea, Color.WHITE);
                    }
                }
            }
        }
        return null;
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
