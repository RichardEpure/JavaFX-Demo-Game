import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class LevelManager
{
    private SubScene scene;
    private Pane pane;
    private int rows;
    private int columns;
    private int tileSize;
    private ArrayList<Rectangle> collidableElements;
    private ArrayList<LifeForm> lifeForms;
    private ELEMENT[][] levelData;

    public LevelManager(int tileSize, ELEMENT[][] levelData)
    {
        this.levelData = levelData;
        this.rows = levelData.length;
        this.columns = levelData[0].length;
        this.tileSize = tileSize;
        this.collidableElements = new ArrayList<>();
        this.lifeForms = new ArrayList<>();
        this.pane = new Pane();
        this.scene = new SubScene(pane, rows*tileSize, columns*tileSize);

        constructLevel();
    }

    // Returns the pane.
    public Pane getPane()
    {
        return this.pane;
    }

    // Returns the sub-scene.
    public SubScene getScene()
    {
        return this.scene;
    }

    // Returns the value of tileSize.
    public int getTileSize()
    {
        return tileSize;
    }

    // Adds the sub-scene to a pane.
    public void addSubSceneToPane(Pane pane)
    {
        pane.getChildren().add(scene);
    }

    // Adds an instance of a "LifeForm" to the array list.
    public void addLifeForm(LifeForm lifeForm)
    {
        lifeForms.add(lifeForm);
    }

    // Returns all instances of "LifeForm"s within the level.
    public ArrayList<LifeForm> getLifeForms()
    {
        return lifeForms;
    }

    // Returns a list of all elements with collision.
    public ArrayList<Rectangle> getCollidableElements()
    {
        return collidableElements;
    }

    // Creates all the tiles in order to construct the level.
    private void constructLevel()
    {
        for(int i=0; i<levelData.length; i++)
        {
            for(int j=0; j<levelData[0].length; j++)
            {
                if(levelData[i][j] != null)
                {
                    addElement(levelData[i][j], i, j);
                }
            }
        }
    }

    // Adds a tile to the level using grid-based logic.
    private void addElement(ELEMENT element, int row, int col)
    {
        addElement(element, row, col, 1);
    }

    private void addElement(ELEMENT element, int row, int col, int span)
    {
        ImageView image = new ImageView(new Image(element.getImage()));
        image.setLayoutY((scene.getHeight()/rows)*row);
        image.setLayoutX((scene.getWidth()/columns)*col);
        image.setFitHeight(tileSize*span);
        image.setFitWidth(tileSize*span);
        image.setViewOrder(element.getViewOrder());
        if(element.isCollidable())
        {
            Rectangle collisionBox = new Rectangle(0,0, Color.RED);
            int sizeFactor = 0;
            switch(element)
            {
                case SOUTHWALL:
                    sizeFactor = 5;
                default:
                    collisionBox.setLayoutY(((scene.getHeight()/rows)*row)+sizeFactor);
                    collisionBox.setLayoutX((scene.getWidth()/columns)*col);
                    collisionBox.setHeight((tileSize*span)-sizeFactor);
                    collisionBox.setWidth(tileSize*span);
                    break;
            }
            collisionBox.setVisible(false);
            collidableElements.add(collisionBox);
            pane.getChildren().add(collisionBox);
        }
        pane.getChildren().addAll(image);
    }
}
