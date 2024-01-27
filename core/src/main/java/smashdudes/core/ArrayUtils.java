package smashdudes.core;

import com.badlogic.gdx.utils.Array;

public class ArrayUtils
{
    public static <T> Array<T> toArray(T... elements)
    {
        return new Array<T>(elements);
    }
}
