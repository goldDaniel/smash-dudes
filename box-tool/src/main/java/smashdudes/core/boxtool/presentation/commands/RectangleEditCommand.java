package smashdudes.core.boxtool.presentation.commands;


import com.badlogic.gdx.utils.FloatArray;

public class RectangleEditCommand extends Command
{
    private final FloatArray rect;
    private final float[] data;

    private final float prevX;
    private final float prevY;
    private final float prevWidth;
    private final float prevHeight;

    public RectangleEditCommand(FloatArray rect, float[] data)
    {
        this.rect = rect;
        this.data = data;

        prevX = rect.get(0);
        prevY = rect.get(1);
        prevWidth = rect.get(2);
        prevHeight = rect.get(3);
    }

    @Override
    public void execute()
    {
        rect.items[0] = data[0];
        rect.items[1] = data[1];
        rect.items[2] = data[2];
        rect.items[3] = data[3];
    }

    @Override
    public void undo()
    {
        rect.items[0] = prevX;
        rect.items[1] = prevY;
        rect.items[2] = prevWidth;
        rect.items[3] = prevHeight;
    }
}
