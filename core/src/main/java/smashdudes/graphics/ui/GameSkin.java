package smashdudes.graphics.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import smashdudes.graphics.RenderResources;

public class GameSkin extends Skin
{
    public GameSkin()
    {
        //SPLASH SCREEN UI/////////////////////////////////////////////////////////////////////////////////
        add("splash_title_font", RenderResources.getFont("rexlia", 128));
        add("splash_continue_font", RenderResources.getFont("rexlia", 32));

        add("splash_title", new Label.LabelStyle(getFont("splash_title_font"), Color.WHITE));
        add("splash_continue", new Label.LabelStyle(getFont("splash_continue_font"), Color.WHITE));



        //MAIN MENU UI/////////////////////////////////////////////////////////////////////////////////////
        add("text_button_font", RenderResources.getFont("rexlia", 32));

        add("text_button_up", RenderResources.getTexture("ui/grey_button01.png"));
        add("text_button_down", RenderResources.getTexture("ui/grey_button02.png"));
        add("text_button_over", RenderResources.getTexture("ui/grey_button03.png"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = newDrawable("text_button_up", Color.LIGHT_GRAY);
        textButtonStyle.down = newDrawable("text_button_down", Color.WHITE);
        textButtonStyle.over = newDrawable("text_button_over", Color.WHITE);
        textButtonStyle.font = getFont("text_button_font");
        textButtonStyle.fontColor = Color.BLACK;
        add("default", textButtonStyle);
    }
}
