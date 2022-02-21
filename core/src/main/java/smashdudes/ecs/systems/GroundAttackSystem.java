package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.core.PlayerHandle;
import smashdudes.core.Projectile;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class GroundAttackSystem extends GameSystem
{
    private Array<PlayerHandle> hasEntered = new Array<>();

    public GroundAttackSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerOnGroundAttackStateComponent.class);
        registerComponentType(PlayerAnimationContainerComponent.class);
        registerComponentType(PlayerComponent.class);
        registerComponentType(AnimationComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        PlayerComponent play = entity.getComponent(PlayerComponent.class);

        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        v.velocity.x = 0;

        if(entity.getComponent(AnimationComponent.class) != container.attack_1)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.attack_1);
            container.attack_1.reset();
        }

        AnimationComponent anim = entity.getComponent(AnimationComponent.class);

        if(!hasEntered.contains(play.handle, true))
        {
            for (Projectile projectile : anim.getCurrentFrame().projectiles)
            {
                Entity bullet = engine.createEntity();

                ProjectileComponent proj = new ProjectileComponent();
                proj.owner = entity;
                proj.dim = projectile.dim.cpy();
                bullet.addComponent(proj);

                PositionComponent pos = new PositionComponent(new Vector2(projectile.pos.x + entity.getComponent(PositionComponent.class).position.x,
                                                                          projectile.pos.y + entity.getComponent(PositionComponent.class).position.y));
                bullet.addComponent(pos);

                VelocityComponent vel = new VelocityComponent();
                int dir = play.facingLeft ? -1 : 1;
                vel.velocity = new Vector2(dir * projectile.speed.x, projectile.speed.y);
                bullet.addComponent(vel);

                bullet.addComponent(new DrawComponent());
                hasEntered.add(play.handle);
            }
        }

        if(anim.isFinished())
        {
            entity.removeComponent(PlayerOnGroundAttackStateComponent.class);
            entity.addComponent(new PlayerIdleComponent());
            hasEntered.removeValue(play.handle, true);
        }
    }
}
