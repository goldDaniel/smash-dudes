package smashdudes.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;

public class GameInputHandler
{
    private Array<PlayerHandle> handles = new Array<>();
    private ArrayMap<PlayerHandle, GameInputRetriever> retrievers = new ArrayMap<>();

    private InputMultiplexer multiplexer = new InputMultiplexer();

    protected GameInputHandler(){}

    public InputProcessor getInputProcessor()
    {
        return multiplexer;
    }

    public GameInputRetriever getGameInput(PlayerHandle handle)
    {
        if(!retrievers.containsKey(handle)) throw new IllegalStateException("Player does not have input handler!");

        return retrievers.get(handle);
    }

    protected Iterable<PlayerHandle> getHandles()
    {
        return handles;
    }

    protected void register(PlayerHandle handle, KeyboardInputListener inputListener)
    {
        handles.add(handle);

        retrievers.put(handle, inputListener);
        multiplexer.addProcessor(inputListener);
    }

    protected void register(PlayerHandle handle, ControllerInputListener inputListener)
    {
        handles.add(handle);
        retrievers.put(handle, inputListener);
    }
}
