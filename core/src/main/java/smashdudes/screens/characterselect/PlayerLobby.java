package smashdudes.screens.characterselect;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;
import smashdudes.core.input.*;

public class PlayerLobby
{
    private static class JoinedPlayer
    {
        public PlayerHandle handle;
        public IGameInputListener input;
        public int selectedCharacterIndex;
        public Color color;
        public boolean lockedIn = false;
    }

    public static class PlayerID
    {
        public Color color;
        public int playerNumber;

        public int selected = 0;
    }

    public interface OnLeave
    {
        void execute(IGameInputListener input);
    }

    private final Array<Color> availableColors = new Array<>();
    private final ArrayMap<PlayerHandle, JoinedPlayer> players = new ArrayMap<>();

    private final OnLeave onLeave;

    public PlayerLobby(OnLeave action)
    {
        onLeave = action;
        availableColors.add(Color.GOLDENROD, Color.OLIVE, Color.SALMON, Color.ROYAL);
    }

    public void join(CharacterSelectInputAssigner assigner, InputDeviceType device, PlayerHandle handle)
    {
        JoinedPlayer joinedPlayer = new JoinedPlayer();
        joinedPlayer.handle = handle;

        if(device == InputDeviceType.Keyboard)
        {
            joinedPlayer.input = new KeyboardInputListener(new InputConfig(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.Z));
        }
        else if(device == InputDeviceType.Controller)
        {
            joinedPlayer.input = new ControllerInputListener(assigner.GetController(handle));
        }

        joinedPlayer.color = availableColors.first();
        availableColors.removeValue(joinedPlayer.color, false);

        joinedPlayer.selectedCharacterIndex = 0;

        players.put(handle, joinedPlayer);
    }

    public Color getPlayerColor(PlayerHandle handle)
    {
        return players.get(handle).color;
    }

    public void leave(PlayerHandle handle)
    {
        JoinedPlayer player = players.removeKey(handle);
        availableColors.add(player.color);

        onLeave.execute(player.input);
    }

    public Array<PlayerID> getPlayers()
    {
        Array<PlayerID> result = new Array<>();

        for(int i = 0; i < players.size; ++i)
        {
            JoinedPlayer player = players.getValueAt(i);

            PlayerID id = new PlayerID();
            id.color = player.color;
            id.playerNumber = i + 1;

            result.add(id);
        }

        return result;
    }

    public void setLockedIn(PlayerHandle handle, boolean value)
    {
        players.get(handle).lockedIn = value;
    }

    public boolean allPlayersLockedIn()
    {
        for(int i = 0; i < players.size; ++i)
        {
            JoinedPlayer player = players.getValueAt(i);
            if(!player.lockedIn) return false;
        }

        return true;
    }
}
