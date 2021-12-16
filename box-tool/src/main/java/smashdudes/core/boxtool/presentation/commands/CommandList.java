package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.commands.Command;

public class CommandList
{
    private Array<Command> commands = new Array<>();
    int currentIndex = -1;


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
        if(currentIndex >= 0 &&
           currentIndex < commands.size - 1)
        {
            commands.get(currentIndex).execute();
            currentIndex++;
        }
    }

    public void undo()
    {
        if(currentIndex >= 0)
        {
            commands.get(currentIndex).undo();
            currentIndex--;
        }
    }
}
