package smashdudes.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import org.libsdl.SDL;
import smashdudes.core.PlayerHandle;

public class GameInputAssigner
{
    private GameInputHandler inputHandler = new GameInputHandler();
    private Array<Controller> boundControllers = new Array<>();
    private boolean keyboardBound = false;


    private ControllerAdapter controllerListener = new ControllerAdapter()
    {
        @Override
        public boolean buttonUp(Controller controller, int buttonCode)
        {
            if(!boundControllers.contains(controller, true))
            {
                if(buttonCode == SDL.SDL_CONTROLLER_BUTTON_A)
                {
                    boundControllers.add(controller);
                    inputHandler.register(new PlayerHandle(), new ControllerInputListener(controller));

                    return true;
                }
            }

            return false;
        }
    };

    private InputAdapter inputListener = new InputAdapter()
    {
        public boolean keyUp (int keycode)
        {
            if(!keyboardBound)
            {
                if(keycode == Input.Keys.SPACE)
                {
                    InputConfig c = new InputConfig(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S);
                    inputHandler.register(new PlayerHandle(), new KeyboardInputListener(c));

                    keyboardBound = true;
                }

            }

            return false;
        }
    };

    public void startListening()
    {
        Gdx.input.setInputProcessor(inputListener);
        Controllers.addListener(controllerListener);
    }

    public void stopListening()
    {
        Gdx.input.setInputProcessor(null);
        Controllers.removeListener(controllerListener);
    }


    public GameInputHandler getGameInputHandler()
    {
        return inputHandler;
    }

    public Iterable<PlayerHandle> getPlayerHandles()
    {
        return inputHandler.getHandles();
    }
}
