import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class ViewManager
{
    protected Pane pane;
    protected Scene scene;
    protected Stage stage;

    public ViewManager(int width, int height)
    {
        this.pane = new Pane();
        this.scene = new Scene(pane, width, height);
        this.stage = new Stage();
        this.stage.setScene(this.scene);
    }

    public Stage getStage()
    {
        return this.stage;
    }
}
