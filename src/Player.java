public class Player extends LifeForm
{
    public Player()
    {
        super(25, 15, 3, 30, ANIMATIONS.PLAYER_DOWN_IDLE);
        this.weapon = WEAPONS.SHORTBOW;
    }
}
