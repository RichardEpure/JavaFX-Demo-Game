import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application
{
    // Reads in data from a text file which has a string representation of the tile layout of the level.
    // The strings are converted into 'ELEMENT' enumerations and are returned as a two-dimensional array.
    public static ELEMENT[][] parseLevel(String file_path)
    {
        try
        {
            FileReader fileReader = new FileReader(file_path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String[]> lines = new ArrayList<>();
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                line = line.replaceAll("\\s+","");
                lines.add(line.split(","));
            }
            bufferedReader.close();

            ELEMENT[][] levelData = new ELEMENT[lines.size()][lines.get(0).length];
            for(int i=0; i<levelData.length; i++)
            {
                for(int j=0; j<levelData[0].length; j++)
                {
                    levelData[i][j] = ELEMENT.getElementWithSymbol(lines.get(i)[j]);
                }
            }
            return levelData;
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unknown path:\n" + file_path);
        }
        catch(IOException e)
        {
            System.out.println("Could not read file:\n" + file_path);
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("OOP-MINI-PROJECT");
        LevelManager levelManager = new LevelManager(100, parseLevel("src/resources/level_info.txt"), new Image[]{new Image("resources/tileMapFront.png"), new Image("resources/tileMapBack.png")});
        GameManager gameManager = new GameManager(levelManager);
        primaryStage = gameManager.getStage();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
