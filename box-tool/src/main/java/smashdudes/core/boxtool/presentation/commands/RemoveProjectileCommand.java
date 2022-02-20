package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.Projectile;

public class RemoveProjectileCommand extends Command
{
    private final Array<Projectile> projectiles;
    private final int index;
    private final Projectile projectile;

    public RemoveProjectileCommand(Array<Projectile> projectiles, Projectile projectile)
    {
        this.projectiles = projectiles;
        this.projectile = projectile;

        index = projectiles.indexOf(projectile, true);
    }

    @Override
    protected void execute()
    {
        projectiles.removeValue(projectile, true);
    }

    @Override
    protected void undo()
    {
        projectiles.insert(index, projectile);
    }
}
