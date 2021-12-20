package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class RemoveFrameCommand extends Command
{
    private final VM.Animation animation;
    private final VM.Animation prevState;
    private final Array<VM.AnimationFrame> toRemove;

    public RemoveFrameCommand(VM.Animation animation, Array<VM.AnimationFrame> toRemove)
    {
        this.animation = animation;
        this.toRemove = toRemove;
        prevState = VM.mapping(VM.mapping(animation));
    }

    @Override
    protected void execute()
    {
        animation.frames.removeAll(toRemove, true);
    }

    @Override
    protected void undo()
    {
        animation.frames = prevState.frames;
    }
}
