package smashdudes.content;

import com.badlogic.gdx.utils.Json;

import java.io.*;
import java.util.Scanner;

public class ContentRepo
{
    public static DTO.Character loadCharacter(String filepath)
    {
        try
        {
            Scanner scanner = new Scanner( new File(filepath) );
            String jsonStr = scanner.useDelimiter("\\A").next();

            Json json = new Json();

            scanner.close();
            return json.fromJson(DTO.Character.class, jsonStr);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("File not found: " + filepath);
    }

    public static void saveCharacter(String filepath, DTO.Character character)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));

            Json json = new Json();
            writer.write(json.toJson(character));

            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String createCharacter(String filepath, String name)
    {
        DTO.Character character = new DTO.Character();
        character.name = name;
        File file = new File("characters/" + name + ".json");
        try
        {
            if (file.createNewFile())
            {
                String path = filepath + name + ".json";
                saveCharacter(path, character);
                return path;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
