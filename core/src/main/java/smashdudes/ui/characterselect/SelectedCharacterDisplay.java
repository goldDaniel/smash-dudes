package smashdudes.ui.characterselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;
import smashdudes.core.PlayerLobbyInfo;
import smashdudes.graphics.RenderResources;
import smashdudes.ui.GameSkin;
import smashdudes.util.CharacterData;

public class SelectedCharacterDisplay extends Table
{
    private final float screenWidth;
    private final float screenHeight;

    private final ArrayMap<PlayerHandle, Stack> playerImageStacks;

    private final ArrayMap<PlayerHandle, Container<Label>> playerReadyLabels;
    private final ArrayMap<PlayerHandle, Integer> previousSelection;

    public SelectedCharacterDisplay(float screenWidth, float screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        playerImageStacks = new ArrayMap<>();
        previousSelection = new ArrayMap<>();
        playerReadyLabels = new ArrayMap<>();
    }

    public void addDisplay(PlayerHandle handle, Color color)
    {
        Stack imageStack = new Stack();

        Image bgImage = new Image(RenderResources.getColor1x1(Color.WHITE));
        bgImage.setColor(color);
        imageStack.add(bgImage);

        Image borderImage = new Image(new Texture("textures/portrait_border.png"));
        imageStack.add(borderImage);

        playerImageStacks.put(handle, imageStack);
        this.add(imageStack).size(192, 192).padLeft(screenWidth / 20).padRight(screenWidth / 20);
    }

    public void removeDisplay(PlayerHandle handle)
    {
        Stack stack = playerImageStacks.removeKey(handle);

        Cell<Stack> cell = this.getCell(stack);
        cell.size(0,0);
        cell.padLeft(0);
        cell.padRight(0);
        stack.remove();
    }

    public void updateSelection(Array<PlayerLobbyInfo> players, Array<CharacterData> characterData)
    {
        for(PlayerLobbyInfo player : players)
        {
            updateLockedInLabel(player);
            updateSelectionPortrait(player, characterData);
        }
    }

    private void updateLockedInLabel(PlayerLobbyInfo player)
    {
        if(player.lockedIn)
        {
            if(!playerReadyLabels.containsKey(player.handle))
            {
                Label readyLabel = new Label("READY", GameSkin.Get(), "splash_continue");
                Container<Label> labelContainer = new Container<>(readyLabel);
                labelContainer.align(Align.center);

                playerImageStacks.get(player.handle).add(labelContainer);
                playerReadyLabels.put(player.handle, labelContainer);
            }
        }
        else
        {
            if(playerReadyLabels.containsKey(player.handle))
            {
                Container<Label> label = playerReadyLabels.removeKey(player.handle);
                label.remove();
            }
        }
    }

    private void updateSelectionPortrait(PlayerLobbyInfo player, Array<CharacterData> characterData)
    {
        if(player.lockedIn) return;

        if(!previousSelection.containsKey(player.handle))
        {
            previousSelection.put(player.handle, -1);
        }

        final int prevIndex = previousSelection.get(player.handle);
        final int currIndex = player.selectedCharacterIndex;

        if(prevIndex != currIndex)
        {
            Image image = new Image(characterData.get(currIndex).texture);
            image.setSize(192,192);
            Stack stack = playerImageStacks.get(player.handle);

            int imageIdx =  stack.getChildren().size - 1;
            if(prevIndex > -1)
            {
                imageIdx--;
                stack.getChild(imageIdx).remove();
            }
            stack.addActorAt(imageIdx, image);
        }

        previousSelection.put(player.handle, currIndex);
    }
}
