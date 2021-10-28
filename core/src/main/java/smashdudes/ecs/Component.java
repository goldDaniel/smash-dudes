package smashdudes.ecs;

public abstract class Component
{
    private boolean enabled = true;

    public void disable()
    {
        enabled = false;
    }

    public void enable()
    {
        enabled = true;
    }

    public boolean isEnabled()
    {
        return enabled;
    }
}
