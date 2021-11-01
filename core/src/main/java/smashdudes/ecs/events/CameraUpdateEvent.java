package smashdudes.ecs.events;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraUpdateEvent extends Event
{
    public final OrthographicCamera camera;

    public CameraUpdateEvent(OrthographicCamera camera)
    {
        this.camera = camera;
    }
}
