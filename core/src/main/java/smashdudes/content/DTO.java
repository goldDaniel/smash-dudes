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

        public Vector2 terrainCollider = new Vector2();

        public float jumpStrength;
        public float gravity;
        public float weight;

        protected void setPlayerData(JsonValue characterData)
        {
            jumpStrength = characterData.get("jump_strength").asFloat();
            gravity = characterData.get("gravity").asFloat();
            weight = characterData.get("weight").asFloat();

            terrainCollider.x = characterData.get("terrain_collider").asFloatArray()[0];
            terrainCollider.y = characterData.get("terrain_collider").asFloatArray()[1];

            JsonValue animationData = characterData.get("animations");
            for (int i = 0; i < animationData.size; i++)
            {
                Animation animation = new Animation();
                animation.setAnimation(animationData.get(i));

                String name = animationData.get(i).get("animation_name").asString();

                animations.add(animation);
            }
        }
    }

    public static class Animation
    {
        public String textureFilePath;
        public String animationName;

        public boolean usesSpriteSheet;

        public Array<AnimationFrame> frames = new Array<>();

        protected void setAnimation(JsonValue animationData)
        {
            usesSpriteSheet = animationData.get("uses_sprite_sheet").asBoolean();
            animationName = animationData.get("animation_name").asString();

            if (usesSpriteSheet)
            {
                textureFilePath = animationData.get("texture_path").asString();
            }

            JsonValue animationFrameData = animationData.get("frames");
            for (int i = 0; i < animationFrameData.size; i++)
            {
                AnimationFrame frame = new AnimationFrame();
                frame.setAnimationFrame(animationFrameData.get(i), usesSpriteSheet);
                frames.add(frame);
            }
        }
    }

    public static class AnimationFrame
    {
        public Rectangle textureRegion;

        public Array<Rectangle> hitboxes = new Array<>();
        public Array<Rectangle> hurtboxes = new Array<>();

        public String texturePath;

        protected void setAnimationFrame(JsonValue animationFrameData, boolean usesSpriteSheet)
        {
            if (usesSpriteSheet)
            {
                float[] textureData = animationFrameData.get("texture_region").asFloatArray();
                textureRegion = new Rectangle(textureData[0], textureData[1], textureData[2], textureData[3]);
            }
            else
            {
                texturePath = animationFrameData.get("file_path").asString();
            }

            JsonValue hitboxData = animationFrameData.get("hitboxes");
            for (int i = 0; i < hitboxData.size; i++)
            {
                float[] hitboxValues = hitboxData.get(i).asFloatArray();
                Rectangle hitbox = new Rectangle(hitboxValues[0], hitboxValues[1], hitboxValues[2], hitboxValues[3]);
                hitboxes.add(hitbox);
            }

            JsonValue hurtboxData = animationFrameData.get("hurtboxes");
            for (int i = 0; i < hurtboxData.size; i++)
            {
                float[] hurtboxValues = hurtboxData.get(i).asFloatArray();
                Rectangle hurtbox = new Rectangle(hurtboxValues[0], hurtboxValues[1], hurtboxValues[2], hurtboxValues[3]);
                hurtboxes.add(hurtbox);
            }
        }
    }
}
