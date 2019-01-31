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
    private ArrayList<Rectangle> collidableElements;
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
        player.getSprite().setViewOrder(1);
        player.addToPane(pane);

        addElement(ELEMENT.NORTHWALL, 0, 0, 2);
        addElement(ELEMENT.FLOOR, 2, 0, 2);
        addElement(ELEMENT.FLOOR, 4, 0, 2);
        addElement(ELEMENT.SOUTHWALL, 6, 3, 2);
        addElement(ELEMENT.SOUTHWALL, 6, 5, 2);
        addElement(ELEMENT.SIDEWALL, 4,5, 2 );
        addElement(ELEMENT.NORTHWALL, 8, 3, 2);
    }

    // Method that initiates a loop which cycles every frame.
    private void gameLoop()
    {
        AnimationTimer timer = new AnimationTimer()
        {
            @Override
            public void handle(long elapsedTime)
            {
                if(isColliding())
                {
                    player.collide();
                }
                else
                {
                    player.move();
                    playerMovement();
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

    // Method that handles player movement and corresponding animations.
    private void playerMovement()
    {
        if(isLeftKeyPressed && !isRightKeyPressed)
        {
            player.getCollisionBox().setLayoutX(player.getCollisionBox().getLayoutX()-player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_LEFT_WALK) { player.setAnimation(ANIMATIONS.PLAYER_LEFT_WALK); }
        }
        if(isRightKeyPressed && !isLeftKeyPressed)
        {
            player.getCollisionBox().setLayoutX(player.getCollisionBox().getLayoutX()+player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_RIGHT_WALK) { player.setAnimation(ANIMATIONS.PLAYER_RIGHT_WALK); }
        }
        if(isUpKeyPressed && !isDownKeyPressed)
        {
            player.getCollisionBox().setLayoutY(player.getCollisionBox().getLayoutY()-player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_UP_WALK) { player.setAnimation(ANIMATIONS.PLAYER_UP_WALK); }
        }
        if(isDownKeyPressed && !isUpKeyPressed)
        {
            player.getCollisionBox().setLayoutY(player.getCollisionBox().getLayoutY()+player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_DOWN_WALK) { player.setAnimation(ANIMATIONS.PLAYER_DOWN_WALK); }
        }
        if( (isDownKeyPressed && isUpKeyPressed) || (isLeftKeyPressed && isRightKeyPressed) )
        {
            switch(player.getCurrentAnimation())
            {
                case PLAYER_LEFT_IDLE:
                case PLAYER_LEFT_WALK:
                    player.setAnimation(ANIMATIONS.PLAYER_LEFT_IDLE);
                    break;

                case PLAYER_RIGHT_IDLE:
                case PLAYER_RIGHT_WALK:
                    player.setAnimation(ANIMATIONS.PLAYER_RIGHT_IDLE);
                    break;

                case PLAYER_UP_IDLE:
                case PLAYER_UP_WALK:
                    player.setAnimation(ANIMATIONS.PLAYER_UP_IDLE);
                    break;

                case PLAYER_DOWN_IDLE:
                case PLAYER_DOWN_WALK:
                default:
                    player.setAnimation(ANIMATIONS.PLAYER_DOWN_IDLE);
                    break;
            }
        }
    }

    // Adds a new node representing a tile to the panel using grid-based logic.
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
        image.setViewOrder(element.getViewOrder());
        if(element.isCollidable())
        {
            Rectangle collisionBox = new Rectangle(0,0, Color.RED);
            switch(element)
            {
                case SOUTHWALL:
                    int sizeFactor = 10;
                    collisionBox.layoutYProperty().bind(pane.heightProperty().divide(rows).multiply(row).add(sizeFactor));
                    collisionBox.layoutXProperty().bind(pane.widthProperty().divide(columns).multiply(col));
                    collisionBox.heightProperty().bind(pane.heightProperty().divide(rows/span).subtract(sizeFactor));
                    collisionBox.widthProperty().bind(pane.widthProperty().divide(columns/span));
                    break;

                default:
                    collisionBox.layoutYProperty().bind(pane.heightProperty().divide(rows).multiply(row));
                    collisionBox.layoutXProperty().bind(pane.widthProperty().divide(columns).multiply(col));
                    collisionBox.heightProperty().bind(pane.heightProperty().divide(rows/span));
                    collisionBox.widthProperty().bind(pane.widthProperty().divide(columns/span));
                    break;
            }
            collisionBox.setVisible(false);
            collidableElements.add(collisionBox);
            pane.getChildren().add(collisionBox);
        }
        pane.getChildren().addAll(image);
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
                    if(!isDownKeyPressed && !isRightKeyPressed && !isUpKeyPressed)
                    {
                        player.setAnimation(ANIMATIONS.PLAYER_LEFT_IDLE);
                    }
                    break;

                case RIGHT:
                    isRightKeyPressed = false;
                    if(!isLeftKeyPressed && !isDownKeyPressed && !isUpKeyPressed)
                    {
                        player.setAnimation(ANIMATIONS.PLAYER_RIGHT_IDLE);
                    }
                    break;

                case UP:
                    isUpKeyPressed = false;
                    if(!isDownKeyPressed && !isLeftKeyPressed && !isRightKeyPressed)
                    {
                        player.setAnimation(ANIMATIONS.PLAYER_UP_IDLE);
                    }
                    break;

                case DOWN:
                    isDownKeyPressed = false;
                    if(!isLeftKeyPressed && !isRightKeyPressed && !isUpKeyPressed)
                    {
                        player.setAnimation(ANIMATIONS.PLAYER_DOWN_IDLE);
                    }
                    break;
            }
        });
    }
}
