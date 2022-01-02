package smashdudes.ecs.components;

import smashdudes.ecs.Component;
import smashdudes.ecs.Entity;

public class HitResolutionComponent extends Component
{
    public final Entity attacker;
    public final Entity attacked;

    public HitResolutionComponent(Entity attacker, Entity attacked)
    {
        this.attacker = attacker;
        this.attacked = attacked;
    }
}
