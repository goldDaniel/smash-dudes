package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;
import smashdudes.core.Projectile;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.JumpEvent;
import smashdudes.gameplay.PlayerState;

public class PlayerStateSystem extends GameSystem
{

    // NOTE (danielg): These are from the old attack system. refactor out
    private Array<PlayerHandle> hasEntered = new Array<>();
    
    public PlayerStateSystem(Engine engine)
    {
        super(engine);
        registerComponentType(PlayerComponent.class);
    }
    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerComponent player = entity.getComponent(PlayerComponent.class);

        switch(player.currentState)
        {
            case Ground_Idle:
                GroundIdle(entity, player, dt);
                break;
            case Ground_Attack:
                GroundAttack(entity, player, dt);
                break;
            case Ground_Stunned:
                break;
            case Ground_Running:
                GroundRunning(entity, player, dt);
                break;
            case Air_Idle:
                AirIdle(entity, player, dt);
                break;
        }
    }

    private void GroundAttack(Entity entity, PlayerComponent player, float dt)
    {
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);

        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        v.velocity.x = 0;

        if(entity.getComponent(AnimationComponent.class) != container.attack_1)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.attack_1);
            container.attack_1.reset();
        }

        AnimationComponent anim = entity.getComponent(AnimationComponent.class);

        if(!hasEntered.contains(player.handle, true))
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
                int dir = player.facingLeft ? -1 : 1;
                vel.velocity.set(dir * projectile.speed.x, projectile.speed.y);
                bullet.addComponent(vel);

                DrawComponent draw = new DrawComponent();
                draw.texture = projectile.texture;
                bullet.addComponent(draw);

                hasEntered.add(player.handle);
            }
        }

        if(anim.isFinished())
        {
            hasEntered.removeValue(player.handle, true);
            player.currentState = PlayerState.Ground_Idle;
        }
    }

    private void GroundRunning(Entity entity, PlayerComponent player, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v  = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        if(entity.getComponent(AnimationComponent.class) != container.running)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.running);
        }

        if(ci.currentState.punch)
        {
            player.currentState = PlayerState.Ground_Attack;
        }
        else if(ci.currentState.up && v.velocity.y == 0)
        {
            v.velocity.y = j.jumpStrength;
            engine.addEvent(new JumpEvent(entity));

            player.currentState = PlayerState.Air_Idle;
        }
        else if( (ci.currentState.left && ci.currentState.right) ||
                !(ci.currentState.left || ci.currentState.right))
        {
            player.currentState = PlayerState.Ground_Idle;
        }
        else
        {
            float speed = v.runSpeed;
            v.velocity.x *= v.deceleration * dt;
            if(ci.currentState.left)
            {
                v.velocity.x -= speed;
            }
            if(ci.currentState.right)
            {
                v.velocity.x += speed;
            }
        }
    }

    private void GroundIdle(Entity entity, PlayerComponent player, float dt)
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        JumpComponent j = entity.getComponent(JumpComponent.class);

        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);
        if(entity.getComponent(AnimationComponent.class) != container.idle)
        {
            entity.removeComponent(AnimationComponent.class);
            entity.addComponent(container.idle);
        }

        v.velocity.x *= v.deceleration * dt;

        if(i.currentState.punch)
        {
            player.currentState = PlayerState.Ground_Attack;
        }
        else if( !(i.currentState.left && i.currentState.right) &&
                (i.currentState.left || i.currentState.right) )
        {
            player.currentState = PlayerState.Ground_Running;
        }
        else if(i.currentState.up)
        {
            v.velocity.y = j.jumpStrength;
            engine.addEvent(new JumpEvent(entity));

            player.currentState = PlayerState.Air_Idle;
        }

    }

    private void AirIdle(Entity entity, PlayerComponent player, float dt)
    {
        CharacterInputComponent ci = entity.getComponent(CharacterInputComponent.class);
        VelocityComponent v = entity.getComponent(VelocityComponent.class);
        PlayerAnimationContainerComponent container = entity.getComponent(PlayerAnimationContainerComponent.class);

        AnimationComponent current = entity.getComponent(AnimationComponent.class);
        if(current != container.jumping || current != container.falling)
        {
            entity.removeComponent(AnimationComponent.class);
            if(v.velocity.y > 0)
            {
                entity.addComponent(container.jumping);
            }
            else
            {
                entity.addComponent(container.falling);
            }
        }

        v.velocity.x *= v.deceleration * dt;
        if(ci.currentState.left || ci.currentState.right)
        {
            float speed = v.airSpeed;
            if(ci.currentState.left)
            {
                v.velocity.x -= speed;
            }
            if(ci.currentState.right)
            {
                v.velocity.x += speed;
            }
        }
    }
}
