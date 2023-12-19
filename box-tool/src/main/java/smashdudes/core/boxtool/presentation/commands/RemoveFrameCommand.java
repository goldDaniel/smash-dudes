package smashdudes.core.boxtool.presentation.commands;

import smashdudes.content.DTO;

public class RemoveFrameCommand extends Command
{
    private final DTO.Animation animation;
    private final DTO.AnimationFrame toRemove;

    private final int prevIndex;

    public RemoveFrameCommand(DTO.Animation animation, DTO.AnimationFrame toRemove)
    {
        this.animation = animation;
        this.toRemove = toRemove;
        prevIndex = animation.frames.indexOf(this.toRemove, true);
    }

    @Override
    protected void execute()
    {
        animation.frames.removeIndex(prevIndex);
    }

    @Override
    protected void undo()
    {
        animation.frames.add(new DTO.AnimationFrame());
        for (int i = prevIndex; i < animation.frames.size - 1; i++)
        {
            animation.frames.set(i + 1, animation.frames.get(i));
        }

        animation.frames.set(prevIndex, toRemove);
    }
}
