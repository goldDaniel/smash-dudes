package smashdudes.content;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.File;
import java.util.Scanner;

public class LoadContent
{
    public static JsonValue loadJson(String fileName)
    {
        try
        {
            Scanner scanner = new Scanner(new File(fileName));
            StringBuilder stringBuilder = new StringBuilder("");
            while (scanner.hasNextLine())
            {
                stringBuilder.append(scanner.nextLine());
            }

            String string = stringBuilder.toString();

            return new JsonReader().parse(string);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        throw new IllegalStateException("Could not load file: " + fileName);
    }

    public static Array<DTO.Terrain> loadTerrainData(String fileName)
    {
        JsonValue json = loadJson(fileName);
        JsonValue terrainData = json.get("terrain");

        Array<DTO.Terrain> data = new Array<>();
        for (int i = 0; i < terrainData.size; i++)
        {
            DTO.Terrain terrainPiece = new DTO.Terrain();
            terrainPiece.setTerrainData(terrainData.get(i));
            data.add(terrainPiece);
        }

        return data;
    }
}
