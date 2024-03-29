package smashdudes.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.core.AttackResult;
import smashdudes.core.Collisions;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.AttackEvent;
import smashdudes.gameplay.AttackBox;
import smashdudes.gameplay.BodyBox;
import smashdudes.graphics.AnimationFrame;

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
        entities = engine.getEntities(AnimationComponent.class, PositionComponent.class, PlayerComponent.class, AttackableComponent.class);
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

                submitAttackResolutionEntity(entity, other, result);
            }
        }
    }

    private void submitAttackResolutionEntity(Entity attacker, Entity attacked, AttackResult result)
    {
        Entity entity = engine.createEntity();

        engine.addEvent(new AttackEvent(attacker, attacked, result.launchVector, result.collisionArea));

        HitResolutionComponent resolution = new HitResolutionComponent(attacker, attacked, result.launchVector, result.collisionArea, result.stunTime, result.damage, result.knockback, result.hitBlock);
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
        BlockComponent otherBlock = attacked.getComponent(BlockComponent.class);

        AnimationFrame thisCurrentFrame = thisAnim.getCurrentFrame();
        AnimationFrame otherCurrentFrame = otherAnim.getCurrentFrame();

        Array<AttackBox> attackBoxes = thisCurrentFrame.getAttackboxesRelativeTo(thisPos.position, thisPlayer.facingLeft);
        Array<BodyBox> bodyBoxes = otherCurrentFrame.getBodyboxesRelativeTo(otherPos.position, otherPlayer.facingLeft);

        AttackResult result = null;
        float largestCollisionArea = 0;

        BodyBox blockBox = new BodyBox(otherBlock.blockBox);
        blockBox.x = otherPos.position.x + blockBox.x - blockBox.width / 2;
        blockBox.y = otherPos.position.y + blockBox.y - blockBox.height / 2;

        if(otherBlock.isEnabled)
        {
            bodyBoxes.add(blockBox);
        }

        for(AttackBox attack: attackBoxes)
        {
            for(BodyBox body : bodyBoxes)
            {
                if(attack.overlaps(body))
                {
                    Rectangle area = Collisions.calculateOverlapRectangle(body, attack);
                    if(area.area() > largestCollisionArea)
                    {
                        if(result == null) result = new AttackResult();
                        result.launchVector.set(attack.getLaunchVector(thisPlayer.facingLeft));
                        result.collisionArea.set(area);
                        result.damage = attack.power;
                        result.knockback = 1f;
                        result.hitBlock = body.equals(blockBox);
                    }
                }
            }
        }
        return result;
    }
}
