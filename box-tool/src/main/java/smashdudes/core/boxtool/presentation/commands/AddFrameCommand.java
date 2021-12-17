package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class AddFrameCommand extends Command
{
    private final VM.Animation animation;
    private final VM.AnimationFrame frame;
    private final int idx;

    public AddFrameCommand(VM.Animation animation, int idx, VM.AnimationFrame frame)
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
