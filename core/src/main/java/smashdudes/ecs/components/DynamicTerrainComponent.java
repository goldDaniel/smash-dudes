package smashdudes.ecs.components;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Component;

public class DynamicTerrainComponent extends Component
{
    public float width;
    public float height;

    public Vector2 prevPos = new Vector2();
}
