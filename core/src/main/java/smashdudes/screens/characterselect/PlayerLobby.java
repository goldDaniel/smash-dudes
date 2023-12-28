package smashdudes.screens.characterselect;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.AudioResources;
import smashdudes.core.PlayerHandle;
import smashdudes.core.PlayerLobbyInfo;
import smashdudes.core.input.*;

public class PlayerLobby
{
    public interface OnLeave
    {
        void execute(IGameInputListener input, PlayerHandle handle);
    }

    private final Array<Color> availableColors = new Array<>();
    private final ArrayMap<PlayerHandle, PlayerLobbyInfo> players = new ArrayMap<>();

    private final OnLeave onLeave;

    public PlayerLobby(OnLeave action)
    {
        onLeave = action;
        availableColors.add(Color.GOLDENROD, Color.OLIVE, Color.SALMON, Color.ROYAL);
        availableColors.add(Color.BROWN, Color.FOREST, Color.FIREBRICK, Color.NAVY);
    }

    public void join(CharacterSelectInputAssigner assigner, InputDeviceType device, PlayerHandle handle)
    {
        PlayerLobbyInfo joinedPlayer = new PlayerLobbyInfo();
        joinedPlayer.handle = handle;

        if(device == InputDeviceType.Keyboard)
        {
            joinedPlayer.input = new KeyboardInputListener(new InputConfig(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.DOWN, Input.Keys.Z, Input.Keys.X));
        }
        else if(device == InputDeviceType.Controller)
        {
            joinedPlayer.input = new ControllerInputListener(assigner.GetController(handle));
        }

        joinedPlayer.color = availableColors.first();
        availableColors.removeValue(joinedPlayer.color, false);

        joinedPlayer.selectedCharacterIndex = 0;
        joinedPlayer.playerNumber = players.size + 1;

        players.put(handle, joinedPlayer);
    }

    public Color getPlayerColor(PlayerHandle handle)
    {
        return players.get(handle).color;
    }

    public Array<PlayerLobbyInfo> getPlayers()
    {
        Array<PlayerLobbyInfo> result = new Array<>();

        for(int i = 0; i < players.size; ++i)
        {
            PlayerLobbyInfo player = players.getValueAt(i);
            result.add(player);
        }

        return result;
    }

    public PlayerLobbyInfo getPlayer(PlayerHandle handle)
    {
        return players.get(handle);
    }

    public boolean allPlayersLockedIn()
    {
        if(players.size < 2) return false;

        for(int i = 0; i < players.size; ++i)
        {
            PlayerLobbyInfo player = players.getValueAt(i);
            if(!player.lockedIn) return false;
        }

        return true;
    }

    public void handlePlayerInput()
    {
        Array<PlayerHandle> toRemove = new Array<>();
        for(int i = 0; i < players.size; ++i)
        {
            PlayerLobbyInfo player = players.getValueAt(i);
            boolean cancelPressed = player.input.cancelPressed();
            boolean confirmPressed = player.input.confirmPressed();

            if(!player.lockedIn && confirmPressed)
            {
                player.lockedIn = true;
                AudioResources.getSoundEffect("audio/ui/select.ogg").play();
            }
            else if(player.lockedIn && cancelPressed)
            {
                player.lockedIn = false;
                AudioResources.getSoundEffect("audio/ui/cancel.ogg").play();
            }
            else if(!player.lockedIn && cancelPressed)
            {
                onLeave.execute(player.input, player.handle);
                availableColors.add(player.color);
                toRemove.add(player.handle);
            }
        }

        for(PlayerHandle handle : toRemove)
        {
            players.removeKey(handle);
        }

        for(int i = 0; i < players.size; ++i)
        {
            players.getValueAt(i).playerNumber = i + 1;
        }
    }
}
