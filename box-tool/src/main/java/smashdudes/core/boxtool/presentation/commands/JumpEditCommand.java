package smashdudes.core.boxtool.presentation.commands;

import smashdudes.content.DTO;

public class JumpEditCommand extends Command
{
    private final DTO.Character character;
    private final float oldValue;
    private final float newValue;

    public JumpEditCommand(DTO.Character character, float newValue)
    {
        this.character = character;
        oldValue = character.jumpStrength;
        this.newValue = newValue;
    }

    @Override
    protected void execute()
    {
        character.jumpStrength = newValue;
    }

    @Override
    protected void undo()
    {
        character.jumpStrength = oldValue;
    }
}
