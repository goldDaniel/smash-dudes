package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Texture;
import smashdudes.ecs.Component;

public class UIComponent extends Component
{
    public final Texture tex;

    public UIComponent(Texture tex)
    {
        this.tex = tex;
    }
}
