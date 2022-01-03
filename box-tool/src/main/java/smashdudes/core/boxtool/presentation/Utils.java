package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ArrayMap;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.UUID;

public class Utils
{
    private static ArrayMap<Object, UUID> keys = new ArrayMap<>();

    public static String chooseFileToLoad(String... fileExtensions)
    {
        String desc = "(";
        for (String s : fileExtensions)
        {
            desc += "*." + s + ", ";
        }
        desc += ")";
        FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, fileExtensions);
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

    public static String getUniqueKey(Object obj)
    {
        if(!keys.containsKey(obj))
        {
            UUID key = UUID.randomUUID();
            while (keys.containsValue(key, true)) // fuck you daniel
            {
                key = UUID.randomUUID();
            }

            keys.put(obj, UUID.randomUUID());
        }

        return keys.get(obj).toString();
    }
}
