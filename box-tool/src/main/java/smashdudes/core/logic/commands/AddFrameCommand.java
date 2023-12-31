package smashdudes.core.logic.commands;

import smashdudes.content.DTO;

public class AddFrameCommand extends Command
{
    private final DTO.Animation animation;
    private final DTO.AnimationFrame frame;
    private final int idx;

    public AddFrameCommand(DTO.Animation animation, int idx, DTO.AnimationFrame frame)
    {
        this.animation = animation;
        this.frame = frame;
        this.idx = idx;
    }

    @Override
    protected void execute()
    {
        animation.frames.insert(idx, frame);
    }

    @Override
    protected void undo()
    {
        animation.frames.removeValue(frame, true);
    }
}
