package smashdudes.ecs.components;

import smashdudes.ecs.Component;
import smashdudes.gameplay.BodyBox;
import smashdudes.gameplay.GameplayUtils;

public class BlockComponent extends Component
{
    public BodyBox blockBox;
    public float shieldDuration = GameplayUtils.MAX_SHIELD_DURATION;
    public boolean isEnabled = false;
}
