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
    private int counter;

    public Weapon(double hitW, double hitH, int damage, int duration)
    {
        this(hitW, hitH, damage,0, 0);
        this.duration = duration;
    }

    public Weapon(double hitW, double hitH, int damage, int range, double speed)
    {
        this.hitBox = new Rectangle(hitW, hitH, Color.BLUE);
        this.damage = damage;
        this.range = range;
        this.distanceTravelled = 0;
        this.speed = speed;
        this.direction = DIRECTIONS.UNDEFINED;
        this.duration = 0;
        this.counter = 0;
    }

    public Rectangle getHitBox()
    {
        return hitBox;
    }

    public int getDamage()
    {
        return damage;
    }

    public int getRange()
    {
        return range;
    }

    public double getSpeed()
    {
        return speed;
    }

    public double getDistanceTravelled()
    {
        return distanceTravelled;
    }

    public void calculateDistance()
    {
        distanceTravelled += speed;
    }

    public void resetDistance()
    {
        distanceTravelled = 0;
    }

    public DIRECTIONS getDirection()
    {
        return direction;
    }

    public void setDirection(DIRECTIONS direction)
    {
        this.direction = direction;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getCounter()
    {
        return counter;
    }

    public void incrementCounter()
    {
        counter += 1;
    }

    public void resetCounter()
    {
        counter = 0;
    }
}
