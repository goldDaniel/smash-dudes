package smashdudes.ui.characterselect;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import smashdudes.screens.characterselect.PlayerLobbyInfo;
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
            characterEntries.get(info.selectedCharacterIndex).addSelection(new CharacterSelectionOverlay(info.color, info.playerNumber));
        }
    }
}
