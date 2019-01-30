import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player
{
    private Rectangle collisionBox;
    private double[] oldPos;
    private ImageView idle;

    public Player()
    {
        this.collisionBox = new Rectangle(25, 1, Color.RED);
        this.oldPos = new double[2];
        this.idle = new ImageView(new Image("resources/down_stand.png"));
        this.idle.setOpacity(0.5);
        setSpriteSize(30);
    }

    public void setLayoutX(double x)
    {
        collisionBox.setLayoutX(x);
    }

    public void setLayoutY(double y)
    {
        collisionBox.setLayoutY(y);
    }

    public void collide()
    {
        if((collisionBox.getLayoutY() - oldPos[1]) < 0)
        {
            collisionBox.setLayoutY(collisionBox.getLayoutY()+1);
        }
        else if((collisionBox.getLayoutY() - oldPos[1]) > 0)
        {
            collisionBox.setLayoutY(collisionBox.getLayoutY()-1);
        }
        else if((collisionBox.getLayoutX()) - oldPos[0] < 0)
        {
            collisionBox.setLayoutX(collisionBox.getLayoutX()+1);
        }
        else if(collisionBox.getLayoutX() - oldPos[0] > 0)
        {
            collisionBox.setLayoutX(collisionBox.getLayoutX()-1);
        }
    }

    public void move()
    {
        oldPos[0] = collisionBox.getLayoutX();
        oldPos[1] = collisionBox.getLayoutY();
        idle.setLayoutX( collisionBox.getLayoutX() - (idle.getFitWidth()/2) + (collisionBox.getWidth()/2) );
        idle.setLayoutY( collisionBox.getLayoutY() - idle.getFitHeight() + (collisionBox.getHeight()+1) );
    }

    public Rectangle getCollisionBox()
    {
        return collisionBox;
    }

    public ImageView getSprite()
    {
        return idle;
    }

    public void addToPane(Pane pane)
    {
        pane.getChildren().addAll(collisionBox, idle);
    }

    private void setSpriteSize(double newWidth)
    {
        double newHeight = (newWidth * idle.getImage().getHeight()) / idle.getImage().getWidth();
        idle.setFitWidth(newWidth);
        idle.setFitHeight(newHeight);
    }
}
