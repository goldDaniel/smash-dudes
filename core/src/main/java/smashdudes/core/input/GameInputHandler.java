package smashdudes.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;

public class GameInputHandler
{
    private ArrayMap<PlayerHandle, GameInputRetriever> retrievers = new ArrayMap<>();

    private InputMultiplexer multiplexer = new InputMultiplexer();

    public GameInputHandler()
    {
        Gdx.input.setInputProcessor(multiplexer);
    }

    public GameInputRetriever getGameInput(PlayerHandle handle)
    {
        if(!retrievers.containsKey(handle)) throw new IllegalStateException("Player does not have input handler!");

        return retrievers.get(handle);
    }

    public void register(PlayerHandle handle, KeyboardInputListener inputListener)
    {
        retrievers.put(handle, inputListener);
        multiplexer.addProcessor(inputListener);
    }

    public void register(PlayerHandle handle, ControllerInputListener inputListener)
    {
        retrievers.put(handle, inputListener);
    }
}
