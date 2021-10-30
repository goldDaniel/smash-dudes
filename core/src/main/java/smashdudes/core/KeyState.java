package smashdudes.core;

public class KeyState
{
    public boolean left   = false;
    public boolean right  = false;
    public boolean up     = false;
    public boolean down   = false;
    public boolean sprint = false;

    public KeyState() {}

    public KeyState(KeyState state)
    {
        this.left   = state.left;
        this.right  = state.right;
        this.up     = state.up;
        this.down   = state.down;
        this.sprint = state.sprint;
    }
}