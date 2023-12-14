package smashdudes.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import smashdudes.graphics.RenderResources;

public class CharacterData
{
    public String name = null;
    public Texture texture = null;
    public String jsonData = null;

    public static CharacterData LoadCharacter(FileHandle characterDirectory)
    {
        CharacterData character = new CharacterData();
        character.name = characterDirectory.nameWithoutExtension();

        // find character portrait and data
        String jsonFilename = character.name + ".json";
        for(FileHandle entry : characterDirectory.list())
        {
            if(entry.name().equals("portrait.png"))
            {
                character.texture = RenderResources.getTexture(entry.path());
            }

            if(entry.name().equals(jsonFilename))
            {
                character.jsonData = entry.readString();
            }
        }

        // validate
        if(character.name == null) throw new IllegalStateException("Character Name not set!");
        if(character.texture == null) throw new IllegalStateException("Character Texture not loaded!");
        if(character.jsonData == null) throw new IllegalStateException("Character Data not loaded!");

        return character;
    }
}
