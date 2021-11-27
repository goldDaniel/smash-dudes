package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;
import smashdudes.ecs.events.HitEvent;

public class HitDetectionSystem extends GameSystem
{
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
        for (Entity e : entities)
        {
            if (isHit(entity, e))
            {
                Vector2 attackLine = new Vector2(0, 1);
                attackLine.nor();
                engine.addEvent(new HitEvent(entity, e, attackLine));
            }
        }
    }

    private boolean isHit(Entity thisEntity, Entity thatEntity)
    {
        AnimationComponent thisAnim = thisEntity.getComponent(AnimationComponent.class);
        PositionComponent thisPos = thisEntity.getComponent(PositionComponent.class);
        PlayerComponent thisPlay = thisEntity.getComponent(PlayerComponent.class);

        Array<Rectangle> hurtboxes = thisAnim.currentAnimation.getKeyFrame(thisAnim.currentTime).hurtboxes;
        int hurtDir = thisPlay.facingLeft ? -1 : 1;

        AnimationComponent thatAnim = thatEntity.getComponent(AnimationComponent.class);
        PositionComponent thatPos = thatEntity.getComponent(PositionComponent.class);
        PlayerComponent thatPlay = thatEntity.getComponent(PlayerComponent.class);

        Array<Rectangle> hitboxes = thatAnim.currentAnimation.getKeyFrame(thatAnim.currentTime).hitboxes;
        int hitDir = thatPlay.facingLeft ? -1 : 1;

        for (Rectangle hurtbox : hurtboxes)
        {
            Rectangle hurtboxRect = new Rectangle(thisPos.position.x + hurtDir * (hurtbox.x - hurtbox.width / 2), thisPos.position.y + hurtbox.y - hurtbox.height, hurtDir * hurtbox.width, hurtbox.height);
            for (Rectangle hitbox : hitboxes)
            {
                Rectangle hitboxRect = new Rectangle(thatPos.position.x + hitDir * (hitbox.x - hitbox.width / 2), thatPos.position.y + hitDir * (hitbox.y - hitbox.height / 2), hitDir * hitbox.width, hitbox.height);
                if (hitboxRect.overlaps(hurtboxRect))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
