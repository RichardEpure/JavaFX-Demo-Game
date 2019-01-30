import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Level extends ViewManager
{
    private int rows;
    private int columns;
    private ArrayList<ImageView> collidableElements;
    private Player player;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    public Level(int width, int height, int rows, int columns)
    {
        super(width, height);
        this.rows = rows;
        this.columns = columns;
        this.collidableElements = new ArrayList<>();
        this.isLeftKeyPressed = false;
        this.isRightKeyPressed = false;
        this.isUpKeyPressed = false;
        this.isDownKeyPressed = false;

        this.stage.maxHeightProperty().bind(this.scene.widthProperty());
        this.stage.minHeightProperty().bind(this.scene.widthProperty());

        createKeyListeners();
        test();
        gameLoop();
    }

    private void test()
    {
        player = new Player();
        player.setLayoutX(WIDTH/2);
        player.setLayoutY(HEIGHT/2);
        player.addToPane(pane);

        addElement(ELEMENT.NORTHWALL, 0, 0, 2);
        addElement(ELEMENT.FLOOR, 2, 0, 2);
        addElement(ELEMENT.FLOOR, 4, 0, 2);
        addElement(ELEMENT.SOUTHWALL, 6, 3, 2);
        addElement(ELEMENT.SOUTHWALL, 6, 5, 2);
        addElement(ELEMENT.SIDEWALL, 4,5, 2 );

        player.getCollisionBox().toFront();
        player.getSprite().toFront();
    }

    // Method that initiates a loop which cycles every frame.
    private void gameLoop()
    {
        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long l)
            {
                if(isColliding())
                {
                    player.collide();
                }
                else
                {
                    player.move();
                    playerColBoxMovement();
                }
            }
        };
        timer.start();
    }

    // Checks whether the playerColBox is about to collide with anything.
    private boolean isColliding()
    {
        for(int i=0; i<collidableElements.size(); i++)
        {
            if(player.getCollisionBox().getBoundsInParent().intersects(collidableElements.get(i).getBoundsInParent()))
            {
                return true;
            }
        }
        return false;
    }

    // Method that handles playerColBox movement.
    private void playerColBoxMovement()
    {
        if(isLeftKeyPressed && !isRightKeyPressed)
        {
            player.getCollisionBox().setLayoutX(player.getCollisionBox().getLayoutX()-3);
        }
        if(isRightKeyPressed && !isLeftKeyPressed)
        {
            player.getCollisionBox().setLayoutX(player.getCollisionBox().getLayoutX()+3);
        }
        if(isUpKeyPressed && !isDownKeyPressed)
        {
            player.getCollisionBox().setLayoutY(player.getCollisionBox().getLayoutY()-3);
        }
        if(isDownKeyPressed && !isUpKeyPressed)
        {
            player.getCollisionBox().setLayoutY(player.getCollisionBox().getLayoutY()+3);
        }
    }

    // Adds a new node to the panel using grid-based logic.
    private void addElement(ELEMENT element, int row, int col)
    {
        addElement(element, row, col, 1);
    }

    private void addElement(ELEMENT element, int row, int col, int span)
    {
        ImageView image = new ImageView(new Image(element.getImage()));
        image.layoutYProperty().bind(pane.heightProperty().divide(rows).multiply(row));
        image.layoutXProperty().bind(pane.widthProperty().divide(columns).multiply(col));
        image.fitHeightProperty().bind(pane.heightProperty().divide(rows/span));
        image.fitWidthProperty().bind(pane.widthProperty().divide(columns/span));
        if(element.isCollidable()) { collidableElements.add(image); }
        pane.getChildren().add(image);
    }

    // Keyboard input listeners.
    private void createKeyListeners()
    {
        scene.setOnKeyPressed((KeyEvent event) ->
        {
            switch(event.getCode())
            {
                case LEFT:
                    isLeftKeyPressed = true;
                    break;

                case RIGHT:
                    isRightKeyPressed = true;
                    break;

                case UP:
                    isUpKeyPressed = true;
                    break;

                case DOWN:
                    isDownKeyPressed = true;
                    break;

                case ESCAPE:
                    stage.close();
                    break;
            }
        });

        scene.setOnKeyReleased((KeyEvent event) ->
        {
            switch(event.getCode())
            {
                case LEFT:
                    isLeftKeyPressed = false;
                    break;

                case RIGHT:
                    isRightKeyPressed = false;
                    break;

                case UP:
                    isUpKeyPressed = false;
                    break;

                case DOWN:
                    isDownKeyPressed = false;
                    break;
            }
        });
    }
}
