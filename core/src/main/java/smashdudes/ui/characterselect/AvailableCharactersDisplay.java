package smashdudes.ui.characterselect;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import smashdudes.core.AudioResources;
import smashdudes.core.PlayerLobbyInfo;
import smashdudes.core.input.IMenuInputRetriever;
import smashdudes.util.CharacterData;

public class AvailableCharactersDisplay extends Table
{
    final int charactersPerRow = 4;
    private final Array<CharacterSlot> characterEntries;

    public AvailableCharactersDisplay()
    {
        characterEntries = new Array<>();
    }

    public void insertCharacter(CharacterData data)
    {
        CharacterSlot selectableCharacter = new CharacterSlot(data);

        characterEntries.add(selectableCharacter);

        this.add(selectableCharacter);
        if((characterEntries.size) % charactersPerRow == 0) {
            this.row();
        }
    }

    public void updateSelection(Array<PlayerLobbyInfo> players)
    {
        for(int i = 0; i < characterEntries.size; ++i)
        {
            characterEntries.get(i).resetSelection();
        }

        for(PlayerLobbyInfo info : players)
        {
            updatePlayerSelection(info);
            characterEntries.get(info.selectedCharacterIndex).addSelection(new CharacterSelectionOverlay(info.color, info.playerNumber));
        }
    }

    private void updatePlayerSelection(PlayerLobbyInfo player)
    {
        if(!player.lockedIn)
        {
            player.selectedCharacterIndex = getPlayerSelection(player.input, player.selectedCharacterIndex);
        }
    }

    private int getPlayerSelection(IMenuInputRetriever input, int currentIndex)
    {
        boolean playSound = false;

        if(input.leftPressed())
        {
            playSound = true;
            if(currentIndex % charactersPerRow == 0)
            {
                currentIndex += charactersPerRow;
            }
            currentIndex--;
        }
        if(input.rightPressed())
        {
            playSound = true;

            currentIndex++;
            if(currentIndex % charactersPerRow == 0)
            {
                currentIndex -= charactersPerRow;
            }
        }
        if(input.upPressed())
        {
            playSound = true;

            currentIndex -= charactersPerRow;
            if(currentIndex < 0)
            {
                currentIndex += characterEntries.size;
            }
        }
        if(input.downPressed())
        {
            playSound = true;

            currentIndex += charactersPerRow;
            if(currentIndex > (characterEntries.size - 1))
            {
                currentIndex -= characterEntries.size;
            }
        }

        if(playSound)
        {
            AudioResources.getSoundEffect("audio/ui/navigate.ogg").play();
        }

        return currentIndex;
    }
}
