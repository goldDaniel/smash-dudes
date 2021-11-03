package smashdudes.core.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import org.libsdl.SDL;

public class ControllerInputListener extends ControllerAdapter implements GameInputRetriever
{
    private InputState state = new InputState();

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
        }

        return false;
    }

    @Override
    public boolean buttonUp (Controller controller, int buttonIndex)
    {
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_A)
        {
            state.up = false;
        }

        return false;
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        float deadzone = 0.2f;
        if(Math.abs(value) < deadzone) return false;

        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
        {
            if(value < 0)
            {
                state.left = true;
                state.right = false;
            }
            else
            {
                state.left = false;
                state.right = true;
            }
        }
        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
        {
            if(value < 0)
            {
                state.down = true;
            }
            else
            {
                state.down = false;
            }
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
