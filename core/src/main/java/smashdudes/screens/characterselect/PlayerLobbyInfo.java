package smashdudes.screens.characterselect;

import com.badlogic.gdx.graphics.Color;
import smashdudes.core.PlayerHandle;
import smashdudes.core.input.IGameInputListener;

public class PlayerLobbyInfo
{
    public PlayerHandle handle;
    public IGameInputListener input;
    public int selectedCharacterIndex;
    public int playerNumber;
    public Color color;
    public boolean lockedIn = false;
}
