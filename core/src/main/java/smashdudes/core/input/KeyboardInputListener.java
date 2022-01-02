package smashdudes.core.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

/**
 * Listens for keyboard input and allows retrieval of state through the GameInputRetriever interface
 */
public class KeyboardInputListener extends InputAdapter implements IGameInputRetriever, IMenuInputRetriever
{

    private final InputConfig config;
    private final InputState state = new InputState();
    private boolean confirmPressed = false;
    private boolean cancelPressed = false;

    public KeyboardInputListener(InputConfig config)
    {
        this.config = config;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == config.left)
        {
            state.left = true;
        }
        if(keycode == config.right)
        {
            state.right = true;
        }
        if(keycode == config.up)
        {
            state.up = true;
        }
        if(keycode == config.down)
        {
            state.down = true;
        }

        if(keycode == config.punch)
        {
            state.punch = true;
        }

        if(keycode == Input.Keys.SPACE)
        {
            confirmPressed = true;
        }
        if(keycode == Input.Keys.ESCAPE)
        {
            cancelPressed = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if(keycode == config.left)
        {
            state.left = false;
        }
        if(keycode == config.right)
        {
            state.right = false;
        }
        if(keycode == config.up)
        {
            state.up = false;
        }
        if(keycode == config.down)
        {
            state.down = false;
        }

        if(keycode == config.punch)
        {
            state.punch = false;
        }

        if(keycode == Input.Keys.SPACE)
        {
            confirmPressed = false;
        }
        if(keycode == Input.Keys.ESCAPE)
        {
            cancelPressed = false;
        }

        return false;
    }

    @Override
    public boolean getLeft() { return state.left; }

    @Override
    public boolean getRight()
    {
        return state.right;
    }

    @Override
    public boolean getUp()
    {
        return state.up;
    }

    @Override
    public boolean getDown()
    {
        return state.down;
    }

    @Override
    public boolean punch()
    {
        return state.punch;
    }

    @Override
    public Vector2 getDirection()
    {
        Vector2 result = new Vector2();
        if(state.left) result.x -= 1;
        if(state.right) result.x += 1;

        if(state.down) result.y -= 1;
        if(state.up) result.y += 1;

        return result.nor();
    }

    @Override
    public boolean confirmPressed()
    {
        return confirmPressed;
    }

    @Override
    public boolean cancelPressed()
    {
        return cancelPressed;
    }
}
