package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class AddAnimationCommand extends Command
{
    private Array<VM.Animation> animations;
    private VM.Animation anim;

    public AddAnimationCommand(Array<VM.Animation> animations, VM.Animation anim)
    {
        this.animations = animations;
        this.anim = anim;
    }

    @Override
    protected void execute()
    {
        animations.add(anim);
    }

    @Override
    protected void undo()
    {
        animations.removeValue(anim, true);
    }
}
