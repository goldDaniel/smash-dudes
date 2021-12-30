package smashdudes.core.boxtool.presentation.commands;

import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class AnimationDurationCommand extends Command
{
    private final VM.Animation animation;
    private final float oldValue;
    private final float newValue;

    public AnimationDurationCommand(VM.Animation animation, float newValue)
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
