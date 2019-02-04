import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyEvent;

public class GameManager extends ViewManager
{
    private LevelManager levelManager;
    private Player player;

    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isDownKeyPressed;

    // If no dimension arguments provided for the constructor, set dimensions to default values (800x800).
    public GameManager(LevelManager levelManager)
    {
        this(800, 800, levelManager);
    }

    // Constructor
    public GameManager(int width, int height, LevelManager levelManager)
    {
        super(width, height);
        this.pane.setStyle("-fx-background-color: #1f1f1f");

        this.levelManager = levelManager;
        this.levelManager.addSubSceneToPane(pane);

        this.isLeftKeyPressed = false;
        this.isRightKeyPressed = false;
        this.isUpKeyPressed = false;
        this.isDownKeyPressed = false;

        this.player = new Player();
        this.player.addToPane(levelManager.getPane(), 2, 2, levelManager.getTileSize());

        centerViewOnPlayer();
        createPaneResizeListeners();
        createKeyListeners();
        gameLoop();
    }

    // Method that initiates a loop which cycles every frame.
    private void gameLoop()
    {
        AnimationTimer loop = new AnimationTimer()
        {
            private long prevElapsedTime = 0;
            private double timer1 = 0;

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
                // Every 120th of a second, carry out player movement/collisions.
                if(timer1 >= (double)1/120)
                {
                    if(isColliding())
                    {
                        player.collide();
                    }
                    else
                    {
                        player.setLastLocation();
                        playerMovement();
                        if(!isColliding())
                        {
                            centerViewOnPlayer();
                        }
                    }
                    timer1=0;
                }
            }
        };
        loop.start();
    }

    // Checks whether the playerColBox is about to collide with anything.
    private boolean isColliding()
    {
        for(int i=0; i<levelManager.getCollidableElements().size(); i++)
        {
            if(player.getCollisionBox().getBoundsInParent().intersects(levelManager.getCollidableElements().get(i).getBoundsInParent()))
            {
                return true;
            }
        }
        return false;
    }

    // Makes the player always in view at the center of the pane.
    private void centerViewOnPlayer()
    {
        levelManager.getScene().setLayoutX((pane.getWidth()/2) - player.getCollisionBox().getLayoutX());
        levelManager.getScene().setLayoutY((pane.getHeight()/2) - player.getCollisionBox().getLayoutY());
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
        player.moveSprite();
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

    // Creates listeners which carry out instructions whenever the pane is resized.
    @SuppressWarnings("unchecked")
    private void createPaneResizeListeners()
    {
        pane.heightProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue obersvable, Object oldValue, Object newValue)
            {
                double deltaHeight = (double)newValue - (double)oldValue;
                levelManager.getScene().setLayoutY(levelManager.getScene().getLayoutY() + deltaHeight/2);
            }
        });

        pane.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue obersvable, Object oldValue, Object newValue)
            {
                double deltaWidth = (double)newValue - (double)oldValue;
                levelManager.getScene().setLayoutX(levelManager.getScene().getLayoutX() + deltaWidth/2);
            }
        });
    }
}
