package smashdudes.core.boxtool.logic.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.content.DTO;

public class AddAnimationCommand extends Command
{
    private Array<DTO.Animation> animations;
    private DTO.Animation anim;

    public AddAnimationCommand(Array<DTO.Animation> animations, DTO.Animation anim)
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
