public class Player extends LifeForm
{
    public Player()
    {
        super(40, 20, 65, ANIMATIONS.PLAYER_DOWN_IDLE, new Weapon(80, 80, 25, 16, 14));
        this.speed = 10;
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

    // Moves the location of the collision box in the specified direction.
    public void move(DIRECTIONS direction)
    {
        this.direction = direction;
        switch(direction)
        {
            case NORTH:
                collisionBox.setLayoutY(collisionBox.getLayoutY() - speed);
                setAnimation(ANIMATIONS.PLAYER_UP_WALK);
                break;
            case EAST:
                collisionBox.setLayoutX(collisionBox.getLayoutX() + speed);
                setAnimation(ANIMATIONS.PLAYER_RIGHT_WALK);
                break;
            case SOUTH:
                collisionBox.setLayoutY(collisionBox.getLayoutY() + speed);
                setAnimation(ANIMATIONS.PLAYER_DOWN_WALK);
                break;
            case WEST:
                collisionBox.setLayoutX(collisionBox.getLayoutX() - speed);
                setAnimation(ANIMATIONS.PLAYER_LEFT_WALK);
                break;
        }
        updatePosition();
    }
}
