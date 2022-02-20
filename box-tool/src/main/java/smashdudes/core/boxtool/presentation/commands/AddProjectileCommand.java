package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.Projectile;

public class AddProjectileCommand extends Command
{
    private final Array<Projectile> projs;
    private final Projectile proj;

    public AddProjectileCommand(Array<Projectile> projs, Projectile proj)
    {
        this.projs = projs;
        this.proj = proj;
    }

    @Override
    protected void execute()
    {
        projs.add(proj);
    }

    @Override
    protected void undo()
    {
        projs.removeValue(proj, true);
    }
}
