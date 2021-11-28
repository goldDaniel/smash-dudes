package smashdudes.core.boxtool;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher
{
    public static void main(String[] args)
    {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("smash-dudes");
        configuration.setWindowedMode(1280, 720);
        configuration.setResizable(false);

        new Lwjgl3Application(new BoxTool(), configuration);
    }
}