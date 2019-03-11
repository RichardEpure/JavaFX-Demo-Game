import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Enemy extends LifeForm
{
    private Circle detectionRadius;
    private Rectangle attackRange;

    public Enemy(double colW, double colH, double spriteSize, ANIMATIONS animation, Weapon weapon)
    {
        super(colW, colH, spriteSize, animation, weapon);
        this.detectionRadius = new Circle(0, 0, 400, Color.YELLOW);
        this.speed = 8;
        detectionRadius.setVisible(true);
        detectionRadius.setViewOrder(-999999);
        detectionRadius.setOpacity(0.05);
        initAttackRange();
    }

    private void initAttackRange()
    {
        this.attackRange = new Rectangle();
        this.attackRange.setVisible(false);
        this.attackRange.widthProperty().bind(weapon.getHitBox().widthProperty());
        this.attackRange.heightProperty().bind(weapon.getHitBox().heightProperty());
        this.attackRange.layoutXProperty().bind(collisionBox.layoutXProperty().add(collisionBox.getWidth()/2).subtract(attackRange.getWidth()/2));
        this.attackRange.layoutYProperty().bind(collisionBox.layoutYProperty().add(collisionBox.getHeight()/2).subtract(attackRange.getHeight()/2));
    }

    // Move in a random direction.
    public void wander()
    {
        state = STATES.WANDERING;
        AnimationTimer loop = new AnimationTimer()
        {
            private long prevElapsedTime = 0;
            private double timer1 = 0;
            private double theta = ThreadLocalRandom.current().nextDouble(0, 360);
            private double xForce = speed * Math.cos(Math.toRadians(theta));
            private double yForce = speed * Math.sin(Math.toRadians(theta)) * -1;
            private int counter = 0;

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
                if(timer1 >= (double)1/120)
                {
                    setLastLocation();
                    collisionBox.setLayoutX(collisionBox.getLayoutX() + xForce);
                    collisionBox.setLayoutY(collisionBox.getLayoutY() + yForce);
                    if(theta < 45 || theta >= 315)
                    {
                        setDirection(DIRECTIONS.EAST);
                    }
                    else if(theta < 135 && theta >= 45)
                    {
                        setDirection(DIRECTIONS.NORTH);
                    }
                    else if(theta < 225 && theta >= 135)
                    {
                        setDirection(DIRECTIONS.WEST);
                    }
                    else if(theta < 315 && theta >= 225)
                    {
                        setDirection(DIRECTIONS.SOUTH);
                    }
                    updateMovementAnimation();
                    updatePosition();
                    counter++;
                    timer1=0;
                }

                if(counter > 150 || state != STATES.WANDERING)
                {
                    if(state == STATES.WANDERING)
                    {
                        state = STATES.IDLE;
                    }
                    this.stop();
                }
            }
        };
        loop.start();
    }

    // Follow a lifeForm in a linear path.
    public void chase(LifeForm lifeForm)
    {
        state = STATES.CHASING;
        AnimationTimer loop = new AnimationTimer()
        {
            private long prevElapsedTime = 0;
            private double timer1 = 0;
            private double x;
            private double y;
            private double theta;
            private double xForce;
            private double yForce;

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
                if(timer1 >= (double)1/120)
                {
                    setLastLocation();

                    x = lifeForm.getCollisionBox().getLayoutX() - collisionBox.getLayoutX();
                    y = lifeForm.getCollisionBox().getLayoutY() - collisionBox.getLayoutY();
                    theta = Math.atan(y/x);
                    xForce = Math.signum(x) * speed * Math.cos(theta);
                    yForce = Math.signum(x) * speed * Math.sin(theta);

                    collisionBox.setLayoutX(collisionBox.getLayoutX() + xForce);
                    collisionBox.setLayoutY(collisionBox.getLayoutY() + yForce);
                    if(x > 40)
                    {
                        setDirection(DIRECTIONS.EAST);
                    }
                    else if(x < -40)
                    {
                        setDirection(DIRECTIONS.WEST);
                    }
                    else if(y >= 0)
                    {
                        setDirection(DIRECTIONS.SOUTH);
                    }
                    else if(y < 0)
                    {
                        setDirection(DIRECTIONS.NORTH);
                    }
                    updateMovementAnimation();
                    updatePosition();
                    timer1=0;
                }

                if(state != STATES.CHASING)
                {
                    state = STATES.IDLE;
                    this.stop();
                }
            }
        };
        loop.start();
    }

    protected abstract void updateMovementAnimation();

    @Override
    public void updatePosition()
    {
        super.updatePosition();
        detectionRadius.setLayoutX(collisionBox.getLayoutX());
        detectionRadius.setLayoutY(collisionBox.getLayoutY());
    }

    // Adds the collisionBox and the sprite elements to a pane in order to be displayed.
    @Override
    public void addToPane(Pane pane, int row, int col, int tileSize)
    {
        super.addToPane(pane, row, col, tileSize);
        pane.getChildren().addAll(detectionRadius, attackRange);
    }

    // Returns the circle which the class uses to detect the player.
    public Circle getDetectionRadius()
    {
        return detectionRadius;
    }

    public Rectangle getAttackRange()
    {
        return attackRange;
    }

    @Override
    protected void positionHitBox()
    {
        weapon.getHitBox().setLayoutX(collisionBox.getLayoutX() + collisionBox.getWidth()/2 - weapon.getHitBox().getWidth()/2);
        weapon.getHitBox().setLayoutY(collisionBox.getLayoutY() + collisionBox.getHeight()/2 - weapon.getHitBox().getHeight()/2);
    }
}
