package smashdudes.core.boxtool.presentation.viewmodel;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import imgui.type.ImFloat;
import smashdudes.content.DTO;

public class VM
{
    public static DTO.Character mapping(VM.Character character)
    {
        DTO.Character result = new DTO.Character();

        Rectangle rect = new Rectangle();
        rect.x = character.terrainCollider.get(0);
        rect.y = character.terrainCollider.get(1);
        rect.width = character.terrainCollider.get(2);
        rect.height = character.terrainCollider.get(3);
        result.terrainCollider = rect;

        result.scale = character.scale;

        result.jumpStrength = character.jumpStrength;
        result.gravity = character.gravity;
        result.weight = character.weight;
        result.name = character.name;

        for(VM.Animation a : character.animations)
        {
            result.animations.add(mapping(a));
        }

        return result;
    }

    public static VM.Character mapping(DTO.Character character)
    {
        VM.Character result = new VM.Character();

        FloatArray arr = new FloatArray();
        arr.add(character.terrainCollider.x);
        arr.add(character.terrainCollider.y);
        arr.add(character.terrainCollider.width);
        arr.add(character.terrainCollider.height);
        result.terrainCollider = arr;

        result.scale = character.scale;

        result.jumpStrength = character.jumpStrength;
        result.gravity = character.gravity;
        result.weight = character.weight;
        result.name = character.name;

        for(DTO.Animation a : character.animations)
        {
            result.animations.add(mapping(a));
        }
        return result;
    }

    public static DTO.Animation mapping(VM.Animation a)
    {
        DTO.Animation anim = new DTO.Animation();

        anim.textureFilePath = a.textureFilePath;
        anim.animationName = a.animationName;
        anim.usesSpriteSheet = a.usesSpriteSheet;

        for(VM.AnimationFrame f : a.frames)
        {
            anim.frames.add(mapping(f));
        }

        return anim;
    }

    public static VM.Animation mapping(DTO.Animation a)
    {
        VM.Animation anim = new VM.Animation();

        anim.textureFilePath = a.textureFilePath;
        anim.animationName = a.animationName;
        anim.usesSpriteSheet = a.usesSpriteSheet;

        for(DTO.AnimationFrame f : a.frames)
        {
            anim.frames.add(mapping(f));
        }

        return anim;
    }

    public static DTO.AnimationFrame mapping(VM.AnimationFrame f)
    {
        DTO.AnimationFrame frame = new DTO.AnimationFrame();

        frame.texturePath = f.texturePath;
        frame.textureRegion = f.textureRegion;

        Array<Rectangle> hitboxes = new Array<>();
        frame.hitboxes = hitboxes;
        for(FloatArray r : f.hitboxes)
        {
            Rectangle rect = new Rectangle();
            rect.x = r.get(0);
            rect.y = r.get(1);
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
            rect.y = r.get(1);
            rect.width = r.get(2);
            rect.height = r.get(3);
            hurtboxes.add(rect);
        }

        return frame;
    }


    public static VM.AnimationFrame mapping(DTO.AnimationFrame f)
    {
        VM.AnimationFrame frame = new VM.AnimationFrame();

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

        return frame;
    }

    public static class Character
    {
        public Array<VM.Animation> animations = new Array<>();

        public FloatArray terrainCollider = new FloatArray();
        public Vector2 debugDim = new Vector2();
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