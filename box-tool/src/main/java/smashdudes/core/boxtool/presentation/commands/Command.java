package smashdudes.core.boxtool.presentation.commands;

public abstract class Command
{
    protected abstract void execute();

    protected abstract void undo();
}