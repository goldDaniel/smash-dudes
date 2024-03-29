package smashdudes.core.input;


/**
 * Holds the button states for our input
 */
public class InputState
{
    public boolean left  = false;
    public boolean right = false;
    public boolean up    = false;
    public boolean down  = false;

    public boolean punch = false;
    public boolean block = false;

    public InputState() {}

    public InputState(InputState state)
    {
        this.left   = state.left;
        this.right  = state.right;
        this.up     = state.up;
        this.down   = state.down;
        this.punch = state.punch;
        this.block = state.block;
    }
}