package smashdudes.ecs.systems;

import com.badlogic.gdx.utils.Array;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.DrawComponent;
import smashdudes.ecs.events.AnimationEventBuilder;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;

public class AnimationSystem extends RenderSystem
{
    public AnimationSystem(Engine engine)
    {
        super(engine);
        registerComponentType(AnimationComponent.class);
        registerComponentType(DrawComponent.class);

        registerEventType(RespawnEvent.class);
    }

    @Override
    protected void renderEntity(Entity entity, float dt, float alpha)
    {
        AnimationComponent anim = entity.getComponent(AnimationComponent.class);
        DrawComponent draw = entity.getComponent(DrawComponent.class);

        anim.update(dt);

        Array<Event> frameEvents = AnimationEventBuilder.getFrameEvents(anim.getCurrentFrame().events);
        for(Event e : frameEvents)
        {
            engine.addEvent(e);
        }

        draw.texture = anim.getCurrentFrame().texture;
    }
}
