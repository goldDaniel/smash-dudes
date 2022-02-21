package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.utils.Array;
import smashdudes.content.DTO;

public class AddProjectileCommand extends Command
{
    private final Array<DTO.Projectile> projs;
    private final DTO.Projectile proj;

    public AddProjectileCommand(Array<DTO.Projectile> projs, DTO.Projectile proj)
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
