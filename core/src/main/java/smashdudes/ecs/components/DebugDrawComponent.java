package smashdudes.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import smashdudes.ecs.Component;

public class DebugDrawComponent extends Component
{
    public class DebugRect
    {
        private Color color;
        private Rectangle rect;

        public DebugRect(Color color, Rectangle rect)
        {
            this.rect = rect;
            this.color = color;
        }

        public Color getColor() { return color; }
        public Rectangle getRect() { return rect; }
    }

    private ArrayMap<ShapeRenderer.ShapeType, Array<DebugRect>> rectangles = new ArrayMap<>();

    public DebugDrawComponent()
    {
        for(ShapeRenderer.ShapeType type : ShapeRenderer.ShapeType.values())
        {
            rectangles.put(type, new Array<>());
        }
    }

    public Array<DebugRect> get(ShapeRenderer.ShapeType type)
    {
        return rectangles.get(type);
    }

    public void pushShape(ShapeRenderer.ShapeType type, Rectangle rect, Color color)
    {
        rectangles.get(type).add(new DebugRect(color, rect));
    }

    public void reset()
    {
        for(ShapeRenderer.ShapeType type : ShapeRenderer.ShapeType.values())
        {
            rectangles.get(type).clear();
        }
    }
}
