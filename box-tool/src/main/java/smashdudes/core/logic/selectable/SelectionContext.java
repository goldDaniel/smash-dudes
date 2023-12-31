package smashdudes.core.logic.selectable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SelectionContext
{
    private Selectable selected = null;

    private final Array<Selectable> selectables = new Array<>();

    public void clearSelectables()
    {
        selectables.clear();
        selected = null;
    }

    public void addSelectable(Selectable selectable)
    {
        this.selectables.add(selectable);
    }

    public void draw(Vector2 mousePos, ShapeRenderer sh)
    {
        for(Selectable rect : selectables)
        {
            rect.draw(mousePos, sh);
        }
    }

    public void doSelection(Vector2 mouseWorldPos)
    {
        Vector2 mouseDelta = new Vector2();
        mouseDelta.x = Gdx.input.getDeltaX() * 0.005f;
        mouseDelta.y = -Gdx.input.getDeltaY() * 0.005f;

        // attempt selection
        if(selected == null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            for(Selectable selectable : selectables)
            {
                if(selectable.select(mouseWorldPos))
                {
                    selected = selectable;
                }
            }
        }
        else if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            if(selected != null)
            {
                selected.release();
                selected = null;
            }
        }
        else if(selected != null)
        {
            selected.drag(mouseWorldPos, mouseDelta);
        }
    }
}
