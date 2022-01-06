package smashdudes.ecs.components;

import smashdudes.core.AttackType;
import smashdudes.ecs.Component;

public class PlayerOnGroundAttackStateComponent extends Component
{
    public final AttackType type;

    public PlayerOnGroundAttackStateComponent(AttackType type)
    {
        this.type = type;
    }
}
