package smashdudes.core.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ArrayMap;
import org.libsdl.SDL;
import smashdudes.core.PlayerHandle;

public class CharacterSelectInputAssigner implements InputProcessor, ControllerListener
{

    @FunctionalInterface
    public interface JoinAction
    {
        void execute(InputDeviceType type, PlayerHandle handle);
    }

    private JoinAction join;

    private PlayerHandle keyboardHandle = null;

    private final ArrayMap<Controller, PlayerHandle>  controllerHandles = new ArrayMap<>();

    public CharacterSelectInputAssigner(JoinAction join)
    {
        this.join = join;
    }

    public Controller GetController(PlayerHandle handle)
    {
        return controllerHandles.getKey(handle, false);
    }

    public void requestLeave(PlayerHandle handle)
    {
        if(keyboardHandle != null && keyboardHandle.equals(handle))
        {
            keyboardHandle = null;
        }
        else
        {
            for(int i = 0; i < controllerHandles.size; ++i)
            {
                PlayerHandle player = controllerHandles.getValueAt(i);
                if(player.equals(handle))
                {
                    controllerHandles.removeValue(player, false);
                    break;
                }
            }
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == Input.Keys.SPACE)
        {
            if(keyboardHandle != null) return false;

            keyboardHandle = new PlayerHandle();
            join.execute(InputDeviceType.Keyboard, keyboardHandle);

            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY)
    {
        return false;
    }

    @Override
    public void connected(Controller controller)
    {

    }

    @Override
    public void disconnected(Controller controller)
    {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        if (buttonCode == SDL.SDL_CONTROLLER_BUTTON_A)
        {
            if(controllerHandles.containsKey(controller)) return false;

            controllerHandles.put(controller, new PlayerHandle());
            join.execute(InputDeviceType.Controller, controllerHandles.get(controller));

            return true;

        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int i)
    {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int i, float v)
    {
        return false;
    }


    public boolean povMoved(Controller controller, int i, PovDirection povDirection)
    {
        return false;
    }

    public boolean xSliderMoved(Controller controller, int i, boolean b)
    {
        return false;
    }

    public boolean ySliderMoved(Controller controller, int i, boolean b)
    {
        return false;
    }

    public boolean accelerometerMoved(Controller controller ,int axis,Vector3 dir)
    {
        return false;
    }
}
