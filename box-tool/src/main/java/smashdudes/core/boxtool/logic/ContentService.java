package smashdudes.core.boxtool.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import smashdudes.content.DTO;
import smashdudes.core.boxtool.data.ContentRepo;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ContentService
{
    private ContentRepo content = new ContentRepo();

    private String filename = null;
    private DTO.Character character = null;

    public boolean hasLoadedCharacter()
    {
        if((filename == null && character != null) ||
           (filename != null && character == null))
        {
            throw new IllegalStateException("This should never happen. Fix your shit");
        }

        return character != null;
    }

    public DTO.Character getCharacter()
    {
        if(hasLoadedCharacter()) return character;

        throw new IllegalStateException("Cannot get character, has not been loaded");
    }

    public void updateHitboxes(String selectedAnimation, int frameNumber, Array<FloatArray> rectangles)
    {
        Array<Rectangle> rects = character.animations.get(selectedAnimation).frames.get(frameNumber).hitboxes;
        for(int i = 0; i < rectangles.size; i++)
        {
            rects.get(i).x      = rectangles.get(i).get(0);
            rects.get(i).y      = rectangles.get(i).get(1);
            rects.get(i).width  = rectangles.get(i).get(2);
            rects.get(i).height = rectangles.get(i).get(3);
        }
    }

    public void updateHurtboxes(String selectedAnimation, int frameNumber, Array<FloatArray> rectangles)
    {
        Array<Rectangle> rects = character.animations.get(selectedAnimation).frames.get(frameNumber).hurtboxes;
        for(int i = 0; i < rectangles.size; i++)
        {
            rects.get(i).x      = rectangles.get(i).get(0);
            rects.get(i).y      = rectangles.get(i).get(1);
            rects.get(i).width  = rectangles.get(i).get(2);
            rects.get(i).height = rectangles.get(i).get(3);
        }
    }

    public String getFilename()
    {
        return filename;
    }

    public void loadFile()
    {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("json files (*.json)", "json");
        final JFileChooser fc = new JFileChooser(Gdx.files.getLocalStoragePath());
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String path = fc.getSelectedFile().getAbsolutePath();

            filename = path;
            character = content.LoadCharacter(path);
        }
    }


}
