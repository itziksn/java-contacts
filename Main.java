package contact_app;

import contact_app.data.ContactsData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Contacts");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void stop() {
        ContactsData.getInstance().store();
    }

    public static void main(String[] args) {
        ContactsData.getInstance().load();
        launch(args);
    }
}
