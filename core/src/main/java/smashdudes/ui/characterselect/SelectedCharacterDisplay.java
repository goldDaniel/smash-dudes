package smashdudes.ui.characterselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.core.PlayerHandle;
import smashdudes.graphics.RenderResources;

public class SelectedCharacterDisplay extends Table
{
    private final float screenWidth;
    private final float screenHeight;

    private final ArrayMap<PlayerHandle, Stack> playerImageStacks;

    public SelectedCharacterDisplay(float screenWidth, float screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        playerImageStacks = new ArrayMap<>();
    }

    public void addDisplay(PlayerHandle handle, Color color)
    {
        Stack imageStack = new Stack();

        Image bgImage = new Image(RenderResources.getColor1x1(color));
        bgImage.setSize(192, 192);
        imageStack.add(bgImage);

        Image borderImage = new Image(new Texture("textures/portrait_border.png"));
        borderImage.setSize(192,192);
        imageStack.add(borderImage);

        playerImageStacks.put(handle, imageStack);
        this.add(imageStack).size(192,192).padLeft(screenWidth / 20).padRight(screenWidth / 20);
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
}
