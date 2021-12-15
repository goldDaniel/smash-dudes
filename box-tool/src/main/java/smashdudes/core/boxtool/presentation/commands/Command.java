package smashdudes.core.boxtool.presentation.commands;

public abstract class Command
{
    public abstract void execute();

    public abstract void undo();
}