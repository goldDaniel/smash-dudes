package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.HitResolutionComponent;
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
        for (Entity other : entities)
        {
            if(entity == other) continue;

            if (hasEntityAttackedOther(entity, other))
            {
                Vector2 attackLine = new Vector2(0, 1);
                attackLine.nor();

                submitAttackEntity(entity, other);
            }
        }
    }

    private void submitAttackEntity(Entity attacker, Entity attacked)
    {
        Entity attackEntry = engine.createEntity();

        HitResolutionComponent resolution = new HitResolutionComponent(attacker, attacked);
        attackEntry.addComponent(resolution);
    }


    private boolean hasEntityAttackedOther(Entity thisEntity, Entity thatEntity)
    {
        PositionComponent thisPos = thisEntity.getComponent(PositionComponent.class);
        PlayerComponent thisPlayer = thisEntity.getComponent(PlayerComponent.class);
        AnimationComponent thisAnim = thisEntity.getComponent(AnimationComponent.class);

        PositionComponent otherPos = thatEntity.getComponent(PositionComponent.class);
        PlayerComponent otherPlayer = thatEntity.getComponent(PlayerComponent.class);
        AnimationComponent otherAnim = thatEntity.getComponent(AnimationComponent.class);

        Array<Rectangle> thisHitboxes = thisAnim.currentAnimation.getKeyFrame(thisAnim.currentTime).hitboxes;
        Array<Rectangle> otherHurtboxes = otherAnim.currentAnimation.getKeyFrame(otherAnim.currentTime).hurtboxes;

        int thisDir = thisPlayer.facingLeft ? -1 : 1;
        int otherDir = otherPlayer.facingLeft ? -1 : 1;

        for(Rectangle hitboxRelative : thisHitboxes)
        {
            Rectangle hitboxAbsolute = new Rectangle();
            hitboxAbsolute.width = thisDir * hitboxRelative.width;
            hitboxAbsolute.height = hitboxRelative.height;
            hitboxAbsolute.x = thisDir * (hitboxRelative.x - hitboxRelative.width / 2)  + thisPos.position.x;
            hitboxAbsolute.y = (hitboxRelative.y - hitboxRelative.height / 2) + thisPos.position.y;

            for(Rectangle hurtboxRelative : otherHurtboxes)
            {
                Rectangle hurtboxAbsolute = new Rectangle();
                hurtboxAbsolute.width = otherDir * hurtboxRelative.width;
                hurtboxAbsolute.height = hurtboxRelative.height;
                hurtboxAbsolute.x = otherDir * (hurtboxRelative.x - hurtboxRelative.width / 2) + otherPos.position.x;
                hurtboxAbsolute.y = (otherPos.position.y - hurtboxRelative.height / 2) + otherPos.position.y;

                if(hitboxAbsolute.overlaps(hurtboxAbsolute))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
