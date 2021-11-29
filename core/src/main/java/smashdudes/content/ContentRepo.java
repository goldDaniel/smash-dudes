package smashdudes.content;

import com.badlogic.gdx.utils.Json;
import smashdudes.content.DTO;

import java.io.*;
import java.util.Scanner;

public class ContentRepo
{
    public DTO.Character loadCharacter(String filepath)
    {
        Scanner scanner = null;
        try
        {
            scanner = new Scanner( new File(filepath) );
            String jsonStr = scanner.useDelimiter("\\A").next();

            Json json = new Json();

            return json.fromJson(DTO.Character.class, jsonStr);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            scanner.close();
        }

        return null;
    }

    public void saveCharacter(String filepath, DTO.Character character)
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
}
