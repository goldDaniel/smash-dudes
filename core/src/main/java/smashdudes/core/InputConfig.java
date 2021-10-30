package smashdudes.core;

import com.badlogic.gdx.Input;

public class InputConfig
{
    public final int left;
    public final int right;
    public final int up;
    public final int down;
    public final int sprint;

    public InputConfig(int left, int right, int up, int down, int sprint)
    {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.sprint = sprint;
    }
}
