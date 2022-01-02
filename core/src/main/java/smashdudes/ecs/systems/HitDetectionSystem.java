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
                submitAttackEntity(entity, other);
            }
        }
    }

    private void submitAttackEntity(Entity attacker, Entity attacked)
    {
        Entity entity = engine.createEntity();

        PositionComponent attackerPos = attacker.getComponent(PositionComponent.class);
        PositionComponent attackedPos = attacked.getComponent(PositionComponent.class);

        Vector2 dir = attackedPos.position.cpy().sub(attackerPos.position).nor();

        HitResolutionComponent resolution = new HitResolutionComponent(attacker, attacked, dir);
        entity.addComponent(resolution);
    }

    private boolean hasEntityAttackedOther(Entity attacker, Entity attacked)
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
                if(hit.contains(hurt) || hit.overlaps(hurt))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
