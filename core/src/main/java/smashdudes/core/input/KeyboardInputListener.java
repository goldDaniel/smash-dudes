package smashdudes.core.input;

import com.badlogic.gdx.InputAdapter;

/**
 * Listens for keyboard input and allows retrieval of state through the GameInputRetriever interface
 */
public class KeyboardInputListener extends InputAdapter implements GameInputRetriever
{

    private final InputConfig config;
    private final InputState state = new InputState();

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

        return false;
    }

    @Override
    public boolean getLeft()
    {
        return state.left;
    }

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
}
