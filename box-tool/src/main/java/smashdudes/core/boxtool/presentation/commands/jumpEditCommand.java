package smashdudes.core.boxtool.presentation.commands;

import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class jumpEditCommand extends Command
{
    private final VM.Character character;
    private final float oldValue;
    private final float newValue;

    public jumpEditCommand(VM.Character character, float newValue)
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
