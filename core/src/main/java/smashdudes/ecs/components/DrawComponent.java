package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Texture;
import smashdudes.ecs.Component;

public class DrawComponent extends Component
{
    public Texture texture;
    public float scale;

    public boolean facingLeft = false;
}
