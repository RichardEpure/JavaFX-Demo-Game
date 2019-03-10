import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Weapon
{
    private Rectangle hitBox;
    private int damage;
    private int range;
    private double distanceTravelled;
    private double speed;
    private DIRECTIONS direction;
    private int duration;
    private int delay;
    private int counter;
    private boolean active;
    private boolean damaging;

    public Weapon(double hitW, double hitH, int damage, int duration, int delay)
    {
        this(hitW, hitH, damage,0, 0, delay);
        this.duration = duration;
    }

    public Weapon(double hitW, double hitH, int damage, int range, double speed, int delay)
    {
        this.hitBox = new Rectangle(hitW, hitH, Color.BLUE);
        this.hitBox.setVisible(false);
        this.damage = damage;
        this.range = range;
        this.distanceTravelled = 0;
        this.speed = speed;
        this.direction = DIRECTIONS.UNDEFINED;
        this.duration = 0;
        this.counter = 0;
        this.active = false;
        this.delay = delay;
        this.damaging = false;
    }

    // Returns the delay between the hit box becoming active and it being damaging.
    public int getDelay()
    {
        return delay;
    }

    // Returns the hit box.
    public Rectangle getHitBox()
    {
        return hitBox;
    }

    // Returns how much 'damage' the hit box can apply to a 'lifeForm'.
    public int getDamage()
    {
        return damage;
    }

    // Returns how far the hit box can travel.
    public int getRange()
    {
        return range;
    }

    // Returns the speed at which the hit box travels.
    public double getSpeed()
    {
        return speed;
    }

    // Returns the total amount of distance travelled of the hit box.
    public double getDistanceTravelled()
    {
        return distanceTravelled;
    }

    // Calculates how far the hit box has travelled since it became active.
    public void calculateDistance()
    {
        distanceTravelled += speed;
    }

    // Returns the direction of motion of the hit box.
    public DIRECTIONS getDirection()
    {
        return direction;
    }

    // Sets how long the hit box will be present in the scene.
    public void setDirection(DIRECTIONS direction)
    {
        this.direction = direction;
    }

    // Returns how long the hit box will be present in the scene.
    public int getDuration()
    {
        return duration;
    }

    // Returns the value of the counter.
    public int getCounter()
    {
        return counter;
    }

    // Increments a counter.
    public void incrementCounter()
    {
        counter += 1;
    }

    // Enables/Disables the hit box.
    public void setActive(boolean active)
    {
        this.active = active;
    }

    // Checks if the motion of the hit box is active.
    public boolean isActive()
    {
        return active;
    }

    // Checks whether the hit box of the weapon can apply damage.
    public boolean isDamaging()
    {
        return damaging;
    }

    // Sets whether the hit box of the weapon can apply damage.
    public void setDamaging(boolean isDamaging)
    {
        this.damaging = isDamaging;
    }

    // Resets the hit box in preparation for the next attack.
    public void reset()
    {
        active = false;
        damaging = false;
        distanceTravelled = 0;
        direction = DIRECTIONS.UNDEFINED;
        counter = 0;
    }
}
