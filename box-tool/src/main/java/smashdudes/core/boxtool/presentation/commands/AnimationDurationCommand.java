package smashdudes.core.boxtool.presentation.commands;

import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class AnimationDurationCommand extends Command
{
    private final VM.Animation animation;
    private final float prevDuration;
    private final float duration;

    public AnimationDurationCommand(VM.Animation anim, float duration)
    {
        this.animation = anim;
        prevDuration = animation.animationDuration;
        this.duration = duration;
    }

    @Override
    protected void execute()
    {
        animation.animationDuration = duration;
    }

    @Override
    protected void undo()
    {
        animation.animationDuration = prevDuration;
    }
}
