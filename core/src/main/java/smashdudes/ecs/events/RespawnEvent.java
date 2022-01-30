package smashdudes.ecs.events;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Entity;

public class RespawnEvent extends Event
{
    public final Vector2 respawnPoint;

    public RespawnEvent(Entity entity, Vector2 respawnPoint)
    {
        super(entity);
        this.respawnPoint = respawnPoint;
    }
}
