package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.math.Vector2;
import smashdudes.core.boxtool.presentation.viewmodel.VM;

public class ColliderDimEditCommand extends Command
{
    private final VM.Character character;
    private final Vector2 oldValue;
    private final Vector2 newValue;

    public ColliderDimEditCommand(VM.Character character, float[] newValue)
    {
        this.character = character;
        oldValue = character.terrainCollider;
        this.newValue = new Vector2(newValue[0], newValue[1]);
    }

    @Override
    protected void execute()
    {
        character.terrainCollider = newValue.cpy();
    }

    @Override
    protected void undo()
    {
        character.terrainCollider = oldValue.cpy();
    }
}
