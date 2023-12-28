package smashdudes.gameplay;

public class GameplayUtils
{
    public static final float MAX_SHIELD_DURATION = 5f;

    public static float DamageToShieldDamage(float damage)
    {
        int ease = 50; // this will need to be tested by hand
        return MAX_SHIELD_DURATION * (damage / (damage + ease));
    }
}
