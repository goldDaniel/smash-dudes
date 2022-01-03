package smashdudes.core.boxtool.presentation.commands;

import smashdudes.content.DTO;

public class GravityEditCommand extends Command
{
    private final DTO.Character character;
    private final float oldValue;
    private final float newValue;

    public GravityEditCommand(DTO.Character character, float newValue)
    {
        this.character = character;
        oldValue = character.gravity;
        this.newValue = newValue;
    }

    @Override
    protected void execute()
    {
        character.gravity = newValue;
    }

    @Override
    protected void undo()
    {
        character.gravity = oldValue;
    }
}
