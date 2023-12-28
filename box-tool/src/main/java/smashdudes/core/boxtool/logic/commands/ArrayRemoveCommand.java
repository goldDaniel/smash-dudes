package smashdudes.core.boxtool.logic.commands;

import com.badlogic.gdx.utils.Array;

public class ArrayRemoveCommand<T> extends Command
{
    private final Array<T> list;

    private final T data;

    private final int index;

    public ArrayRemoveCommand(Array<T> list, T data)
    {
        this.list = list;
        this.data = data;

        index = list.indexOf(data, true);
    }

    @Override
    protected void execute()
    {
        list.removeIndex(index);
    }

    @Override
    protected void undo()
    {
        list.insert(index, data);
    }
}
