package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.content.DTO;

public class RemoveProjectileCommand extends Command
{
    private final Array<DTO.Projectile> projectiles;
    private final int index;
    private final DTO.Projectile projectile;

    public RemoveProjectileCommand(Array<DTO.Projectile> projectiles, DTO.Projectile projectile)
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
