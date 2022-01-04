package smashdudes.ecs.components;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;

public class VelocityComponent extends Component
{
    public Vector2 velocity = new Vector2();

    public float runSpeed;
    public float airSpeed;
    public float deceleration;
}
