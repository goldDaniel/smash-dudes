package smashdudes.core.logic.commands;

import com.badlogic.gdx.Gdx;

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
