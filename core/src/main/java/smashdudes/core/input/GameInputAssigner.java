package smashdudes.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import org.libsdl.SDL;
import smashdudes.core.PlayerHandle;

/**
 * This class is responsible for mapping input listeners to player handles
 *
 * Each player will have its own input listener. Only one player can use the keyboard
 * and every other player must use a controller
 */
public class GameInputAssigner
{
    //handles our mappings between player handles and the input handlers
    private GameInputHandler gameInputHandler = new GameInputHandler();

    //keeps track of controllers we have already assigned to player
    private Array<Controller> boundControllers = new Array<>();

    //lets us know if the keyboard has been assigned to a player
    private boolean keyboardBound = false;

    //this listens for controller input. If the A button is pressed, we create a controller input listener
    //for the controller that just lifted the A button, and map it to a new player handle
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
                    gameInputHandler.register(new PlayerHandle(), new ControllerInputListener(controller));

                    return true;
                }
            }

            return false;
        }
    };

    //this listens for keyboard input. If the space key is pressed, we create a controller input listener
    //for the controller that just lifted the space key, and map it to a new player handle
    private InputAdapter inputListener = new InputAdapter()
    {
        @Override
        public boolean keyUp (int keycode)
        {
            if(!keyboardBound)
            {
                if(keycode == Input.Keys.SPACE)
                {
                    InputConfig c = new InputConfig(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.S);
                    gameInputHandler.register(new PlayerHandle(), new KeyboardInputListener(c));

                    keyboardBound = true;

                    return true;
                }

            }

            return false;
        }
    };

    /**
     * attaches our listeners to libGDX to receive input events
     */
    public void startListening()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer(inputListener, gameInputHandler.getInputProcessor()));
        Controllers.addListener(controllerListener);
    }

    /**
     * stops listeners from receiving input events
     */
    public void stopListening()
    {
        Gdx.input.setInputProcessor(null);
        Controllers.removeListener(controllerListener);
    }

    public IMenuInputRetriever getMenuInput(PlayerHandle p)
    {
        return (IMenuInputRetriever)gameInputHandler.getGameInput(p);
    }

    public GameInputHandler getGameInputHandler()
    {
        return gameInputHandler;
    }


    public Iterable<PlayerHandle> getPlayerHandles()
    {
        return gameInputHandler.getHandles();
    }
}
