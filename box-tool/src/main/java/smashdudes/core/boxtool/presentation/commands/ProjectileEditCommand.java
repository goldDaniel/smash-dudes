package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.math.Vector2;
import smashdudes.content.DTO;

public class ProjectileEditCommand extends Command
{
    private final DTO.Projectile proj;
    private final DTO.Projectile data;

    private final Vector2 prevSpeed;
    private final Vector2 prevDim;
    private final Vector2 prevPos;

    public ProjectileEditCommand(DTO.Projectile proj, DTO.Projectile data)
    {
        this.proj = proj;
        this.data = data;

        prevSpeed = proj.speed;
        prevDim = proj.dim;
        prevPos = proj.pos;
    }

    @Override
    protected void execute()
    {
        proj.speed = data.speed.cpy();
        proj.dim = data.dim.cpy();
        proj.pos = data.pos.cpy();
    }

    @Override
    protected void undo()
    {
        proj.speed = prevSpeed.cpy();
        proj.dim = prevDim.cpy();
        proj.pos = prevPos.cpy();
    }
}
