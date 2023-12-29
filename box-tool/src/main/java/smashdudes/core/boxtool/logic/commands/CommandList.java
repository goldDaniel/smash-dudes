package smashdudes.core.boxtool.logic.commands;

import com.badlogic.gdx.utils.Array;

public class CommandList
{
    public interface OnUndo
    {
        void execute();
    }

    public interface OnRedo
    {
        void execute();
    }

    final Array<OnUndo> undoCallbacks = new Array<>();
    final Array<OnRedo> redoCallbacks = new Array<>();

    private final Array<Command> commands = new Array<>();
    int currentIndex = -1;

    public void addUndoCallback(OnUndo undo)
    {
        undoCallbacks.add(undo);
    }

    public void addRedoCallback(OnRedo redo)
    {
        redoCallbacks.add(redo);
    }

    public void clear()
    {
        commands.clear();
        currentIndex = -1;
    }

    public void execute(Command c)
    {
        //if we have existing commands in the list, and we are executing a new one, we must remove all the
        //ones AFTER the current index, this is because we cannot redo actions after a new one has been
        //executed or else things will get out of order
        if(currentIndex + 1 < commands.size - 1)
        {
            commands.removeRange(currentIndex + 1, commands.size - 1);
        }

        c.execute();
        commands.add(c);
        currentIndex++;
    }

    public void redo()
    {
        if(currentIndex >= -1 &&
           currentIndex < commands.size - 1)
        {
            commands.get(++currentIndex).execute();
        }

        for(OnRedo redo : redoCallbacks)
        {
            redo.execute();
        }
    }

    public void undo()
    {
        if(currentIndex >= 0)
        {
            commands.get(currentIndex).undo();
            currentIndex--;
        }

        for(OnUndo undo : undoCallbacks)
        {
            undo.execute();
        }
    }
}
