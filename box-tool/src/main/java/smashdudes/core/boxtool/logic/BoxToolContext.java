package smashdudes.core.boxtool.logic;

import com.badlogic.gdx.utils.Queue;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.presentation.commands.Command;
import smashdudes.core.boxtool.presentation.commands.CommandList;


public class BoxToolContext
{
    private final CommandList commandList = new CommandList();
    private final Queue<Command> commandQueue = new Queue<>();
    private DTO.Character currentCharacter = null;

    private DTO.Animation currentAnimation = null;

    private DTO.AnimationFrame currentAnimationFrame = null;

    private boolean playAnimation = false;

    private float currentTime = 0;

    public void setCurrentCharacter(DTO.Character character)
    {
        commandList.clear();
        commandQueue.clear();
        this.currentCharacter = character;
        if(character.animations.size > 0)
        {
            this.currentAnimation = character.animations.first();
            if(this.currentAnimation.frames.size > 0)
            {
                this.currentAnimationFrame = this.currentAnimation.frames.first();
            }
        }

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
        this.currentAnimationFrame = null;

        if(this.currentAnimation != null && this.currentAnimation.frames.notEmpty())
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
        commandQueue.addLast(c);
    }

    public void incrementTime(float increment)
    {
        if(playAnimation)
        {
            currentTime += increment;
        }
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

    public void endFrame()
    {
        while(commandQueue.notEmpty())
        {
            commandList.execute(commandQueue.removeFirst());
        }
    }
}
