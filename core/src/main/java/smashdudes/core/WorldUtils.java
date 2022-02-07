package smashdudes.core;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import smashdudes.content.DTO;

public class WorldUtils
{
    private static DTO.Stage stage;

    public static void setStage(DTO.Stage stage)
    {
        WorldUtils.stage = stage;
    }

    public static Rectangle getStageBounds()
    {
        return new Rectangle(stage.stageBounds);
    }

    public static Vector2 getRespawnPoint()
    {
        return stage.respawnPoint.cpy();
    }
}
