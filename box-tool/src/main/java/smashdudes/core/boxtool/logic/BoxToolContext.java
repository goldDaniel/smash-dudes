package smashdudes.core.boxtool.logic;

import smashdudes.content.DTO;
import smashdudes.core.boxtool.presentation.commands.Command;
import smashdudes.core.boxtool.presentation.commands.CommandList;

public class BoxToolContext
{
    private CommandList commandList = null;
    private DTO.Character currentCharacter = null;

    private DTO.Animation selectedAnimation = null;

    public BoxToolContext()
    {
        commandList = new CommandList();
    }

    public void setCurrentCharacter(DTO.Character character)
    {
        this.currentCharacter = character;
    }

    public DTO.Character getCharacter()
    {
        return currentCharacter;
    }

    public void undo()
    {
        commandList.undo();
    }

    public void redo()
    {
        commandList.redo();
    }

    public void execute(Command c)
    {
        commandList.execute(c);
    }
}
