package smashdudes.core.input;

public interface IMenuInputRetriever
{
    boolean leftPressed();

    boolean rightPressed();

    boolean upPressed();

    boolean downPressed();

    boolean confirmPressed();
    boolean cancelPressed();
}
