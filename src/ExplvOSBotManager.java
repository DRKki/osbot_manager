import gui.ManagerPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public final class ExplvOSBotManager extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public final void start(final Stage primaryStage) {

        if (!Version.isLatestVersion()) showUpdateDialog();

        primaryStage.setTitle("Explv's OSBot Manager");
        Scene scene = new Scene(new ManagerPane(), 600, 400);
        scene.getStylesheets().add("style/manager_pane.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showUpdateDialog() {
        Dialog dialog = new Dialog();
        dialog.setTitle("Explv's OSBot Manager");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        Label label = new Label("A new version is available!");
        label.setFont(new Font("Arial", 20));
        grid.add(label, 0, 0);

        Hyperlink threadLink = new Hyperlink("Click here to update");
        threadLink.setFont(new Font("Arial", 15));
        threadLink.setPadding(new Insets(0, 0, 0, 0));
        threadLink.setOnAction(e -> getHostServices().showDocument("http://osbot.org/forum/topic/100554-explvs-osbot-manager/"));
        grid.add(threadLink, 0, 1);

        dialog.showAndWait();
    }
}