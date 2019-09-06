package br.com.finalcraft.unesp.java.main.javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class MyFXMLs {

    public static Parent loadMain(){
        return loadFXML("/assets/truemain.fxml");
    }

    public static Parent loadServer(){
        return loadFXML("/assets/server.fxml");
    }

    public static Parent loadCheckers(){
        return loadFXML("/assets/checkers.fxml");
    }

    public static Parent loadClient(){
        return loadFXML("/assets/client.fxml");
    }

    public static URL getConsoleCSS(){
        return MyFXMLs.class.getResource("/assets/main/console-style.css");
    }

    private static Parent loadFXML(String fxmlFileAssetPath){
        try {
            return FXMLLoader.load(MyFXMLs.class.getResource(fxmlFileAssetPath));
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }

}
