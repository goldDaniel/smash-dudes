package smashdudes.core.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.math.Vector2;
import org.libsdl.SDL;

/**
 * Listens for controller input and allows retrieval of state through the GameInputRetriever interface
 */
public class ControllerInputListener extends ControllerAdapter implements GameInputRetriever, MenuInputRetriever
{
    private InputState state = new InputState();
    private boolean confirmPressed = false;

    public ControllerInputListener(Controller controller)
    {
        controller.addListener(this);
    }

    @Override
    public boolean buttonDown (Controller controller, int buttonIndex)
    {
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_A)
        {
            state.up = true;
            confirmPressed = true;
        }

        return false;
    }

    @Override
    public boolean buttonUp (Controller controller, int buttonIndex)
    {
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_A)
        {
            state.up = false;
            confirmPressed = false;
        }

        return false;
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        float deadzone = 0.2f;
        if(Math.abs(value) < deadzone)
        {
            if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
            {
                state.left = false;
                state.right = false;
            }
            else if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
            {
                state.down = false;
                state.up = false;
            }

            return false;
        }

        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
        {
            state.left = value < 0;
            state.right = value > 0;
        }
        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
        {
            state.down = value > 0;
            state.up = value < 0;
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
}
