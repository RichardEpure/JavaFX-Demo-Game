import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player
{
    private Rectangle collisionBox;
    private double[] oldPos;
    private ImageView sprite;
    private ANIMATIONS currentAnimation;
    private double speed;

    public Player()
    {
        this.collisionBox = new Rectangle(25, 15, Color.RED);
        this.collisionBox.setVisible(false);
        this.oldPos = new double[2];
        this.currentAnimation = ANIMATIONS.PLAYER_DOWN_IDLE;
        this.sprite = new ImageView(new Image(currentAnimation.cycleFrame()));
        this.speed = 1.5;
        setSpriteSize(30);
        animationLoop();
    }

    private void animationLoop()
    {
        AnimationTimer timer = new AnimationTimer()
        {
            private long prevElapsedTime = 0;
            private double timer1 = 0;
            private ANIMATIONS animation;

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
                if(timer1 >= 0.15)
                {
                    sprite.setImage(new Image(animation.cycleFrame()));
                    timer1=0;
                }

                if(currentAnimation != animation)
                {
                    animation = currentAnimation;
                    sprite.setImage(new Image(animation.cycleFrame()));
                }
            }
        };
        timer.start();
    }

    // Sets which animation set the player will display.
    public void setAnimation(ANIMATIONS animation)
    {
        this.currentAnimation = animation;
    }

    // Returns the animation that is currently being displayed.
    public ANIMATIONS getCurrentAnimation()
    {
        return currentAnimation;
    }

    // Set positions of necessary player elements.
    public void setLayoutX(double x)
    {
        collisionBox.setLayoutX(x);
    }

    public void setLayoutY(double y)
    {
        collisionBox.setLayoutY(y);
    }

    // Move collision box outside of the object it collided with.
    public void collide()
    {
        if((collisionBox.getLayoutY() - oldPos[1]) < 0)
        {
            collisionBox.setLayoutY(collisionBox.getLayoutY()+speed);
        }
        else if((collisionBox.getLayoutY() - oldPos[1]) > 0)
        {
            collisionBox.setLayoutY(collisionBox.getLayoutY()-speed);
        }
        else if((collisionBox.getLayoutX()) - oldPos[0] < 0)
        {
            collisionBox.setLayoutX(collisionBox.getLayoutX()+speed);
        }
        else if(collisionBox.getLayoutX() - oldPos[0] > 0)
        {
            collisionBox.setLayoutX(collisionBox.getLayoutX()-speed);
        }
        else
        {
            collisionBox.setLayoutX(oldPos[0]);
            collisionBox.setLayoutY(oldPos[1]);
        }
    }

    // Set player's sprite location to player's collision box location.
    public void move()
    {
        oldPos[0] = collisionBox.getLayoutX();
        oldPos[1] = collisionBox.getLayoutY();
        sprite.setLayoutX( collisionBox.getLayoutX() - (sprite.getFitWidth()/2) + (collisionBox.getWidth()/2) );
        sprite.setLayoutY( collisionBox.getLayoutY() - sprite.getFitHeight() + (collisionBox.getHeight()+1) );
    }

    // Adds player elements to a pane in order to be displayed.
    public void addToPane(Pane pane)
    {
        pane.getChildren().addAll(collisionBox, sprite);
    }

    // Returns the player's collision box.
    public Rectangle getCollisionBox()
    {
        return collisionBox;
    }

    // Returns the player's sprite.
    public ImageView getSprite()
    {
        return sprite;
    }

    // Sets the sprite width and calculates the new height in order to keep the image height/width ratio.
    private void setSpriteSize(double newWidth)
    {
        double newHeight = (newWidth * sprite.getImage().getHeight()) / sprite.getImage().getWidth();
        sprite.setFitWidth(newWidth);
        sprite.setFitHeight(newHeight);
    }

    // Returns the player's movement speed value.
    public double getSpeed()
    {
        return speed;
    }

    // Sets the player's movement speed value.
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }
}
