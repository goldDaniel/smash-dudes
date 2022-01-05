package smashdudes.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ArrayMap;

public class AudioResources
{
    private static boolean hasInitialized = false;

    private static ArrayMap<String, Sound> soundEffects;
    private static ArrayMap<String, Music> music;

    public static void init()
    {
        if(hasInitialized) throw new IllegalStateException("Render Resources already initialized");
        hasInitialized = true;

        soundEffects = new ArrayMap<>();
        music = new ArrayMap<>();
    }

    public static Music getMusic(String fileName)
    {
        if (music.containsKey(fileName))
        {
            return music.get(fileName);
        }
        else
        {
            Music m = Gdx.audio.newMusic(Gdx.files.internal(fileName));
            music.put(fileName, m);

            return m;
        }
    }

    public static Sound getSoundEffect(String fileName)
    {
        if (soundEffects.containsKey(fileName))
        {
            return soundEffects.get(fileName);
        }
        else
        {
            Sound s = Gdx.audio.newSound(Gdx.files.internal(fileName));
            soundEffects.put(fileName, s);

            return s;
        }
    }
}
