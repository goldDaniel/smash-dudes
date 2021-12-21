package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class DeleteAnimationCommand extends Command
{
    private final Array<VM.Animation> animations;
    private final VM.Animation anim;

    public DeleteAnimationCommand(Array<VM.Animation> animations, VM.Animation anim)
    {
        this.animations = animations;
        this.anim = anim;
    }
    @Override
    protected void execute()
    {
        animations.removeValue(anim, true);
    }

    @Override
    protected void undo()
    {
        animations.add(anim);
    }
}
