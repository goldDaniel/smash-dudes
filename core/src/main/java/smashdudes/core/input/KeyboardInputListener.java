package smashdudes.core.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

/**
 * Listens for keyboard input and allows retrieval of state through the GameInputRetriever interface
 */
public class KeyboardInputListener extends InputAdapter implements IGameInputListener
{

    private final InputConfig config;
    private final InputState gameInputState = new InputState();

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
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
            leftPressed = gameInputState.left = true;
        }
        if(keycode == config.right)
        {
            rightPressed = gameInputState.right = true;
        }
        if(keycode == config.up)
        {
            upPressed = gameInputState.up = true;
        }
        if(keycode == config.down)
        {
            downPressed = gameInputState.down = true;
        }

        if(keycode == config.punch)
        {
            gameInputState.punch = true;
        }

        if(keycode == config.block)
        {
            gameInputState.block = true;
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
            leftPressed = gameInputState.left = false;
        }
        if(keycode == config.right)
        {
            rightPressed = gameInputState.right = false;
        }
        if(keycode == config.up)
        {
            upPressed = gameInputState.up = false;
        }
        if(keycode == config.down)
        {
            downPressed = gameInputState.down = false;
        }

        if(keycode == config.punch)
        {
            gameInputState.punch = false;
        }

        if(keycode == config.block)
        {
            gameInputState.block = false;
        }

        return false;
    }

    @Override
    public boolean getLeft() { return gameInputState.left; }

    @Override
    public boolean getRight()
    {
        return gameInputState.right;
    }

    @Override
    public boolean getUp()
    {
        return gameInputState.up;
    }

    @Override
    public boolean getDown()
    {
        return gameInputState.down;
    }

    @Override
    public boolean punch()
    {
        return gameInputState.punch;
    }

    @Override
    public boolean block(){return gameInputState.block;}

    @Override
    public boolean leftPressed()
    {
        boolean result = leftPressed;
        leftPressed = false;
        return result;
    }

    @Override
    public boolean rightPressed()
    {
        boolean result = rightPressed;
        rightPressed = false;
        return result;
    }

    @Override
    public boolean upPressed()
    {
        boolean result = upPressed;
        upPressed = false;
        return result;
    }

    @Override
    public boolean downPressed()
    {
        boolean result = downPressed;
        downPressed = false;
        return result;
    }

    @Override
    public boolean confirmPressed()
    {
        boolean result = confirmPressed;
        confirmPressed = false;
        return result;
    }

    @Override
    public boolean cancelPressed()
    {
        boolean result = cancelPressed;
        cancelPressed = false;
        return result;
    }

    @Override
    public InputDeviceType getDeviceType()
    {
        return InputDeviceType.Keyboard;
    }
}
