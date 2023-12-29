package smashdudes.core.boxtool.presentation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IdentityMap;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.UUID;

public class Utils
{
    private static final IdentityMap<Object, UUID> keys = new IdentityMap<>();

    public static String chooseFileToLoad(FileHandle path, String... fileExtensions)
    {
        if(!path.isDirectory()) throw new IllegalArgumentException("Filehandle must be a directory");

        StringBuilder desc = new StringBuilder("(");
        for (String s : fileExtensions)
        {
            desc.append("*.").append(s).append(", ");
        }
        desc.append(")");

        FileNameExtensionFilter filter = new FileNameExtensionFilter(desc.toString(), fileExtensions);
        final JFileChooser fc = new JFileChooser(path.toString());

        fc.setDialogTitle("Select a file to load...");
        fc.setFileFilter(filter);

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            return fc.getSelectedFile().getAbsolutePath();
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
            return fc.getSelectedFile().getAbsolutePath();
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
