package smashdudes.ecs.systems;

import smashdudes.ecs.Engine;
import smashdudes.ecs.Entity;
import smashdudes.ecs.components.GameOverComponent;

public class GameOverSystem extends GameSystem
{
    public interface IScreenTransition
    {
        void execute();
    }

    private final IScreenTransition transition;

    public GameOverSystem(Engine engine, IScreenTransition transition)
    {
        super(engine);

        registerComponentType(GameOverComponent.class);

        this.transition = transition;
    }

    @Override
    public void updateEntity(Entity entity, float dt)
    {
        GameOverComponent g = entity.getComponent(GameOverComponent.class);

        g.timer -= dt;
        if(g.timer <= 0)
        {
            transition.execute();
        }
    }
}
