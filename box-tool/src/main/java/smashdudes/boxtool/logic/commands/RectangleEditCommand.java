package smashdudes.boxtool.logic.commands;


import com.badlogic.gdx.math.Rectangle;

public class RectangleEditCommand extends Command
{
    private final Rectangle rect;
    private final float[] data;
    private final float prevX;
    private final float prevY;
    private final float prevWidth;
    private final float prevHeight;

    public RectangleEditCommand(Rectangle rect, float[] data)
    {
        this.rect = rect;
        this.data = data;

        prevX = rect.x;
        prevY = rect.y;
        prevWidth = rect.width;
        prevHeight = rect.height;
    }

    @Override
    protected void execute()
    {
        rect.x = data[0];
        rect.y = data[1];
        rect.width = Math.abs(data[2]);
        rect.height = Math.abs(data[3]);
    }

    @Override
    protected void undo()
    {
        rect.x = prevX;
        rect.y = prevY;
        rect.width = prevWidth;
        rect.height = prevHeight;
    }
}
