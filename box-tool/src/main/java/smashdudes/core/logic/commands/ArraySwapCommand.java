package smashdudes.core.logic.commands;

import com.badlogic.gdx.utils.Array;

public class ArraySwapCommand extends Command
{

    private final Array list;
    private final int idx0;
    private final int idx1;

    public ArraySwapCommand(Array list, int idx0, int idx1)
    {
        this.list = list;
        this.idx0 = idx0;
        this.idx1 = idx1;
    }

    @Override
    protected void execute()
    {
        list.swap(idx0, idx1);
    }

    @Override
    protected void undo()
    {
        list.swap(idx0, idx1);
    }
}
