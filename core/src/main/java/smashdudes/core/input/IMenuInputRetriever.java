package smashdudes.core.input;

import com.badlogic.gdx.math.Vector2;

public interface IMenuInputRetriever
{
    boolean leftPressed();

    boolean rightPressed();

    boolean upPressed();

    boolean downPressed();

    boolean confirmPressed();
    boolean cancelPressed();
}
