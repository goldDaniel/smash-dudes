package smashdudes.core;

public class PlayerHandle
{
    private static int nextID = 1;

    public final int ID = nextID++;

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof PlayerHandle)
        {
            return ((PlayerHandle)obj).ID == this.ID;
        }
        return false;
    }}
