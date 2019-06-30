package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // STATIC STAGE FOR LOGIN STAGE
    public static Stage primaryStage;

    // EMPTY STRING FOR ACTIVE USER
    public static String activeUser = "";

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        try{
            // OPEN LOGINPAGE.FXML SO THE USER CAN EITHER LOG IN OR LOG OUT FROM THE PROGRAMME
            Parent root = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
            primaryStage.setTitle("QUICKSHARE");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}
