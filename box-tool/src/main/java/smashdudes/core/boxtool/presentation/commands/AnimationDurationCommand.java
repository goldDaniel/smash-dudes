package smashdudes.core.boxtool.presentation.commands;

import smashdudes.content.DTO;

public class AnimationDurationCommand extends Command
{
    private final DTO.Animation animation;
    private final float oldValue;
    private final float newValue;

    public AnimationDurationCommand(DTO.Animation animation, float newValue)
    {
        this.animation = animation;
        oldValue = animation.animationDuration;
        this.newValue = newValue;
    }

    @Override
    protected void execute()
    {
        animation.animationDuration = newValue;
    }

    @Override
    protected void undo()
    {
        animation.animationDuration = oldValue;
    }
}
