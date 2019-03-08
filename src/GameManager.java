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

        initialiseLifeForms();
        centerViewOnPlayer();
        createPaneResizeListeners();
        createKeyListeners();
        gameLoop();
    }

    private void initialiseLifeForms()
    {
        player = new Player();
        player.addToPane(levelManager.getPane(), 2, 2, levelManager.getTileSize());
        levelManager.addLifeForm(player);

        LifeForm x = new LifeForm(25, 15, 3, 30, ANIMATIONS.PLAYER_DOWN_IDLE);
        x.addToPane(levelManager.getPane(), 3, 2, levelManager.getTileSize());
        levelManager.addLifeForm(x);
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
                    checkAllCollisions();
                    checkAllAttacks();
                    test();
                    if(player.getState() == STATES.IDLE || player.getWeapon().getRange() > 0)
                    {
                        playerMovement();
                        if (!isPlayerColliding())
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
    private boolean isPlayerColliding()
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

    private void test()
    {
        for(int i=0; i<levelManager.getCollidableElements().size(); i++)
        {
            if(player.getCollisionBox().getBoundsInParent().intersects(player.getWeapon().getHitBox().getBoundsInParent()))
            {
                System.out.println("COLLIDE");
            }
        }
    }

    // Handles the collisions of every "LifeForm" instance within a level.
    private void checkAllCollisions()
    {
        for(int i=0; i<levelManager.getLifeForms().size(); i++)
        {
            for (int j = 0; j<levelManager.getCollidableElements().size(); j++)
            {
                if (levelManager.getLifeForms().get(i).getCollisionBox().getBoundsInParent().intersects(levelManager.getCollidableElements().get(j).getBoundsInParent()))
                {
                    levelManager.getLifeForms().get(i).collide();
                }
            }
        }
    }

    // Handles all combat between "LifeForm"s.
    private void checkAllAttacks()
    {
        for(int i=0; i<levelManager.getLifeForms().size(); i++)
        {
            if(levelManager.getLifeForms().get(i).getState() == STATES.ATTACK)
            {
                LifeForm lifeForm = levelManager.getLifeForms().get(i);

                // Checks if the hit box is in the scene, if it isn't, add it in.
                if(!levelManager.getPane().getChildren().contains(lifeForm.getWeapon().getHitBox()))
                {
                    levelManager.getPane().getChildren().add(lifeForm.getWeapon().getHitBox());
                }
                // Checks what direction the attack is executed at and store it for future reference.
                if(lifeForm.getWeapon().getDirection() == DIRECTIONS.UNDEFINED)
                {
                    lifeForm.getWeapon().setDirection(lifeForm.getDirection());
                }
                // Positions the initial location of the hit box.
                if(lifeForm.getWeapon().getCounter() == 0)
                {
                    positionHitBox(lifeForm);
                }
                // Launch the attack in the appropriate direction.
                if(lifeForm.getWeapon().getDistanceTravelled() < lifeForm.getWeapon().getRange() && lifeForm.getWeapon().getRange() > 0)
                {
                    switch(lifeForm.getWeapon().getDirection())
                    {
                        case NORTH:
                            lifeForm.getWeapon().getHitBox().setLayoutY(lifeForm.getWeapon().getHitBox().getLayoutY() - lifeForm.getWeapon().getSpeed());
                            break;
                        case EAST:
                            lifeForm.getWeapon().getHitBox().setLayoutX(lifeForm.getWeapon().getHitBox().getLayoutX() + lifeForm.getWeapon().getSpeed());
                            break;
                        case SOUTH:
                            lifeForm.getWeapon().getHitBox().setLayoutY(lifeForm.getWeapon().getHitBox().getLayoutY() + lifeForm.getWeapon().getSpeed());
                            break;
                        case WEST:
                            lifeForm.getWeapon().getHitBox().setLayoutX(lifeForm.getWeapon().getHitBox().getLayoutX() - lifeForm.getWeapon().getSpeed());
                            break;
                    }
                    lifeForm.getWeapon().calculateDistance();
                    lifeForm.getWeapon().incrementCounter();
                }
                else if(lifeForm.getWeapon().getRange() == 0 && lifeForm.getWeapon().getCounter() <= lifeForm.getWeapon().getDuration())
                {
                    lifeForm.getWeapon().incrementCounter();
                }
                else
                {
                    levelManager.getPane().getChildren().remove(lifeForm.getWeapon().getHitBox());
                    lifeForm.getWeapon().resetDistance();
                    lifeForm.getWeapon().setDirection(DIRECTIONS.UNDEFINED);
                    lifeForm.getWeapon().resetCounter();
                    lifeForm.setState(STATES.IDLE);
                }
            }
        }
    }

    // Positions the initial location of the hit box.
    private void positionHitBox(LifeForm lifeForm)
    {
        switch (lifeForm.getWeapon().getDirection())
        {
            case NORTH:
                lifeForm.getWeapon().getHitBox().setLayoutY(
                    lifeForm.getWeapon().getHitBox().getLayoutY() -
                    lifeForm.getWeapon().getHitBox().getHeight()
                );
                lifeForm.getWeapon().getHitBox().setLayoutX(
                    lifeForm.getWeapon().getHitBox().getLayoutX() -
                    (lifeForm.getWeapon().getHitBox().getWidth()/2) +
                    (lifeForm.getCollisionBox().getWidth()/2)
                );
                break;
            case EAST:
                lifeForm.getWeapon().getHitBox().setLayoutY(
                    lifeForm.getWeapon().getHitBox().getLayoutY() +
                    (lifeForm.getCollisionBox().getHeight()/2) -
                    (lifeForm.getWeapon().getHitBox().getHeight()/2)
                );
                lifeForm.getWeapon().getHitBox().setLayoutX(
                    lifeForm.getWeapon().getHitBox().getLayoutX() +
                    lifeForm.getCollisionBox().getWidth()
                );
                break;
            case SOUTH:
                lifeForm.getWeapon().getHitBox().setLayoutY(
                    lifeForm.getWeapon().getHitBox().getLayoutY() +
                    lifeForm.getCollisionBox().getHeight()
                );
                lifeForm.getWeapon().getHitBox().setLayoutX(
                    lifeForm.getWeapon().getHitBox().getLayoutX() -
                    (lifeForm.getWeapon().getHitBox().getWidth()/2) +
                    (lifeForm.getCollisionBox().getWidth()/2)
                );
                break;
            case WEST:
                lifeForm.getWeapon().getHitBox().setLayoutY(
                    lifeForm.getWeapon().getHitBox().getLayoutY() +
                    (lifeForm.getCollisionBox().getHeight()/2) -
                    (lifeForm.getWeapon().getHitBox().getHeight()/2)
                );
                lifeForm.getWeapon().getHitBox().setLayoutX(
                    lifeForm.getWeapon().getHitBox().getLayoutX() -
                    lifeForm.getWeapon().getHitBox().getWidth()
                );
                break;
        }
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
        player.setLastLocation();
        if(isLeftKeyPressed && !isRightKeyPressed)
        {
            player.setDirection(DIRECTIONS.WEST);
            player.getCollisionBox().setLayoutX(player.getCollisionBox().getLayoutX()-player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_LEFT_WALK) { player.setAnimation(ANIMATIONS.PLAYER_LEFT_WALK); }
        }
        if(isRightKeyPressed && !isLeftKeyPressed)
        {
            player.setDirection(DIRECTIONS.EAST);
            player.getCollisionBox().setLayoutX(player.getCollisionBox().getLayoutX()+player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_RIGHT_WALK) { player.setAnimation(ANIMATIONS.PLAYER_RIGHT_WALK); }
        }
        if(isUpKeyPressed && !isDownKeyPressed)
        {
            player.setDirection(DIRECTIONS.NORTH);
            player.getCollisionBox().setLayoutY(player.getCollisionBox().getLayoutY()-player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_UP_WALK) { player.setAnimation(ANIMATIONS.PLAYER_UP_WALK); }
        }
        if(isDownKeyPressed && !isUpKeyPressed)
        {
            player.setDirection(DIRECTIONS.SOUTH);
            player.getCollisionBox().setLayoutY(player.getCollisionBox().getLayoutY()+player.getSpeed());
            if(player.getCurrentAnimation() != ANIMATIONS.PLAYER_DOWN_WALK) { player.setAnimation(ANIMATIONS.PLAYER_DOWN_WALK); }
        }
        if( (isDownKeyPressed && isUpKeyPressed) || (isLeftKeyPressed && isRightKeyPressed) )
        {
            switch(player.getCurrentAnimation())
            {
                case PLAYER_LEFT_IDLE:
                case PLAYER_LEFT_WALK:
                    player.setDirection(DIRECTIONS.WEST);
                    player.setAnimation(ANIMATIONS.PLAYER_LEFT_IDLE);
                    break;

                case PLAYER_RIGHT_IDLE:
                case PLAYER_RIGHT_WALK:
                    player.setDirection(DIRECTIONS.EAST);
                    player.setAnimation(ANIMATIONS.PLAYER_RIGHT_IDLE);
                    break;

                case PLAYER_UP_IDLE:
                case PLAYER_UP_WALK:
                    player.setDirection(DIRECTIONS.NORTH);
                    player.setAnimation(ANIMATIONS.PLAYER_UP_IDLE);
                    break;

                case PLAYER_DOWN_IDLE:
                case PLAYER_DOWN_WALK:
                default:
                    player.setDirection(DIRECTIONS.SOUTH);
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

                case SPACE:
                    if(player.getState() != STATES.ATTACK)
                    {
                        player.getWeapon().getHitBox().setLayoutY(player.getCollisionBox().getLayoutY());
                        player.getWeapon().getHitBox().setLayoutX(player.getCollisionBox().getLayoutX());
                        player.setState(STATES.ATTACK);
                    }
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
