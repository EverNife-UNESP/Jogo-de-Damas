package br.com.finalcraft.unesp.java.main;

import br.com.finalcraft.unesp.java.common.application.data.enums.PlayerType;
import br.com.finalcraft.unesp.java.main.javafx.view.MyFXMLs;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXMain extends Application {

    public static Stage thePrimaryStage;

    public static void shutDown(){
        thePrimaryStage.close();
        System.exit(0);
    }

    @Override
    public void start(Stage thePrimaryStage) throws Exception{
        JavaFXMain.thePrimaryStage = thePrimaryStage;
        JavaFXMain.thePrimaryStage.setOnCloseRequest(e -> System.exit(0));

        Scene scene = new Scene(MyFXMLs.loadMain());
        JavaFXMain.thePrimaryStage.setTitle("Checkers TheGame - Java");
        JavaFXMain.thePrimaryStage.setScene(scene);
        JavaFXMain.thePrimaryStage.show();

        //ServerController.setUp();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}