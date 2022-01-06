package smashdudes.core.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Listens for keyboard input and allows retrieval of state through the GameInputRetriever interface
 */
public class KeyboardInputListener extends InputAdapter implements IGameInputRetriever, IMenuInputRetriever
{

    private final InputConfig config;
    private boolean confirmPressed = false;
    private boolean cancelPressed = false;
    private ArrayMap<Integer, Boolean> keyState = new ArrayMap<>();

    public KeyboardInputListener(InputConfig config)
    {
        this.config = config;

        keyState.put(config.left, false);
        keyState.put(config.right, false);
        keyState.put(config.up, false);
        keyState.put(config.down, false);

        keyState.put(config.punch, false);
        keyState.put(config.special, false);
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if(keyState.containsKey(keycode))
        {
            keyState.put(keycode, true);
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
        if(keyState.containsKey(keycode))
        {
            keyState.put(keycode, false);
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
    public boolean getLeft() { return keyState.get(config.left); }

    @Override
    public boolean getRight()
    {
        return keyState.get(config.right);
    }

    @Override
    public boolean getUp()
    {
        return keyState.get(config.up);
    }

    @Override
    public boolean getDown()
    {
        return keyState.get(config.down);
    }

    @Override
    public boolean punch()
    {
        return keyState.get(config.punch);
    }

    @Override
    public boolean special()
    {
        return keyState.get(config.special);
    }

    @Override
    public Vector2 getDirection()
    {
        Vector2 result = new Vector2();
        if(keyState.get(config.left)) result.x -= 1;
        if(keyState.get(config.right)) result.x += 1;

        if(keyState.get(config.down)) result.y -= 1;
        if(keyState.get(config.up)) result.y += 1;

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
