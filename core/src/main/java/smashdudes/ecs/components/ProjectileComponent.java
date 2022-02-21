package smashdudes.ecs.components;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;
import smashdudes.ecs.Entity;

public class ProjectileComponent extends Component
{
     public float lifeTime = 2;
     public Entity owner;

     public Vector2 dim;
}
