package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;
import smashdudes.core.Projectile;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class GroundAttackSystem extends GameSystem
{
    private Array<PlayerHandle> hasEntered = new Array<>();
    private ArrayMap<PlayerHandle, CharacterInputComponent> removedInputs = new ArrayMap<>();

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
                proj.damage = projectile.damage;
                proj.knockback = projectile.knockback;
                proj.lifeTime = projectile.lifeTime;
                bullet.addComponent(proj);

                PositionComponent pos = new PositionComponent(new Vector2(projectile.pos.x + entity.getComponent(PositionComponent.class).position.x,
                                                                          projectile.pos.y + entity.getComponent(PositionComponent.class).position.y));
                bullet.addComponent(pos);

                VelocityComponent vel = new VelocityComponent();
                int dir = play.facingLeft ? -1 : 1;
                vel.velocity.set(dir * projectile.speed.x, projectile.speed.y);
                bullet.addComponent(vel);

                DrawComponent draw = new DrawComponent();
                draw.texture = projectile.texture;
                bullet.addComponent(draw);

                hasEntered.add(play.handle);
            }
        }

        if(!removedInputs.containsKey(play.handle))
        {
            CharacterInputComponent input = entity.removeComponent(CharacterInputComponent.class);
            removedInputs.put(play.handle, input);
        }

        if(anim.isFinished())
        {
            entity.removeComponent(PlayerOnGroundAttackStateComponent.class);

            entity.addComponent(new PlayerIdleComponent());
            entity.addComponent(removedInputs.removeKey(play.handle));

            hasEntered.removeValue(play.handle, true);
        }
    }
}
