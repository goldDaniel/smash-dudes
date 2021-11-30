package smashdudes.core.boxtool.logic;

import com.badlogic.gdx.Gdx;
import smashdudes.content.DTO;
import smashdudes.content.ContentRepo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ContentService
{
    private ContentRepo content = new ContentRepo();

    private String filename = null;
    private DTO.Character character = null;
    private DTO.AnimationFrame frame = null;

    public boolean hasLoadedCharacter()
    {
        if((filename == null && character != null) ||
           (filename != null && character == null))
        {
            throw new IllegalStateException("This should never happen. Fix your shit");
        }

        return character != null;
    }

    public void saveCharacter()
    {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("json files (*.json)", "json");
        final JFileChooser fc = new JFileChooser(Gdx.files.getLocalStoragePath());
        fc.setDialogTitle("Save your file...");
        fc.setFileFilter(filter);

        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String path = fc.getSelectedFile().getAbsolutePath();
            content.saveCharacter(path, character);
        }
    }

    public DTO.Character getCharacter()
    {
        return character;
    }

    public void setFrame(DTO.AnimationFrame frame)
    {
        this.frame = frame;
    }

//    public DTO.AnimationFrame getFrame(int frameNum)
//    {
//        return character.animations;
//    }


    public String getFilename()
    {
        return filename;
    }

    public void loadFile()
    {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("json files (*.json)", "json");
        final JFileChooser fc = new JFileChooser(Gdx.files.getLocalStoragePath());
        fc.setDialogTitle("Select a file to load...");
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String path = fc.getSelectedFile().getAbsolutePath();

            filename = path;
            character = content.loadCharacter(path);
        }
    }
}
