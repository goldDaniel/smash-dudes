package smashdudes.core.boxtool.logic.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.gameplay.CombatBox;

public class RemoveBoxCommand<T extends CombatBox> extends Command
{
    private final Array<T> boxes;
    private final int index;
    private final T box;


    public RemoveBoxCommand(Array<T> boxes, T box)
    {
        this.boxes = boxes;
        this.box = box;

        index = boxes.indexOf(box, true);
    }

    @Override
    protected void execute()
    {
        boxes.removeValue(box, true);
    }

    @Override
    protected void undo()
    {
        boxes.insert(index, box);
    }
}
