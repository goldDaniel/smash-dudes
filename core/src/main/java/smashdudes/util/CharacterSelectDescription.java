package smashdudes.util;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.PlayerHandle;
import smashdudes.core.input.GameInputHandler;
import smashdudes.screens.CharacterSelectScreen;


public class CharacterSelectDescription
{
    public static class PlayerDescription
    {
        public final String identifier;
        public final PlayerHandle handle;

        public PlayerDescription(String id, PlayerHandle p)
        {
            this.identifier = id;
            this.handle = p;
        }
    };


    public Array<PlayerDescription> descriptions;
    public final GameInputHandler gameInput;

    public CharacterSelectDescription(GameInputHandler gameInput, Array<PlayerDescription> players)
    {
        this.gameInput = gameInput;
        this.descriptions = players;
    }
}
