import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Animator
{
    private ANIMATIONS currentAnimation;
    private int currentFrame;
    protected ImageView sprite;
    protected AnimationTimer loop;

    public Animator(double spriteSize, ANIMATIONS animation)
    {
        this.currentAnimation = animation;
        this.currentFrame = 0;
        this.sprite = new ImageView(new Image(currentAnimation.getFrames()[currentFrame]));
        setSpriteSize(spriteSize);
        animationLoop();
    }

    private void animationLoop()
    {
        loop = new AnimationTimer()
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
                // Continuously cycle between animation frames at constant intervals.
                if(timer1 >= 0.15)
                {
                    nextFrame();
                    timer1=0;
                }
                // If the animation was changed, ignore timer and switch to new animation.
                if(currentAnimation != animation)
                {
                    animation = currentAnimation;
                    nextFrame();
                }
            }
        };
        loop.start();
    }

    private void nextFrame()
    {
        currentFrame++;
        if(currentFrame >= currentAnimation.getFrames().length)
        {
            currentFrame = 0;
        }
        sprite.setImage(new Image(currentAnimation.getFrames()[currentFrame]));
    }

    // Sets the sprite width and calculates the new height in order to keep the image height/width ratio.
    private void setSpriteSize(double newWidth)
    {
        double newHeight = (newWidth * sprite.getImage().getHeight()) / sprite.getImage().getWidth();
        sprite.setFitWidth(newWidth);
        sprite.setFitHeight(newHeight);
    }

    // Sets the animation that will be displayed.
    public void setAnimation(ANIMATIONS animation)
    {
        this.currentAnimation = animation;
    }

    // Returns the animation that is currently being displayed.
    public ANIMATIONS getCurrentAnimation()
    {
        return currentAnimation;
    }
}
