package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.math.Rectangle;
import smashdudes.content.DTO;

public class ColliderDimEditCommand extends Command
{
    private final DTO.Character character;
    private final Rectangle oldValue;
    private final Rectangle newValue;

    public ColliderDimEditCommand(DTO.Character character, float[] newValue)
    {
        this.character = character;
        oldValue = character.terrainCollider;
        this.newValue = new Rectangle(newValue[0], newValue[1], newValue[2], newValue[3]);
    }

    @Override
    protected void execute()
    {
        character.terrainCollider = newValue;
    }

    @Override
    protected void undo()
    {
        character.terrainCollider = oldValue;
    }
}
