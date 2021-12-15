package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.commands.Command;

public class CommandList
{
    private Array<Command> commands = new Array<>();
    private int index = -1;


    public void execute(Command c)
    {
        c.execute();
        commands.add(c);
        index++;
    }

    public void undo()
    {
        if(index >= 0)
        {
            commands.get(index).undo();
            index--;
        }
    }

    public boolean canUndo()
    {
        return index >= 0;
    }

    public boolean canRedo()
    {
        return index < commands.size - 1 && commands.size > 0;
    }
}
