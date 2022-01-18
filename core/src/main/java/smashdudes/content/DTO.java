package smashdudes.content;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.JsonValue;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;

public class DTO
{
    public static class Stage
    {
        public Array<Terrain> terrain = new Array<>();
        public Array<Vector2> spawnPoints = new Array<>();
    }

    public static class Terrain
    {
        public String textureFilePath;
        public Vector2 position = new Vector2();

        public float width, height;

        protected void setTerrainData(JsonValue terrainData)
        {
            textureFilePath = terrainData.get("texture").asString();
            position.x = terrainData.get("position").asFloatArray()[0];
            position.y = terrainData.get("position").asFloatArray()[1];

            width = terrainData.get("dim").get("width").asFloat();
            height = terrainData.get("dim").get("height").asFloat();
        }
    }

    public static class Character
    {
        public Array<Animation> animations = new Array<>();

        public Rectangle terrainCollider = new Rectangle(0, 0, 2, 2);
        public float scale = 2;


        public float jumpStrength = 20;
        public float gravity = 20;
        public float weight = 10;
        public float airSpeed = 10;
        public float runSpeed = 10;
        public float deceleration = 40;

        public String name;
    }

    public static class Animation
    {
        public String textureFilePath;
        public String animationName;

        public float animationDuration;

        public boolean usesSpriteSheet;

        public Array<AnimationFrame> frames = new Array<>();
    }

    public static class AnimationFrame
    {
        public Rectangle textureRegion;

        public Array<Rectangle> attackboxes = new Array<>();
        public Array<Rectangle> bodyboxes = new Array<>();

        public String texturePath;
    }
}
