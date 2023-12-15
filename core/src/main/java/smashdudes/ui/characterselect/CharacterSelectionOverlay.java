package smashdudes.ui.characterselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import smashdudes.graphics.RenderResources;
import smashdudes.ui.GameSkin;

public class CharacterSelectionOverlay extends Container<Stack>
{
    public CharacterSelectionOverlay(Color backgroundColor, int playerNumber)
    {
        if(backgroundColor == null) throw new IllegalArgumentException("Color cannot be null!");
        if(playerNumber < 0 || playerNumber > 4) throw new IllegalArgumentException("Player must be between 1 and 4 inclusive!");

        this.minSize(32,32);
        if(playerNumber == 1) this.align(Align.topLeft);
        if(playerNumber == 2) this.align(Align.topRight);
        if(playerNumber == 3) this.align(Align.bottomLeft);
        if(playerNumber == 4) this.align(Align.bottomRight);

        Stack stack = new Stack();
        this.setActor(stack);

        Image bgImage = new Image(RenderResources.getColor1x1(backgroundColor));
        bgImage.setSize(32,32);
        stack.add(bgImage);

        String labelText = "P" + playerNumber;
        Label label = new Label(labelText, GameSkin.Get(), "character_select_selection_overlay");
        Container<Label> labelContainer = new Container<>(label);
        labelContainer.align(Align.center);
        stack.add(labelContainer);
    }
}
