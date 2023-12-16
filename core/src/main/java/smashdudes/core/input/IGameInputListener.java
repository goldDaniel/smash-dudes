package smashdudes.core.input;

public interface IGameInputListener extends IGameInputRetriever, IMenuInputRetriever
{
    InputDeviceType getDeviceType();
}
