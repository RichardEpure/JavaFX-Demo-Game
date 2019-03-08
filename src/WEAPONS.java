import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public enum WEAPONS
{
    UNARMED(6, 6, 2),
    LONGSWORD(100, 30, 25),
    SHORTBOW(10, 10, 20, 350, 50);

    private Rectangle hitBox;
    private int damage;
    private int range;
    private double speed;

    WEAPONS(double hitW, double hitH, int damage)
    {
        this(hitW, hitH, damage,0, 0);
    }

    WEAPONS(double hitW, double hitH, int damage, int range, double speed)
    {
        this.hitBox = new Rectangle(hitW, hitH, Color.BLUE);
        this.damage = damage;
        this.range = range;
        this.speed = speed;
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
}
