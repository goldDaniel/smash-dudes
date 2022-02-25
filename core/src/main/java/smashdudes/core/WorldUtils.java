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

    public static boolean isSegmentTouchingTerrain(Vector2 point1, Vector2 point2)
    {
        float colliderSize = 0.001f;
        float width = Math.max(Math.abs(point1.x - point2.x), colliderSize);
        float height = Math.max(Math.abs(point1.y - point2.y), colliderSize);
        float xPos = (point1.x + point2.x) / 2;
        float yPos = (point1.y + point2.y) / 2;

        Rectangle collider = new Rectangle(xPos, yPos, width, height);
        for(DTO.Terrain terrain : stage.terrain)
        {
            if(collider.overlaps(new Rectangle(terrain.position.x, terrain.position.y,
                                                    terrain.collisionWidth, terrain.collisionHeight))) return true;
        }

        return false;
    }
}
