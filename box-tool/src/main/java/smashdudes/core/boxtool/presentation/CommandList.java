package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.commands.Command;

public class CommandList
{
    private Array<Command> commands = new Array<>();


    public void clear()
    {
        commands.clear();
    }

    public void execute(Command c)
    {
        c.execute();
        commands.add(c);
    }

    public void redo()
    {

    }

    public void undo()
    {
        if(commands.size > 0)
        {
            commands.pop().undo();
        }
    }
}
