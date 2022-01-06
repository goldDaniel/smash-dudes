package smashdudes.core.input;


/**
 * Getters for the current input state of an input device
 */
public interface IGameInputRetriever
{
    boolean getLeft();
    boolean getRight();
    boolean getUp();
    boolean getDown();

    boolean punch();
    boolean special();
}
