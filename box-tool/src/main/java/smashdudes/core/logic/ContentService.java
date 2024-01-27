package smashdudes.core.logic;

import smashdudes.content.DTO;
import smashdudes.content.ContentRepo;

public class ContentService
{
    public void updateCharacter(DTO.Character character, String path)
    {
        ContentRepo.saveCharacter(path, character);
    }

    public DTO.Character readCharacter(String path)
    {
        return ContentRepo.loadCharacter(path);
    }

    public String createCharacter(String filepath, String name)
    {
        return ContentRepo.createCharacter(filepath, name);
    }
}
