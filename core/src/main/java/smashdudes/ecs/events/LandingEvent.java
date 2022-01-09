package smashdudes.ecs.events;

import com.badlogic.gdx.math.Vector2;
import smashdudes.ecs.Entity;

public class LandingEvent extends Event
{
    public final Vector2 landingPoint;

    public LandingEvent(Entity entity, Vector2 landingPoint)
    {
        super(entity);
        this.landingPoint = landingPoint;
    }
}
