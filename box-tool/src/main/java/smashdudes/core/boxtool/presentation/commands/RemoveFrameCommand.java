package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class RemoveFrameCommand extends Command
{
    private final VM.Animation animation;
    private final Array<VM.AnimationFrame> frames;
    private final Array<Integer> indices = new Array<>();

    public RemoveFrameCommand(VM.Animation animation, Array<VM.AnimationFrame> frames)
    {
        this.animation = animation;
        this.frames = frames;

        for(VM.AnimationFrame frame : frames)
        {
            indices.add(animation.frames.indexOf(frame, true));
        }
    }

    @Override
    protected void execute()
    {
        animation.frames.removeAll(frames, true);
    }

    @Override
    protected void undo()
    {
        for(int i = 0; i < indices.size; i++)
        {
            animation.frames.insert(indices.get(i), frames.get(i));
        }
    }
}
