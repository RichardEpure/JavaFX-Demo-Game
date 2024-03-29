import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class LifeForm extends Animator
{
    protected Rectangle collisionBox;
    private double[] oldPos;
    protected double speed;
    private int health;
    protected STATES state;
    protected DIRECTIONS direction;
    protected Weapon weapon;

    public LifeForm(double colW, double colH, double spriteSize, ANIMATIONS animation, Weapon weapon)
    {
        super(spriteSize, animation);
        this.collisionBox = new Rectangle(colW, colH, Color.RED);
        this.collisionBox.setVisible(false);
        this.oldPos = new double[2];
        this.speed = 5;
        this.health = 100;
        this.state = STATES.IDLE;
        this.direction = DIRECTIONS.SOUTH;
        this.weapon = weapon;
    }

    protected void animationLoop()
    {
        loop = new AnimationTimer()
        {
            private long prevElapsedTime = 0;
            private double timer1 = 0;
            private ANIMATIONS animation;
            private boolean endOfAnimation;

            @Override
            public void handle(long elapsedTime)
            {
                // Calculates how much time has passed between frames.
                if(prevElapsedTime == 0)
                {
                    prevElapsedTime = elapsedTime;
                    return;
                }
                double deltaTime = (elapsedTime - prevElapsedTime) / 1.0e9;
                prevElapsedTime = elapsedTime;
                timer1 += deltaTime;

                // Looped instructions
                // Continuously cycle between animation frames at constant intervals.
                if(timer1 >= 0.05)
                {
                    endOfAnimation = nextFrame();
                    if(endOfAnimation && state == STATES.ATTACK)
                    {
                        idle();
                    }
                    timer1=0;
                }
                // If the animation was changed, ignore timer and switch to new animation.
                if(currentAnimation != animation)
                {
                    animation = currentAnimation;
                    currentFrame = 0;
                    nextFrame();
                }
            }
        };
        loop.start();
    }

    protected abstract void idle();

    protected abstract void startAttackAnimation();

    // Initiates an attack
    public void attack()
    {
        weapon.getHitBox().setLayoutY(collisionBox.getLayoutY());
        weapon.getHitBox().setLayoutX(collisionBox.getLayoutX());
        startAttackAnimation();
        state = STATES.ATTACK;
        positionHitBox();
        weapon.setActive(true);
    }

    // Positions the initial location of the hit box.
    protected void positionHitBox()
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
    protected void updatePosition()
    {
        sprite.setLayoutX( collisionBox.getLayoutX() - (sprite.getFitWidth()/2) + (collisionBox.getWidth()/2) );
        sprite.setLayoutY( collisionBox.getLayoutY() - sprite.getFitHeight() + (collisionBox.getHeight()+1) );
    }

    // Adds the collisionBox and the sprite elements to a pane in order to be displayed.
    public void addToPane(Pane pane, int row, int col, int tileSize)
    {
        collisionBox.setLayoutX(col*tileSize);
        collisionBox.setLayoutY((row*tileSize));
        updatePosition();
        sprite.viewOrderProperty().bind(sprite.layoutYProperty().multiply(-1));
        pane.getChildren().addAll(collisionBox, sprite);
    }

    // Returns the collision box.
    public Rectangle getCollisionBox()
    {
        return collisionBox;
    }

    // Returns how much health this "LifeForm" has.
    public int getHealth()
    {
        return health;
    }

    public void decreaseHealth(int damage)
    {
        this.health -= damage;
    }

    public STATES getState()
    {
        return state;
    }

    // Returns the currently equipped weapon
    public Weapon getWeapon()
    {
        return weapon;
    }

    public void setDirection(DIRECTIONS direction)
    {
        this.direction = direction;
    }
}
