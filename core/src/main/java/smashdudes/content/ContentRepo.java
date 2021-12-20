package smashdudes.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.io.*;
import java.util.Scanner;

public class ContentRepo
{
    public static DTO.Character loadCharacter(String filepath)
    {
        Scanner scanner = null;
        try
        {
            scanner = new Scanner( new File(filepath) );
            String jsonStr = scanner.useDelimiter("\\A").next();

            Json json = new Json();

            scanner.close();
            return json.fromJson(DTO.Character.class, jsonStr);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
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

    public static void createCharacter(String filepath, String name)
    {
        DTO.Character character = new DTO.Character();
        character.name = name;
        File file = new File(name + ".json");
        try
        {
            if (file.createNewFile())
            {
                saveCharacter(filepath + name + ".json", character);
            }
            else
            {
                //throw error window. Use while loop and scanner?
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteCharacter(DTO.Character character)
    {

    }
}
