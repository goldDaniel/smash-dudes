package smashdudes.core.input;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.math.Vector2;
import org.libsdl.SDL;
import smashdudes.core.input.InputState;

/**
 * Listens for controller input and allows retrieval of state through the GameInputRetriever interface
 */
public class ControllerInputListener extends ControllerAdapter implements IGameInputRetriever, IMenuInputRetriever
{
    private InputState state = new InputState();

    private Vector2 menuDir = new Vector2();
    private boolean confirmPressed = false;
    private boolean cancelPressed = false;


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
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_B)
        {
            cancelPressed = true;
        }

        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_X)
        {
            state.punch = true;
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
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_B)
        {
            cancelPressed = false;
        }

        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_X)
        {
            state.punch = false;
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
                menuDir.x = 0;
                state.left = false;
                state.right = false;
            }
            else if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
            {
                menuDir.y = 0;
                state.down = false;
            }

            return false;
        }

        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
        {
            menuDir.x = value;
            state.left = value < 0;
            state.right = value > 0;
        }
        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
        {
            menuDir.y = -value;
            state.down = value > 0;
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
    public boolean punch()
    {
        return state.punch;
    }

    @Override
    public Vector2 getDirection()
    {
        return menuDir.nor();
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
