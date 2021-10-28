package smashdudes.ecs.events;

import com.badlogic.gdx.graphics.OrthographicCamera;
import smashdudes.ecs.Entity;

public class CameraUpdateEvent extends Event
{
    public final OrthographicCamera camera;

    public CameraUpdateEvent(OrthographicCamera camera)
    {
        this.camera = camera;
    }
}
