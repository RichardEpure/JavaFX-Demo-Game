import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("OOP-MINI-PROJECT");
        Level l = new Level(800, 800, 30, 30);
        primaryStage = l.getStage();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
