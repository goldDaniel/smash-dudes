package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class RemoveBoxCommand extends Command
{
    private Array<FloatArray> boxes;
    private int index;
    private FloatArray box;


    public RemoveBoxCommand(Array<FloatArray> boxes, FloatArray box)
    {
        this.boxes = boxes;
        this.box = box;

        index = boxes.indexOf(box, true);
    }

    @Override
    public void execute()
    {
        boxes.removeValue(box, true);
    }

    @Override
    public void undo()
    {
        boxes.insert(index, box);
    }
}
