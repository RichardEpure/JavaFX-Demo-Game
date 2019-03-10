public class Player extends LifeForm
{
    public Player()
    {
        super(40, 20, 3, 40, ANIMATIONS.PLAYER_DOWN_IDLE);
        this.weapon = new Weapon(55, 55, 50, 16, 14);
    }

    protected void endAttackAnimation()
    {
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
