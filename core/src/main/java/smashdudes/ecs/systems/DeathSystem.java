package smashdudes.ecs.systems;

import com.badlogic.gdx.utils.Array;
import smashdudes.core.PlayerHandle;
import smashdudes.core.WorldUtils;
import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.*;
import smashdudes.ecs.events.Event;
import smashdudes.ecs.events.RespawnEvent;
import smashdudes.ecs.events.WinEvent;

public class DeathSystem extends GameSystem
{
    private final Array<Entity> alivePlayers = new Array<>();
    private boolean hasFiredWinEvent = false;

    public DeathSystem(Engine engine)
    {
        super(engine);

        registerComponentType(PlayerComponent.class);
        registerComponentType(PositionComponent.class);

        registerEventType(RespawnEvent.class);
        registerEventType(WinEvent.class);
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        PlayerComponent play = entity.getComponent(PlayerComponent.class);
        PositionComponent pos = entity.getComponent(PositionComponent.class);

        boolean attemptRespawn = false;
        if(play.lives <= 0)
        {
            entity.removeAllOtherComponents(PlayerComponent.class, HealthComponent.class, UIComponent.class);
        }
        else if(!WorldUtils.getStageBounds().contains(pos.position))
        {
            play.lives--;
            attemptRespawn = true;
        }

        if(play.lives > 0)
        {
            alivePlayers.add(entity);
            if(attemptRespawn) engine.addEvent(new RespawnEvent(entity, WorldUtils.getRespawnPoint()).setImmediate());
        }
    }

    @Override
    public void postUpdate()
    {
        if(alivePlayers.size == 1 && !hasFiredWinEvent)
        {
            engine.addEvent(new WinEvent(alivePlayers.get(0)));
            hasFiredWinEvent = true;
            //engine.createEntity().addComponent(new GameOverComponent());
        }

        alivePlayers.clear();
    }

    @Override
    public void handleEvent(Event event)
    {
        if(event instanceof RespawnEvent)
        {
            RespawnEvent e = (RespawnEvent)event;
            PositionComponent pos = e.entity.getComponent(PositionComponent.class);
            pos.position.set(e.respawnPoint);
        }
    }
}
