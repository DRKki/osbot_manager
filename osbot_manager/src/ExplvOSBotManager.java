import gui.ManagerPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class ExplvOSBotManager extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) {
        primaryStage.setTitle("Explv's OSBot Manager");
        primaryStage.setScene(new Scene(new ManagerPane(), 600, 400));
        primaryStage.show();
    }
}