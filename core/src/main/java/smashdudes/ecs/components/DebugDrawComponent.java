package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Component;

public class DebugDrawComponent extends Component
{
    public Color color = Color.GOLD;
    public Rectangle box = new Rectangle();

    public Array<Rectangle> hitboxes = new Array<>();
    public Array<Rectangle> hurtboxes = new Array<>();

    public boolean facingLeft = false;
}
