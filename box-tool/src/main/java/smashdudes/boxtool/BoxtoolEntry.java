package smashdudes.boxtool;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class BoxtoolEntry
{
    public static void main(String[] args)
    {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Smash Dudes Character Editor");
        configuration.setWindowedMode(1900, 1020);
        configuration.setMaximized(true);

        new Lwjgl3Application(new BoxToolUI(), configuration);
    }
}

