package smashdudes.core.input;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import org.libsdl.SDL;

/**
 * Listens for controller input and allows retrieval of state through the GameInputRetriever interface
 */
public class ControllerInputListener extends ControllerAdapter implements IGameInputListener
{
    private final InputState gameInputState = new InputState();
    private final Controller controller;

    // menu input state
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean confirmPressed = false;
    private boolean cancelPressed = false;

    private boolean leftTriggerDown = false;
    private boolean rightTriggerDown = false;


    public ControllerInputListener(Controller controller)
    {
        controller.addListener(this);
        this.controller = controller;
    }

    private void assignValue(int buttonIndex, boolean value)
    {
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_A)
        {
            gameInputState.up = value;
            confirmPressed = value;
        }
        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_B)
        {
            cancelPressed = value;
        }

        if(buttonIndex == SDL.SDL_CONTROLLER_BUTTON_X)
        {
            gameInputState.punch = value;
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
        if(controller != this.controller) return false;

        assignValue(buttonIndex, true);
        return false;
    }

    @Override
    public boolean buttonUp (Controller controller, int buttonIndex)
    {
        if(controller != this.controller) return false;

        assignValue(buttonIndex, false);
        return false;
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value)
    {
        if(controller != this.controller) return false;

        final float deadZone = 0.4f;
        if(Math.abs(value) < deadZone)
        {
            if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
            {
                gameInputState.left = false;
                gameInputState.right = false;
            }
            else if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
            {
                upPressed = false;
                downPressed = false;
                gameInputState.down = false;
            }
            else if(axisIndex == SDL.SDL_CONTROLLER_AXIS_TRIGGERLEFT)
            {
                leftTriggerDown = false;
            }
            else if(axisIndex == SDL.SDL_CONTROLLER_AXIS_TRIGGERRIGHT)
            {
                rightTriggerDown = false;
            }

            if(!leftTriggerDown && !rightTriggerDown)
            {
                gameInputState.block = false;
            }

            return false;
        }

        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX)
        {
            gameInputState.left = value < 0;
            gameInputState.right = value > 0;
        }
        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY)
        {
            gameInputState.down = value > 0;
        }

        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_TRIGGERRIGHT)
        {
            gameInputState.block = value > 0;
            rightTriggerDown = true;
        }

        if(axisIndex == SDL.SDL_CONTROLLER_AXIS_TRIGGERLEFT)
        {
            gameInputState.block = value > 0;
            leftTriggerDown = true;
        }

        return false;
    }

    @Override
    public boolean getLeft()
    {
        return gameInputState.left;
    }

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
    public boolean block()
    {
        return gameInputState.block;
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
