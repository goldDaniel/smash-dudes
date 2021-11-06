package smashdudes.core.input;


/**
 * Getters for the current input state of an input device
 */
public interface GameInputRetriever
{
    boolean getLeft();
    boolean getRight();
    boolean getUp();
    boolean getDown();
}
