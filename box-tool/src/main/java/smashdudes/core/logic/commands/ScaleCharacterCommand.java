package smashdudes.core.logic.commands;

import com.badlogic.gdx.math.Rectangle;
import smashdudes.content.DTO;

public class ScaleCharacterCommand extends Command
{
    private final DTO.Character character;
    private final float newScale;
    private final float oldScale;

    public ScaleCharacterCommand(DTO.Character character, float newScale)
    {
        if(newScale <= 0.1f) newScale = 0.1f;

        this.character = character;
        this.oldScale = character.scale;
        this.newScale = newScale;
    }

    @Override
    protected void execute()
    {
        character.scale = newScale;
        float boxScalar = newScale / oldScale;

        scaleBoxes(boxScalar);
    }

    @Override
    protected void undo()
    {
        character.scale = oldScale;
        float boxScalar = oldScale / newScale;

        scaleBoxes(boxScalar);
    }

    private void scaleBoxes(float boxScalar)
    {

        character.terrainCollider.x *= boxScalar;
        character.terrainCollider.y *= boxScalar;
        character.terrainCollider.width *= boxScalar;
        character.terrainCollider.height *= boxScalar;

        for(DTO.Animation animation : character.animations)
        {
            for(DTO.AnimationFrame frame : animation.frames)
            {
                for(Rectangle rect : frame.bodyboxes)
                {
                    rect.x      *= boxScalar;
                    rect.y      *= boxScalar;
                    rect.width  *= boxScalar;
                    rect.height *= boxScalar;
                }
                for(Rectangle rect : frame.attackboxes)
                {
                    rect.x      *= boxScalar;
                    rect.y      *= boxScalar;
                    rect.width  *= boxScalar;
                    rect.height *= boxScalar;
                }
            }
        }
    }
}
