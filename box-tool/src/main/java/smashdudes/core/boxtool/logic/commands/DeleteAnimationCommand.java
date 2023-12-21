package smashdudes.core.boxtool.logic.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.content.DTO;

public class DeleteAnimationCommand extends Command
{
    private final Array<DTO.Animation> animations;
    private final DTO.Animation anim;

    public DeleteAnimationCommand(Array<DTO.Animation> animations, DTO.Animation anim)
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
