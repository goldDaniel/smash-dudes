package smashdudes.core.input;

import com.badlogic.gdx.math.Vector2;

public interface IMenuInputRetriever
{
    Vector2 getDirection();
    boolean confirmPressed();
}
