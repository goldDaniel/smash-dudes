package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import smashdudes.ecs.Component;

public class UIComponent extends Component
{
    public final Texture tex;
    public final Color backgroundColor;

    public UIComponent(Texture tex)
    {
        this(tex, new Color(0,0,0,0));
    }

    public UIComponent(Texture tex, Color color)
    {
        this.tex = tex;
        backgroundColor = new Color(color);
    }
}
