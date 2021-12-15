package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Utils
{
    public static String chooseFileToLoad()
    {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("json files (*.json)", "json");
        final JFileChooser fc = new JFileChooser(Gdx.files.getLocalStoragePath());
        fc.setDialogTitle("Select a file to load...");
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String path = fc.getSelectedFile().getAbsolutePath();
            return path;
        }

        return null;
    }

    public static String chooseFileToSave()
    {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("json files (*.json)", "json");
        final JFileChooser fc = new JFileChooser(Gdx.files.getLocalStoragePath());
        fc.setDialogTitle("Save your file...");
        fc.setFileFilter(filter);
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String path = fc.getSelectedFile().getAbsolutePath();
            return  path;
        }

        return null;
    }
}