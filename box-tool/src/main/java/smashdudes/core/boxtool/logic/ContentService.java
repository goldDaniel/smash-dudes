package smashdudes.core.boxtool.logic;

import smashdudes.content.DTO;
import smashdudes.content.ContentRepo;

public class ContentService
{
    private ContentRepo content = new ContentRepo();

    public void updateCharacter(DTO.Character character, String path)
    {
        content.saveCharacter(path, character);
    }

    public DTO.Character readCharacter(String path)
    {
        return content.loadCharacter(path);
    }
}
