package smashdudes.particletool;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class ParticletoolEntry
{
    public static void main(String[] args)
    {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Smash Dudes Particle Editor");
        configuration.setWindowedMode(1900, 1020);
        configuration.setMaximized(true);

        new Lwjgl3Application(new ParticleToolUI(), configuration);
    }
}

