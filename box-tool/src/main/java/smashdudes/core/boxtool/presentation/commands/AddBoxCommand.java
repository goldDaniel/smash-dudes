package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class AddBoxCommand extends Command
{

    private Array<FloatArray> boxes;
    private FloatArray box;


    public AddBoxCommand(Array<FloatArray> boxes, FloatArray box)
    {
        this.boxes = boxes;
        this.box = box;
    }

    @Override
    public void execute()
    {
        boxes.add(box);
    }

    @Override
    public void undo()
    {
        boxes.removeValue(box, true);
    }
}
