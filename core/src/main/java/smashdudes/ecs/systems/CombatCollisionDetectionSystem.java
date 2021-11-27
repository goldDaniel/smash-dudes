package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.PlayerComponent;
import smashdudes.ecs.components.PositionComponent;

public class CombatCollisionDetectionSystem extends GameSystem
{

    private Array<Entity> entities;


    public CombatCollisionDetectionSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PositionComponent.class);
        registerComponentType(PlayerComponent.class);
        registerComponentType(AnimationComponent.class);
    }

    @Override
    protected void preUpdate()
    {
        entities = engine.getEntities(PositionComponent.class, PlayerComponent.class, AnimationComponent.class);
    }

    @Override
    protected void updateEntity(Entity entity, float dt)
    {
        for(Entity other : entities)
        {
            if(other == entity) continue;


            if(hasAttackedOtherEntity(entity, other))
            {
            }
        }
    }

    private boolean hasAttackedOtherEntity(Entity entity, Entity other)
    {
        PositionComponent thisPos = entity.getComponent(PositionComponent.class);
        PlayerComponent thisPlayer = entity.getComponent(PlayerComponent.class);
        AnimationComponent thisAnim = entity.getComponent(AnimationComponent.class);

        PositionComponent otherPos = other.getComponent(PositionComponent.class);
        PlayerComponent otherPlayer = other.getComponent(PlayerComponent.class);
        AnimationComponent otherAnim = other.getComponent(AnimationComponent.class);

        Array<Rectangle> thisHitboxes = thisAnim.currentAnimation.getKeyFrame(thisAnim.currentTime).hitboxes;
        Array<Rectangle> otherHurtboxes = otherAnim.currentAnimation.getKeyFrame(otherAnim.currentTime).hurtboxes;

        int thisDir = thisPlayer.facingLeft ? -1 : 1;
        int otherDir = otherPlayer.facingLeft ? -1 : 1;

        for(Rectangle hitboxRelative : thisHitboxes)
        {
            Rectangle hitboxAbsolute = new Rectangle(hitboxRelative);
            hitboxAbsolute.x = thisDir * (hitboxRelative.x - hitboxRelative.width / 2)  + thisPos.position.x;
            hitboxAbsolute.y += thisPos.position.y;

            for(Rectangle hurtboxRelative : otherHurtboxes)
            {
                Rectangle hurtboxAbsolute = new Rectangle(hurtboxRelative);
                hurtboxAbsolute.x = otherDir * (hurtboxRelative.x - hurtboxRelative.width / 2) + otherPos.position.x;
                hurtboxAbsolute.y += otherPos.position.y;

                if(hitboxAbsolute.overlaps(hurtboxAbsolute))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
