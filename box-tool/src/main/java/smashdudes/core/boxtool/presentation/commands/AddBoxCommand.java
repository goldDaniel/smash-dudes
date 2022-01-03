package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class AddBoxCommand extends Command
{

    private Array<Rectangle> boxes;
    private Rectangle box;


    public AddBoxCommand(Array<Rectangle> boxes, Rectangle box)
    {
        this.boxes = boxes;
        this.box = box;
    }

    @Override
    protected void execute()
    {
        boxes.add(box);
    }

    @Override
    protected void undo()
    {
        boxes.removeValue(box, true);
    }
}
