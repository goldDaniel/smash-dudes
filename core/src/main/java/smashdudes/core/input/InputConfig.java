package smashdudes.core.input;


/**
 * Holds the keyboard input configuration
 */
public class InputConfig
{
    public final int left;
    public final int right;
    public final int up;
    public final int down;

    public final int punch;

    public InputConfig(int left, int right, int up, int down, int punch)
    {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.punch = punch;
    }
}
