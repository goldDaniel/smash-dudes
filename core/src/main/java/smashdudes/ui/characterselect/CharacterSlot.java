package smashdudes.ui.characterselect;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import smashdudes.ui.GameSkin;
import smashdudes.util.CharacterData;

public class CharacterSlot extends Table
{
    private final Stack overlayStack;
    private final Array<CharacterSelectionOverlay> selectionOverlay;

    public CharacterSlot(CharacterData data)
    {
        // we want the character selectors to only overlap the image
        // not the text. So the stack only contains the image
        overlayStack = new Stack();

        Image characterImage = new Image(data.texture);
        Label characterName = new Label(data.name, GameSkin.Get(), "splash_continue");

        selectionOverlay = new Array<>();

        overlayStack.add(characterImage);
        this.add(overlayStack).width(128).height(128).row();
        this.add(characterName);

        this.pad(20);
    }

    public void resetSelection()
    {
        while(selectionOverlay.notEmpty())
        {
            CharacterSelectionOverlay overlay = selectionOverlay.pop();
            overlayStack.removeActor(overlay);
        }
    }

    public void addSelection(CharacterSelectionOverlay overlay)
    {
        selectionOverlay.add(overlay);
        overlayStack.add(overlay);
    }
}
