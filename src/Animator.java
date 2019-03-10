import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Animator
{
    protected ANIMATIONS currentAnimation;
    protected int currentFrame;
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

    protected abstract void animationLoop();

    // Cycles through all the frames of a given animation and goes back to the beginning once it reaches the end.
    protected boolean nextFrame()
    {
        boolean endOfAnimation = false;
        if(currentFrame >= currentAnimation.getFrames().length)
        {
            currentFrame = 0;
            endOfAnimation = true;
        }
        sprite.setImage(new Image(currentAnimation.getFrames()[currentFrame]));
        currentFrame++;
        return endOfAnimation;
    }

    // Sets the sprite width and calculates the new height in order to keep the image height/width ratio.
    private void setSpriteSize(double newWidth)
    {
        double newHeight = (newWidth * sprite.getImage().getHeight()) / sprite.getImage().getWidth();
        sprite.setFitWidth(newWidth);
        sprite.setFitHeight(newHeight);
    }

    // Returns the sprite image
    public ImageView getSprite()
    {
        return sprite;
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
