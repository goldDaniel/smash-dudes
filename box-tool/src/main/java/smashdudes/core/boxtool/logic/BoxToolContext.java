package smashdudes.core.boxtool.logic;

import smashdudes.content.DTO;
import smashdudes.core.boxtool.presentation.commands.Command;
import smashdudes.core.boxtool.presentation.commands.CommandList;

public class BoxToolContext
{
    private final CommandList commandList = new CommandList();
    private DTO.Character currentCharacter = null;

    private DTO.Animation currentAnimation = null;

    private DTO.AnimationFrame currentAnimationFrame = null;

    private boolean playAnimation = false;

    private float currentTime = 0;

    public void setCurrentCharacter(DTO.Character character)
    {
        commandList.clear();
        this.currentCharacter = character;
        this.currentAnimation = null;
        this.currentAnimationFrame = null;
        this.playAnimation = false;
        this.currentTime = 0;
    }

    public DTO.Character getCharacter()
    {
        return currentCharacter;
    }

    public void setCurrentAnimation(DTO.Animation animation)
    {
        this.currentAnimation = animation;
        if(this.currentAnimation == null)
        {
            currentAnimationFrame = null;
        }
        else if(this.currentAnimation.frames.notEmpty())
        {
            this.currentAnimationFrame = this.currentAnimation.frames.first();
        }
    }

    public DTO.Animation getCurrentAnimation()
    {
        return currentAnimation;
    }

    public void setAnimationFrame(DTO.AnimationFrame frame)
    {
        this.currentAnimationFrame = frame;
    }

    public DTO.AnimationFrame getAnimationFrame()
    {
        return this.currentAnimationFrame;
    }

    public void undo()
    {
        commandList.undo();
    }

    public void redo()
    {
        commandList.redo();
    }

    public void execute(Command c)
    {
        commandList.execute(c);
    }

    public void incrementTime(float increment)
    {
        currentTime += increment;
    }

    public float getCurrentTime()
    {
        return currentTime;
    }

    public boolean isPlayingAnimation()
    {
        return playAnimation;
    }

    public void startAnimation()
    {
        playAnimation = true;
    }

    public void pauseAnimation()
    {
        playAnimation = false;
    }

    public void stopAnimation()
    {
        currentTime = 0;
        playAnimation = false;
    }
}