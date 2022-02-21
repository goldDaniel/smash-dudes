package smashdudes.ecs.systems;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import smashdudes.core.AttackResult;
import smashdudes.core.Collisions;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;

public class ProjectileSystem extends GameSystem
{
    private Array<Entity> attackables = new Array<>();

    public ProjectileSystem(Engine engine)
    {
        super(engine);
        registerComponentType(ProjectileComponent.class);
        registerComponentType(PositionComponent.class);
        registerComponentType(VelocityComponent.class);
    }

    @Override
    public void preUpdate()
    {
        attackables = engine.getEntities(AttackableComponent.class, AnimationComponent.class, PositionComponent.class, PlayerComponent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        ProjectileComponent proj = entity.getComponent(ProjectileComponent.class);
        if(proj.lifeTime <= 0)
        {
            engine.destroyEntity(entity);
        }
        else
        {
            proj.lifeTime -= dt;
            checkProjectileCollisions(entity);
        }
    }

    private void checkProjectileCollisions(Entity projectile)
    {
        ProjectileComponent projBullet = projectile.getComponent(ProjectileComponent.class);
        Vector2 posBullet = projectile.getComponent(PositionComponent.class).position;
        Rectangle projBox = new Rectangle(posBullet.x - projBullet.dim.x / 2, posBullet.y - projBullet.dim.y / 2, projBullet.dim.x, projBullet.dim.y);

        for(Entity target : attackables)
        {
            if(projBullet.owner == target) continue;

            AnimationComponent anim = target.getComponent(AnimationComponent.class);
            PlayerComponent play = target.getComponent(PlayerComponent.class);
            Array<Rectangle> bodyBoxes = anim.getCurrentFrame().getBodyboxesRelativeTo(target.getComponent(PositionComponent.class).position, play.facingLeft);
            for(Rectangle bodyBox : bodyBoxes)
            {
                if(projBox.overlaps(bodyBox))
                {
                    Entity collision = engine.createEntity();
                    collision.addComponent(new HitResolutionComponent(projBullet.owner, target,
                                                                      projectile.getComponent(VelocityComponent.class).velocity.cpy(),
                                                                      Collisions.calculateOverlapRectangle(projBox, bodyBox),
                                                                      1.0f, projBullet.damage, projBullet.knockback));
                    engine.destroyEntity(projectile);
                    break;
                }
            }
        }
    }
}
