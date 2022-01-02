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

            Vector2 dir = hasEntityAttackedOther(entity, other);
            if (dir != null)
            {
                submitAttackEntity(entity, other, dir);
            }
        }
    }

    private void submitAttackEntity(Entity attacker, Entity attacked, Vector2 dir)
    {
        Entity entity = engine.createEntity();

        HitResolutionComponent resolution = new HitResolutionComponent(attacker, attacked, dir.nor(), 0.5f);
        entity.addComponent(resolution);
    }

    private Vector2 hasEntityAttackedOther(Entity attacker, Entity attacked)
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
                    Vector2  dir = new Vector2(hit.x, hit.y);
                    return dir.sub(hit.x, hit.y);
                }
            }
        }
        return null;
    }
}
