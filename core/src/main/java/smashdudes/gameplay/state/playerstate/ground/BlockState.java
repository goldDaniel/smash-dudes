package smashdudes.gameplay.state.playerstate.ground;

import smashdudes.ecs.Entity;
import smashdudes.ecs.components.AnimationComponent;
import smashdudes.ecs.components.BlockComponent;
import smashdudes.ecs.components.CharacterInputComponent;
import smashdudes.ecs.events.BlockBreakEvent;
import smashdudes.ecs.events.Event;
import smashdudes.gameplay.state.State;

public class BlockState extends PlayerGroundState
{
    public BlockState(Entity entity)
    {
        super(entity);
    }

    @Override
    public void onEnter()
    {
        super.onEnter();
        entity.getComponent(BlockComponent.class).isEnabled = true;
    }

    @Override
    public void innerUpdate(float dt)
    {

    }

    @Override
    public void onExit()
    {
        entity.getComponent(BlockComponent.class).isEnabled = false;
    }

    @Override
    public State getNextState()
    {
        CharacterInputComponent i = entity.getComponent(CharacterInputComponent.class);
        if(!i.currentState.block)
        {
            return new GroundIdleState(entity);
        }

        return this;
    }

    @Override
    public State handleEvent(Event event)
    {
        State state = super.handleEvent(event);
        if(state == this && event instanceof BlockBreakEvent)
        {
            return new BlockStunnedState(entity, 3f);
        }

        return state;
    }
}
