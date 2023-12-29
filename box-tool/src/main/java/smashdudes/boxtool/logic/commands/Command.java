package smashdudes.boxtool.logic.commands;

public abstract class Command
{
    protected abstract void execute();

    protected abstract void undo();
}