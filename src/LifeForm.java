import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LifeForm extends Animator
{
    private Rectangle collisionBox;
    private double[] oldPos;
    private double speed;
    private int health;
    private STATES state;
    private DIRECTIONS direction;
    protected Weapon weapon;

    public LifeForm(double colW, double colH, double speed, double spriteSize, ANIMATIONS animation)
    {
        super(spriteSize, animation);
        this.collisionBox = new Rectangle(colW, colH, Color.RED);
        this.collisionBox.setVisible(false);
        this.oldPos = new double[2];
        this.speed = speed;
        this.health = 100;
        this.state = STATES.IDLE;
        this.direction = DIRECTIONS.SOUTH;
        this.weapon = new Weapon(6, 6, 2, 10);
    }

    // Initiates an attack
    public void attack()
    {
        weapon.getHitBox().setLayoutY(collisionBox.getLayoutY());
        weapon.getHitBox().setLayoutX(collisionBox.getLayoutX());
        state = STATES.ATTACK;
        weapon.setActive(true);
        positionHitBox();
    }

    // Positions the initial location of the hit box.
    private void positionHitBox()
    {
        weapon.setDirection(direction);
        switch (weapon.getDirection())
        {
            case NORTH:
                weapon.getHitBox().setLayoutY(
                    weapon.getHitBox().getLayoutY() -
                    weapon.getHitBox().getHeight()
                );
                weapon.getHitBox().setLayoutX(
                    weapon.getHitBox().getLayoutX() -
                    (weapon.getHitBox().getWidth()/2) +
                    (collisionBox.getWidth()/2)
                );
                break;
            case EAST:
                weapon.getHitBox().setLayoutY(
                    weapon.getHitBox().getLayoutY() +
                    (collisionBox.getHeight()/2) -
                    (weapon.getHitBox().getHeight()/2)
                );
                weapon.getHitBox().setLayoutX(
                    weapon.getHitBox().getLayoutX() +
                    collisionBox.getWidth()
                );
                break;
            case SOUTH:
                weapon.getHitBox().setLayoutY(
                    weapon.getHitBox().getLayoutY() +
                    collisionBox.getHeight()
                );
                weapon.getHitBox().setLayoutX(
                    weapon.getHitBox().getLayoutX() -
                    (weapon.getHitBox().getWidth()/2) +
                    (collisionBox.getWidth()/2)
                );
                break;
            case WEST:
                weapon.getHitBox().setLayoutY(
                    weapon.getHitBox().getLayoutY() +
                    (collisionBox.getHeight()/2) -
                    (weapon.getHitBox().getHeight()/2)
                );
                weapon.getHitBox().setLayoutX(
                    weapon.getHitBox().getLayoutX() -
                    weapon.getHitBox().getWidth()
                );
                break;
        }
    }

    // Move collision box to its position before any collision occurred.
    public void collide()
    {
        collisionBox.setLayoutX(oldPos[0]);
        collisionBox.setLayoutY(oldPos[1]);
    }

    // Stores the current location of the collision box for reference in pending collisions.
    public void setLastLocation()
    {
        oldPos[0] = collisionBox.getLayoutX();
        oldPos[1] = collisionBox.getLayoutY();
    }

    // Sets the sprite's location to the collision box's location.
    private void moveSprite()
    {
        sprite.setLayoutX( collisionBox.getLayoutX() - (sprite.getFitWidth()/2) + (collisionBox.getWidth()/2) );
        sprite.setLayoutY( collisionBox.getLayoutY() - sprite.getFitHeight() + (collisionBox.getHeight()+1) );
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
        moveSprite();
    }

    // Adds the collisionBox and the sprite elements to a pane in order to be displayed.
    public void addToPane(Pane pane, int row, int col, int tileSize)
    {
        collisionBox.setLayoutX(col*tileSize);
        collisionBox.setLayoutY((row*tileSize));
        moveSprite();
        sprite.viewOrderProperty().bind(sprite.layoutYProperty().multiply(-1));
        pane.getChildren().addAll(collisionBox, sprite);
    }

    // Returns the collision box.
    public Rectangle getCollisionBox()
    {
        return collisionBox;
    }

    // Returns the movement speed value.
    public double getSpeed()
    {
        return speed;
    }

    // Returns how much health this "LifeForm" has.
    public int getHealth()
    {
        return health;
    }

    public STATES getState()
    {
        return state;
    }

    public void setState(STATES state)
    {
        this.state = state;
    }

    // Returns the currently equipped weapon
    public Weapon getWeapon()
    {
        return weapon;
    }

    public DIRECTIONS getDirection()
    {
        return direction;
    }

    public void setDirection(DIRECTIONS direction)
    {
        this.direction = direction;
    }
}
