package smashdudes.content;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.File;
import java.util.Scanner;

public class LoadContent
{
    public static class TerrainDTO
    {
        public String textureFilePath;
        public Vector2 position = new Vector2();

        public float width, height;

        private void setTerrainData(JsonValue terrainData)
        {
            textureFilePath = terrainData.get("texture").asString();
            position.x = terrainData.get("position").asFloatArray()[0];
            position.y = terrainData.get("position").asFloatArray()[1];

            width = terrainData.get("dim").get("width").asFloat();
            height = terrainData.get("dim").get("height").asFloat();
        }
    }

    public static Array<TerrainDTO> loadTerrainData(String fileName)
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

            JsonValue json = new JsonReader().parse(string);
            JsonValue terrainData = json.get("terrain");

            Array<TerrainDTO> data = new Array<TerrainDTO>();
            for (int i = 0; i < terrainData.size; i++)
            {
                TerrainDTO terrainPiece = new TerrainDTO();
                terrainPiece.setTerrainData(terrainData.get(i));
                data.add(terrainPiece);
            }

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        throw new IllegalStateException("Could not load terrain data");
    }
}
