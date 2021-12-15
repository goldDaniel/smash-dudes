package smashdudes.core.boxtool.presentation.viewmodel;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import smashdudes.content.DTO;

public class VM
{
    public static DTO.Character mapping(VM.Character character)
    {
        DTO.Character result = new DTO.Character();

        result.terrainCollider = character.terrainCollider;
        result.debugDim = character.debugDim;
        result.drawDim = character.drawDim;

        result.jumpStrength = character.jumpStrength;
        result.gravity = character.gravity;
        result.weight = character.weight;

        for(VM.Animation a : character.animations)
        {
            DTO.Animation anim = new DTO.Animation();
            result.animations.add(anim);

            anim.textureFilePath = a.textureFilePath;
            anim.animationName = a.animationName;
            anim.usesSpriteSheet = a.usesSpriteSheet;

            for(VM.AnimationFrame f : a.frames)
            {
                DTO.AnimationFrame frame = new DTO.AnimationFrame();
                anim.frames.add(frame);

                frame.texturePath = f.texturePath;
                frame.textureRegion = f.textureRegion;

                Array<Rectangle> hitboxes = new Array<>();
                frame.hitboxes = hitboxes;
                for(FloatArray r : f.hitboxes)
                {
                    Rectangle rect = new Rectangle();
                    rect.x = r.get(0);
                    rect.y= r.get(1);
                    rect.width = r.get(2);
                    rect.height = r.get(3);
                    hitboxes.add(rect);
                }

                Array<Rectangle> hurtboxes = new Array<>();
                frame.hurtboxes = hurtboxes;
                for(FloatArray r : f.hurtboxes)
                {
                    Rectangle rect = new Rectangle();
                    rect.x = r.get(0);
                    rect.y= r.get(1);
                    rect.width = r.get(2);
                    rect.height = r.get(3);
                    hurtboxes.add(rect);
                }
            }
        }

        return result;
    }

    public static VM.Character mapping(DTO.Character character)
    {
        VM.Character result = new VM.Character();

        result.terrainCollider = character.terrainCollider;
        result.debugDim = character.debugDim;
        result.drawDim = character.drawDim;

        result.jumpStrength = character.jumpStrength;
        result.gravity = character.gravity;
        result.weight = character.weight;

        for(DTO.Animation a : character.animations)
        {
            VM.Animation anim = new VM.Animation();
            result.animations.add(anim);

            anim.textureFilePath = a.textureFilePath;
            anim.animationName = a.animationName;
            anim.usesSpriteSheet = a.usesSpriteSheet;

            for(DTO.AnimationFrame f : a.frames)
            {
                VM.AnimationFrame frame = new VM.AnimationFrame();
                anim.frames.add(frame);

                frame.texturePath = f.texturePath;
                frame.textureRegion = f.textureRegion;

                Array<FloatArray> hitboxes = new Array<>();
                frame.hitboxes = hitboxes;
                for(Rectangle r : f.hitboxes)
                {
                    FloatArray arr = new FloatArray(4);
                    arr.add(r.x);
                    arr.add(r.y);
                    arr.add(r.width);
                    arr.add(r.height);
                    hitboxes.add(arr);
                }

                Array<FloatArray> hurtboxes = new Array<>();
                frame.hurtboxes = hurtboxes;
                for(Rectangle r : f.hurtboxes)
                {
                    FloatArray arr = new FloatArray(4);
                    arr.add(r.x);
                    arr.add(r.y);
                    arr.add(r.width);
                    arr.add(r.height);
                    hurtboxes.add(arr);
                }
            }
        }

        return result;
    }

    public static class Character
    {
        public Array<VM.Animation> animations = new Array<>();

        public Vector2 terrainCollider = new Vector2();
        public Vector2 debugDim = new Vector2();
        public Vector2 drawDim = new Vector2();

        public float jumpStrength;
        public float gravity;
        public float weight;
    }

    public static class Animation
    {
        public String textureFilePath;
        public String animationName;

        public boolean usesSpriteSheet;

        public Array<VM.AnimationFrame> frames = new Array<>();
    }

    public static class AnimationFrame
    {
        public Rectangle textureRegion;

        public Array<FloatArray> hitboxes = new Array<>();
        public Array<FloatArray> hurtboxes = new Array<>();

        public String texturePath;
    }
}