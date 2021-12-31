package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class ColliderDimEditCommand extends Command
{
    private final VM.Character character;
    private final FloatArray oldValue;
    private final FloatArray newValue;

    public ColliderDimEditCommand(VM.Character character, float[] newValue)
    {
        this.character = character;
        oldValue = character.terrainCollider;
        this.newValue = new FloatArray(newValue);
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
