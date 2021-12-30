package smashdudes.content;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class DTO
{
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

        public Rectangle terrainCollider = new Rectangle();
        public Vector2 debugDim = new Vector2();
        public Vector2 drawDim = new Vector2();
        public float scale;

        public float jumpStrength;
        public float gravity;
        public float weight;

        public String name;
    }

    public static class Animation
    {
        public String textureFilePath;
        public String animationName;

        public float animationDuration = 0.25f;

        public boolean usesSpriteSheet;

        public Array<AnimationFrame> frames = new Array<>();

    }

    public static class AnimationFrame
    {
        public Rectangle textureRegion;

        public Array<Rectangle> hitboxes = new Array<>();
        public Array<Rectangle> hurtboxes = new Array<>();

        public String texturePath;
    }
}
