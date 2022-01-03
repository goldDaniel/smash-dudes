package smashdudes.core.boxtool.presentation.commands;

import smashdudes.content.DTO;

public class ScaleEditCommand extends Command
{
    private final DTO.Character character;
    private final float oldValue;
    private final float newValue;

    public ScaleEditCommand(DTO.Character character, float newValue)
    {
        this.character = character;
        oldValue = character.scale;
        this.newValue = newValue;
    }

    @Override
    protected void execute()
    {
        character.scale = newValue;
    }

    @Override
    protected void undo()
    {
        character.scale = oldValue;
    }

}
