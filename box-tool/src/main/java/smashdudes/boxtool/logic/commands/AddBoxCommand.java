package smashdudes.boxtool.logic.commands;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import smashdudes.gameplay.CombatBox;

import java.lang.reflect.InvocationTargetException;

public class AddBoxCommand<T extends CombatBox> extends Command
{

    private final Array<T> boxes;
    private final T box;


    public AddBoxCommand(Array<T> boxes, Class<T> boxType)
    {
        this.boxes = boxes;
        this.box = instantiate(boxType, new Rectangle());
    }

    public AddBoxCommand(Array<T> boxes, Class<T> boxType, Rectangle rect)
    {
        this.boxes = boxes;
        this.box = instantiate(boxType, rect);
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

    private T instantiate(Class<T> boxType, Rectangle rect)
    {
        try
        {
            return boxType.getDeclaredConstructor(Rectangle.class).newInstance(rect);
        }
        catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }
}
