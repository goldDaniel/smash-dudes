package smashdudes.core.boxtool.presentation.commands;

import smashdudes.content.DTO;

public class WeightEditCommand extends Command
{
    private final DTO.Character character;
    private final float oldVlaue;
    private final float newValue;

    public WeightEditCommand(DTO.Character character, float newValue)
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
