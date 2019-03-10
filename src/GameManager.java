import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

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
        player.addToPane(levelManager.getPane(), 6, 6, levelManager.getTileSize());
        levelManager.addLifeForm(player);

        LifeForm x = new Player();
        x.addToPane(levelManager.getPane(), 9, 9, levelManager.getTileSize());
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
                    executeAttacks();
                    checkHitBoxCollisions();
                    if(player.getState() == STATES.IDLE)
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

    // Checks if a hit box intersects with a lifeForm's collision box and carries out the appropriate actions.
    private void checkHitBoxCollisions()
    {
        for(int i=0; i<levelManager.getLifeForms().size(); i++)
        {
            LifeForm lifeForm1 = levelManager.getLifeForms().get(i);
            for(int j=0; j<levelManager.getLifeForms().size(); j++)
            {
                LifeForm lifeForm2 = levelManager.getLifeForms().get(j);
                if(lifeForm1.getWeapon().getHitBox().getBoundsInParent().intersects(lifeForm2.getCollisionBox().getBoundsInParent()) && lifeForm1 != lifeForm2 && lifeForm1.getWeapon().isDamaging())
                {
                    levelManager.getPane().getChildren().remove(lifeForm1.getWeapon().getHitBox());
                    lifeForm1.getWeapon().reset();
                    lifeForm2.decreaseHealth(lifeForm1.getWeapon().getDamage());
                    if(lifeForm2.getHealth() <= 0)
                    {
                        levelManager.getPane().getChildren().remove(lifeForm2.getCollisionBox());
                        levelManager.getPane().getChildren().remove(lifeForm2.getSprite());
                        levelManager.getLifeForms().remove(lifeForm2);
                    }
                }
            }
        }
    }

    // Handles all combat between "LifeForm"s.
    private void executeAttacks()
    {
        for(int i=0; i<levelManager.getLifeForms().size(); i++)
        {
            LifeForm lifeForm = levelManager.getLifeForms().get(i);
            Weapon weapon = lifeForm.getWeapon();
            Rectangle hitBox = weapon.getHitBox();
            double speed = weapon.getSpeed();

            if(weapon.isActive())
            {
                // Checks if the hit box is in the scene, if it isn't, add it in after a delay.
                if(!levelManager.getPane().getChildren().contains(hitBox) && weapon.getCounter() > weapon.getDelay())
                {
                    levelManager.getPane().getChildren().add(hitBox);
                    weapon.setDamaging(true);
                }
                // Launch the attack in the appropriate direction.
                if(weapon.getDistanceTravelled() < weapon.getRange() && weapon.getRange() > 0)
                {
                    switch(weapon.getDirection())
                    {
                        case NORTH:
                            hitBox.setLayoutY(hitBox.getLayoutY() - speed);
                            break;
                        case EAST:
                            hitBox.setLayoutX(hitBox.getLayoutX() + speed);
                            break;
                        case SOUTH:
                            hitBox.setLayoutY(hitBox.getLayoutY() + speed);
                            break;
                        case WEST:
                            hitBox.setLayoutX(hitBox.getLayoutX() - speed);
                            break;
                    }
                    weapon.calculateDistance();
                }
                else if (weapon.getCounter() > weapon.getDuration() || weapon.getRange() != 0)
                {
                    levelManager.getPane().getChildren().remove(hitBox);
                    weapon.reset();
                }
                weapon.incrementCounter();
            }
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
            player.move(DIRECTIONS.WEST);
        }
        if(isRightKeyPressed && !isLeftKeyPressed)
        {
            player.move(DIRECTIONS.EAST);
        }
        if(isUpKeyPressed && !isDownKeyPressed)
        {
            player.move(DIRECTIONS.NORTH);
        }
        if(isDownKeyPressed && !isUpKeyPressed)
        {
            player.move(DIRECTIONS.SOUTH);
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
                    if(player.getState() != STATES.ATTACK && !player.getWeapon().isActive())
                    {
                        player.attack();
                    }
                    break;

                case ESCAPE:
                    stage.close();
                    break;
            }
        });

        scene.setOnKeyReleased((KeyEvent event) ->
        {
            switch (event.getCode())
            {
                case LEFT:
                    isLeftKeyPressed = false;
                    if (!isDownKeyPressed && !isRightKeyPressed && !isUpKeyPressed && player.getState() != STATES.ATTACK)
                    {
                        player.setAnimation(ANIMATIONS.PLAYER_LEFT_IDLE);
                    }
                    break;

                case RIGHT:
                    isRightKeyPressed = false;
                    if (!isLeftKeyPressed && !isDownKeyPressed && !isUpKeyPressed && player.getState() != STATES.ATTACK)
                    {
                        player.setAnimation(ANIMATIONS.PLAYER_RIGHT_IDLE);
                    }
                    break;

                case UP:
                    isUpKeyPressed = false;
                    if (!isDownKeyPressed && !isLeftKeyPressed && !isRightKeyPressed && player.getState() != STATES.ATTACK)
                    {
                        player.setAnimation(ANIMATIONS.PLAYER_UP_IDLE);
                    }
                    break;

                case DOWN:
                    isDownKeyPressed = false;
                    if (!isLeftKeyPressed && !isRightKeyPressed && !isUpKeyPressed && player.getState() != STATES.ATTACK)
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
