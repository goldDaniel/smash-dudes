package smashdudes.core.boxtool.presentation.commands;

import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class ScaleEditCommand extends Command
{
    private final VM.Character character;
    private final float oldValue;
    private final float newValue;

    public ScaleEditCommand(VM.Character character, float newValue)
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
