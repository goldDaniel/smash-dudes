package smashdudes.ecs.components;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;

public class OrbitingComponent extends Component
{
    public Vector2 orbitOrigin = new Vector2();
    public float orbitSpeed;
}
