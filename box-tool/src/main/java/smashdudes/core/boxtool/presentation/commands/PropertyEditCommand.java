package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import smashdudes.content.DTO;

import java.lang.reflect.Field;

public class PropertyEditCommand<T, U> extends Command
{
    private final U instance;
    private final T newData;

    private Field field;
    private T oldData;

    public PropertyEditCommand(String propertyName, T data, U instance)
    {
        this.instance = instance;
        newData = data;
        Array<Class<?>> classes = new Array<>(DTO.class.getDeclaredClasses());
        if(!classes.contains(instance.getClass(), false))
        {
            throw new IllegalArgumentException("Must be of type 'DTO'");
        }
        try
        {
            Field property = instance.getClass().getField(propertyName);
            field = property;
            oldData = (T)property.get(instance);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    @Override
    protected void execute()
    {
        try
        {
            field.set(instance, newData);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    @Override
    protected void undo()
    {
        try
        {
            field.set(instance, oldData);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }
}
