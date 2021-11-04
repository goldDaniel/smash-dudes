package smashdudes.content;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    public AssetManager manager = new AssetManager();

    public static final AssetDescriptor<Texture> someTexture =
            new AssetDescriptor<Texture>("images/sometexture.png", Texture.class);

    public static final AssetDescriptor<TextureAtlas> uiAtlas =
            new AssetDescriptor<TextureAtlas>("ui/uiskin.pack", TextureAtlas.class);

    public static final AssetDescriptor<Skin> uiSkin =
            new AssetDescriptor<Skin>("ui/uiskin.json", Skin.class,
                    new SkinLoader.SkinParameter("ui/uiskin.pack"));

    public void load()
    {
        manager.load(someTexture);
        manager.load(uiAtlas);
        manager.load(uiSkin);
    }

    public void dispose()
    {
        manager.dispose();
    }
}
