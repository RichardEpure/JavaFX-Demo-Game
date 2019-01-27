import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class ViewManager
{
    protected Pane pane;
    protected Scene scene;
    protected Stage stage;

    protected int WIDTH;
    protected int HEIGHT;

    public ViewManager(int width, int height)
    {
        this.WIDTH = width;
        this.HEIGHT = height;

        this.pane = new Pane();
        this.scene = new Scene(pane, WIDTH, HEIGHT);
        this.stage = new Stage();
        this.stage.setScene(this.scene);
    }

    public Stage getStage()
    {
        return this.stage;
    }
}
