package smashdudes.core.boxtool.presentation.commands;

import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class WeightEditCommand extends Command
{
    private final VM.Character character;
    private final float oldVlaue;
    private final float newValue;

    public WeightEditCommand(VM.Character character, float newValue)
    {
        this.character = character;
        oldVlaue = character.weight;
        this.newValue = newValue;
    }

    @Override
    protected void execute()
    {
        character.weight = newValue;
    }

    @Override
    protected void undo()
    {
        character.weight = oldVlaue;
    }
}
