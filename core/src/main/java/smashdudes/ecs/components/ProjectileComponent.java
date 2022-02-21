package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;
import smashdudes.ecs.Entity;

public class ProjectileComponent extends Component
{
     public Entity owner;
     public Vector2 dim;

     public float knockback;
     public float damage;

     public float lifeTime;

}
