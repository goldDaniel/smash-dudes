package smashdudes.core.input;

import com.badlogic.gdx.Input;

public class InputConfig
{
    public final int left;
    public final int right;
    public final int up;
    public final int down;

    public InputConfig(int left, int right, int up, int down)
    {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }
}
