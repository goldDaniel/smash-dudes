package smashdudes.content;

import com.badlogic.gdx.math.Vector2;
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

    public static DTO.Stage loadStage(String filename)
    {
        JsonValue json = loadJson(filename);

        DTO.Stage result = new DTO.Stage();

        result.terrain  = loadTerrainData(json.get("terrain"));
        result.spawnPoints = loadSpawnPoints(json.get("spawn_points"));

        return result;
    }

    public static Array<Vector2> loadSpawnPoints(JsonValue json)
    {
        Array<Vector2> result = new Array<>();
        for (int i = 0; i < json.size; i++)
        {
            Vector2 value = new Vector2();
            value.x = json.get(i).get(0).asFloat();
            value.y = json.get(i).get(1).asFloat();

            result.add(value);
        }

        return result;
    }

    public static Array<DTO.Terrain> loadTerrainData(JsonValue json)
    {
        Array<DTO.Terrain> data = new Array<>();
        for (int i = 0; i < json.size; i++)
        {
            DTO.Terrain terrainPiece = new DTO.Terrain();
            terrainPiece.setTerrainData(json.get(i));
            data.add(terrainPiece);
        }

        return data;
    }
}
