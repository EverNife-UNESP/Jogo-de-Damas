package br.com.finalcraft.unesp.java.jogodamas.server.javafx.controller;

import br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller.CheckersController;
import br.com.finalcraft.unesp.java.jogodamas.main.JavaFXMain;
import br.com.finalcraft.unesp.java.jogodamas.common.consoleview.ConsoleView;
import br.com.finalcraft.unesp.java.jogodamas.main.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.java.jogodamas.server.tcphandler.ServerSideTCP;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class ServerController {

    public static ServerController instance;
    public static Scene scene;

    public static void show(){
        JavaFXMain.thePrimaryStage.setScene(scene = new Scene(MyFXMLs.loadServer()));
    }

    @FXML
    private Label waitingLabel;

    @FXML
    private ImageView gifImage;


    @FXML
    private TextField textFieldPorta;

    private int porta = 12000;
    private boolean pauseThread = false;

    @FXML
    void onAlterarPorta() {

        String portaString = textFieldPorta.getText();
        Integer thePorta = null;
        try {
            thePorta = Integer.parseInt(portaString);
        }catch (Exception ignored){
        }

        if (thePorta == null || porta == thePorta){
            return;
        }

        pauseThread = true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alterar porta");
        alert.setHeaderText("Você quer alterar a porta do servidor?");
        alert.setContentText("Você quer alterar a porta do servidor para a porta [" + portaString + "] ? ");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            porta = thePorta;
            ServerSideTCP.initialize(porta);
            pauseThread = false;
        }
    }


    @FXML
    void initialize() {
        instance = this;
        ConsoleView.initialize();
        ServerSideTCP.initialize(porta);

        new Thread(){
            String dots = ".";
            @Override
            public void run() {
                try {
                    while (true){
                        Thread.sleep(500);
                        if (pauseThread){
                            continue;
                        }
                        if (ServerSideTCP.isConnected()){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    CheckersController.show();
                                }
                            });
                            return;
                        }
                        if (dots.length() > 3){
                            dots = "";
                        }else {
                            dots = dots + ".";
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                waitingLabel.setText("Esperando um oponente" + dots);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
