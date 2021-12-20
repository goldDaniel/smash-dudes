package smashdudes.core.boxtool.presentation.commands;

import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class gravityEditCommand extends Command
{
    private final VM.Character character;
    private final float oldValue;
    private final float newValue;

    public gravityEditCommand(VM.Character character, float newValue)
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
