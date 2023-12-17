package smashdudes.core.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import org.libsdl.SDL;
import smashdudes.core.AudioResources;

public class MenuNavigator implements InputProcessor, ControllerListener
{
    private Group buttonGroup;
    private int currentButtonIndex = -1;

    public void setButtonGroup(Group buttonGroup)
    {
        this.buttonGroup = buttonGroup;
    }

    private void unhoverButton(int index)
    {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.touchUp);
        event.setStageX(Integer.MIN_VALUE);
        event.setStageY(Integer.MIN_VALUE);
        buttonGroup.getChild(index).fire(event);
    }

    private void hoverButton(int index)
    {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.touchDown);
        buttonGroup.getChild(index).fire(event);
    }

    private void clickButton(int index)
    {
        InputEvent event = new InputEvent();
        event.setType(InputEvent.Type.touchUp);
        buttonGroup.getChild(index).fire(event);
        AudioResources.getSoundEffect("audio/ui/select.ogg").play();
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if(buttonGroup == null) return false;

        if(currentButtonIndex == -1)
        {
            currentButtonIndex = 0;
        }

        unhoverButton(currentButtonIndex);
        if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            currentButtonIndex = (currentButtonIndex + 1) % buttonGroup.getChildren().size;
            AudioResources.getSoundEffect("audio/ui/navigate.ogg").play();
        }
        else if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            currentButtonIndex--;
            if(currentButtonIndex < 0)
            {
                currentButtonIndex = buttonGroup.getChildren().size - 1;
            }
            AudioResources.getSoundEffect("audio/ui/navigate.ogg").play();
        }
        hoverButton(currentButtonIndex);

        if(keycode == Input.Keys.ENTER || keycode == Input.Keys.SPACE)
        {
            clickButton(currentButtonIndex);
            AudioResources.getSoundEffect("audio/ui/select.ogg").play();
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
        if(buttonGroup == null) return false;

        currentButtonIndex = -1;
        for(int i = 0; i < buttonGroup.getChildren().size; ++i)
        {
            if(buttonGroup.getChild(i).hit(screenX, screenY, true) != null)
            {
                currentButtonIndex = i;
                hoverButton(i);
                AudioResources.getSoundEffect("audio/ui/navigate.ogg").play();
            }
            else
            {
                unhoverButton(i);
            }

        }
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
        if(buttonGroup == null) return false;

        if(currentButtonIndex == -1)
        {
            currentButtonIndex = 0;
        }

        unhoverButton(currentButtonIndex);
        if(buttonCode == SDL.SDL_CONTROLLER_BUTTON_DPAD_DOWN)
        {
            currentButtonIndex = (currentButtonIndex + 1) % buttonGroup.getChildren().size;
            AudioResources.getSoundEffect("audio/ui/navigate.ogg").play();
        }
        else if(buttonCode == SDL.SDL_CONTROLLER_BUTTON_DPAD_UP)
        {
            currentButtonIndex--;
            if(currentButtonIndex < 0)
            {
                currentButtonIndex = buttonGroup.getChildren().size - 1;
            }
            AudioResources.getSoundEffect("audio/ui/navigate.ogg").play();
        }
        hoverButton(currentButtonIndex);

        if(buttonCode == SDL.SDL_CONTROLLER_BUTTON_A)
        {
            clickButton(currentButtonIndex);
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

    @Override
    public boolean povMoved(Controller controller, int i, PovDirection povDirection)
    {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int i, boolean b)
    {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int i, boolean b)
    {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int i, Vector3 vector3)
    {
        return false;
    }
}
