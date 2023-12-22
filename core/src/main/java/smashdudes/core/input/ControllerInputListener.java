package smashdudes.core.input;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import org.libsdl.SDL;

/**
 * Listens for controller input and allows retrieval of state through the GameInputRetriever interface
 */
public class ControllerInputListener extends ControllerAdapter implements IGameInputListener
{
    private InputState gameInputstate = new InputState();


    // menu input state
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean confirmPressed = false;
    private boolean cancelPressed = false;


    public ControllerInputListener(Controller controller)
    {
        controller.addListener(this);
    }

    private void assignValue(int buttonIndex, boolean value)
    {
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_A)
        {
            gameInputstate.up = value;
            confirmPressed = value;
        }
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_B)
        {
            cancelPressed = value;
        }

        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_X)
        {
            gameInputstate.punch = value;
        }

        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_DPAD_LEFT)
        {
            leftPressed = value;
        }
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_DPAD_RIGHT)
        {
            rightPressed = value;
        }
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_DPAD_UP)
        {
            upPressed = value;
        }
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_DPAD_DOWN)
        {
            downPressed = value;
        }
    }

    @Override
    public boolean buttonDown (Controller controller, int buttonIndex)
    {
        assignValue(buttonIndex, true);
        return false;
    }

    @Override
    public boolean buttonUp (Controller controller, int buttonIndex)
    {
        assignValue(buttonIndex, false);
        return false;
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        final float deadZone = 0.4f;
        if(Math.abs(value) < deadZone)
        {
            if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
            {
                gameInputstate.left = false;
                gameInputstate.right = false;
            }
            else if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
            {
                upPressed = false;
                downPressed = false;
                gameInputstate.down = false;
            }

            return false;
        }

        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
        {
            gameInputstate.left = value < 0;
            gameInputstate.right = value > 0;
        }
        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
        {
            gameInputstate.down = value > 0;
        }


        return false;
    }

    @Override
    public boolean getLeft()
    {
        return gameInputstate.left;
    }

    @Override
    public boolean getRight()
    {
        return gameInputstate.right;
    }

    @Override
    public boolean getUp()
    {
        return gameInputstate.up;
    }

    @Override
    public boolean getDown()
    {
        return gameInputstate.down;
    }

    @Override
    public boolean punch()
    {
        return gameInputstate.punch;
    }

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
        return InputDeviceType.Controller;
    }
}
