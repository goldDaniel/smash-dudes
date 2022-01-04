package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class RemoveBoxCommand extends Command
{
    private Array<Rectangle> boxes;
    private int index;
    private Rectangle box;


    public RemoveBoxCommand(Array<Rectangle> boxes, Rectangle box)
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
