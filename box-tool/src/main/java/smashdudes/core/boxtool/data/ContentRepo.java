package smashdudes.core.boxtool.data;

import smashdudes.content.DTO;
import smashdudes.content.LoadContent;

public class ContentRepo
{
    public DTO.Character LoadCharacter(String filepath)
    {
        return LoadContent.loadCharacterData(filepath);
    }
}
