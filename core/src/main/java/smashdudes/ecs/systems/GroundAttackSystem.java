package smashdudes.ecs.systems;

import smashdudes.core.AttackType;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class GroundAttackSystem extends GameSystem
{
    public GroundAttackSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerOnGroundAttackStateComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(AnimationComponent.class);
        registerComponentType(VelocityComponent.class);
        registerComponentType(PositionComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerOnGroundAttackStateComponent attack = entity.getComponent(PlayerOnGroundAttackStateComponent.class);
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        AnimationComponent animation = entity.getComponent(AnimationComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        if(attack.type == AttackType.NEUTRAL_ATTACK)
        {
            velocity.velocity.x = 0;

            animation = container.attack_1;
        }

        if(attack.type == AttackType.NEUTRAL_SPECIAL_ATTACK)
        {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            velocity.velocity.x = 0;

            animation = container.special_1;

            Entity proj = engine.createEntity();

            DrawComponent draw = new DrawComponent();
            proj.addComponent(draw);

            PositionComponent pos = new PositionComponent(position.position);
            proj.addComponent(pos);

            VelocityComponent vel = new VelocityComponent();
            vel.velocity.set(40, 0);
            proj.addComponent(vel);

            ProjectileComponent pro = new ProjectileComponent();
            pro.maxLiveTime = 10;
            pro.currLiveTime = 0;
            pro.ownerID = entity.ID;
        }

        if (entity.getComponent(AnimationComponent.class) != animation)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(animation);
            animation.reset();
        }

        if (entity.getComponent(AnimationComponent.class).isFinished())
        {
            entity.removeComponent(PlayerOnGroundAttackStateComponent.class);
            entity.addComponent(new PlayerIdleComponent());
        }
    }
}
