package smashdudes.core;

import com.badlogic.gdx.math.Vector2;

public class Entity
{
    private static int nextID = 1;
    public final int uniqueID = nextID++;

    public final Vector2 position = new Vector2();
    public final Vector2 velocity = new Vector2();
}



