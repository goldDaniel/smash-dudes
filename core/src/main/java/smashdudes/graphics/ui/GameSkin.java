package smashdudes.graphics.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

        TextButton.TextButtonStyle menuButtonStyle = new TextButton.TextButtonStyle();
        menuButtonStyle.up = newDrawable("text_button_up", Color.LIGHT_GRAY);
        menuButtonStyle.down = newDrawable("text_button_down", Color.WHITE);
        menuButtonStyle.over = newDrawable("text_button_over", Color.WHITE);
        menuButtonStyle.font = getFont("text_button_font");
        menuButtonStyle.fontColor = Color.BLACK;
        menuButtonStyle.overFontColor = Color.MAROON;
        add("text_button_main_menu", menuButtonStyle);

        //SETTINGS UI////////////////////////////////////////////////////////////////////////////////////////

        add("checkbox_settings_on", RenderResources.getTexture("ui/grey_boxCheckmark.png"));
        add("checkbox_settings_off", RenderResources.getTexture("ui/grey_box.png"));

        CheckBox.CheckBoxStyle checkboxStyle = new CheckBox.CheckBoxStyle();
        checkboxStyle.checkboxOn = newDrawable("checkbox_settings_on", Color.WHITE);
        checkboxStyle.checkboxOnOver = newDrawable("checkbox_settings_on", Color.LIGHT_GRAY);
        checkboxStyle.checkboxOff = newDrawable("checkbox_settings_off", Color.WHITE);
        checkboxStyle.checkboxOver = newDrawable("checkbox_settings_off", Color.LIGHT_GRAY);
        checkboxStyle.font = getFont("text_button_font");
        checkboxStyle.fontColor = Color.WHITE;

        add("checkbox_settings", checkboxStyle);


        add("slider_settings_background", RenderResources.getTexture("ui/grey_sliderHorizontal.png"));
        add("slider_settings_knob_default", RenderResources.getTexture("ui/grey_sliderDown.png"));

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = newDrawable("slider_settings_background", Color.WHITE);
        sliderStyle.knob = newDrawable("slider_settings_knob_default", Color.WHITE);
        sliderStyle.knobOver = newDrawable("slider_settings_knob_default", Color.LIGHT_GRAY);


        add("slider_settings", sliderStyle);
    }
}
