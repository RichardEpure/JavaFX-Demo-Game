public class Human extends Enemy
{
    public Human()
    {
        super(40, 20, 65, ANIMATIONS.PLAYER_DOWN_IDLE, new Weapon(90, 90, 20, 16, 14));
        this.speed = 1.5;
    }

    @Override
    protected void updateMovementAnimation()
    {
        switch(this.direction)
        {
            case NORTH:
                setAnimation(ANIMATIONS.PLAYER_UP_WALK);
                break;
            case EAST:
                setAnimation(ANIMATIONS.PLAYER_RIGHT_WALK);
                break;
            case SOUTH:
                setAnimation(ANIMATIONS.PLAYER_DOWN_WALK);
                break;
            case WEST:
                setAnimation(ANIMATIONS.PLAYER_LEFT_WALK);
                break;
        }
    }

    @Override
    protected void idle()
    {
        state = STATES.IDLE;
        switch(direction)
        {
            case NORTH:
                setAnimation(ANIMATIONS.PLAYER_UP_IDLE);
                break;
            case EAST:
                setAnimation(ANIMATIONS.PLAYER_RIGHT_IDLE);
                break;
            case SOUTH:
                setAnimation(ANIMATIONS.PLAYER_DOWN_IDLE);
                break;
            case WEST:
                setAnimation(ANIMATIONS.PLAYER_LEFT_IDLE);
                break;
        }
    }

    @Override
    protected void startAttackAnimation()
    {
        switch(direction)
        {
            case NORTH:
                setAnimation(ANIMATIONS.PLAYER_ATTACK_UP);
                break;
            case EAST:
                setAnimation(ANIMATIONS.PLAYER_ATTACK_RIGHT);
                break;
            case SOUTH:
                setAnimation(ANIMATIONS.PLAYER_ATTACK_DOWN);
                break;
            case WEST:
                setAnimation(ANIMATIONS.PLAYER_ATTACK_LEFT);
                break;
        }
    }
}
