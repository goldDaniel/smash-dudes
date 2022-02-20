package smashdudes.core.boxtool.presentation.commands;

import com.badlogic.gdx.math.Vector2;
import smashdudes.core.Projectile;

public class ProjecileEditCommand extends Command
{
    private final Projectile proj;
    private final Projectile data;

    private final Vector2 prevSpeed;
    private final Vector2 prevDim;
    private final Vector2 prevPos;

    public ProjecileEditCommand(Projectile proj, Projectile data)
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
