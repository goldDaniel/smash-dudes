package smashdudes.ui.characterselect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
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

        float size = 32;

        float rotationAngle = 0;
        this.minSize(size,size);
        this.maxSize(size,size);

        if(playerNumber == 1)
        {
            this.align(Align.topLeft);
            rotationAngle = 0;
        }
        else if(playerNumber == 2)
        {
            this.align(Align.topRight);
            rotationAngle = -MathUtils.PI / 2 * MathUtils.radiansToDegrees;
        }
        else if(playerNumber == 3)
        {
            this.align(Align.bottomLeft);
            rotationAngle = MathUtils.PI / 2 * MathUtils.radiansToDegrees;
        }
        else if(playerNumber == 4)
        {
            this.align(Align.bottomRight);
            rotationAngle = MathUtils.PI * MathUtils.radiansToDegrees;
        }

        Stack stack = new Stack();
        this.setActor(stack);

        Image bgImage = new Image(RenderResources.getTexture("textures/selector_background.png"));
        bgImage.setColor(backgroundColor);
        bgImage.setOrigin(size / 2, size / 2);
        bgImage.setRotation(rotationAngle);
        stack.add(bgImage);

        Image borderImage = new Image(RenderResources.getTexture("textures/selector_border.png"));
        borderImage.setOrigin(size / 2, size / 2);
        borderImage.setRotation(rotationAngle);
        stack.add(borderImage);

        String labelText = "P" + playerNumber;
        Label label = new Label(labelText, GameSkin.Get(), "character_select_selection_overlay");
        Container<Label> labelContainer = new Container<>(label);
        labelContainer.align(Align.center);
        stack.add(labelContainer);
    }
}
