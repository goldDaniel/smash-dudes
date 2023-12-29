package smashdudes.boxtool.logic.commands;

import com.badlogic.gdx.utils.Array;

import java.lang.reflect.InvocationTargetException;

public class ArrayAddCommand<T> extends Command
{
    private final Array<T> list;

    private final T data;

    public ArrayAddCommand(Array<T> list, Class<T> classType)
    {
        this.list = list;
        data = instantiate(classType);
    }

    @Override
    protected void execute()
    {
        list.add(data);
    }

    @Override
    protected void undo()
    {
        list.removeValue(data, true);
    }

    private T instantiate(Class<T> type)
    {
        try
        {
            return type.getDeclaredConstructor().newInstance();
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }
}
