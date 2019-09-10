package br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller;

import br.com.finalcraft.unesp.java.jogodamas.client.tcp.ClientSideTCP;
import br.com.finalcraft.unesp.java.jogodamas.common.Sleeper;
import br.com.finalcraft.unesp.java.jogodamas.common.consoleview.ConsoleView;
import br.com.finalcraft.unesp.java.jogodamas.main.JavaFXMain;
import br.com.finalcraft.unesp.java.jogodamas.main.javafx.view.MyFXMLs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class ClientController {

    public static ClientController instance;
    public static Scene scene;

    public static void show(){
        JavaFXMain.thePrimaryStage.setScene(scene = new Scene(MyFXMLs.loadClient()));
    }

    @FXML
    void initialize() {
        instance = this;
        ConsoleView.initialize();
    }

    @FXML
    private TextField textFieldIP;

    @FXML
    private TextField textFieldPorta;

    @FXML
    private Label labelResult;

    @FXML
    void onConexaoDireta(ActionEvent event) {
        String ip = textFieldIP.getText();
        String portaString = textFieldPorta.getText();

        if (ip.isEmpty()){
            labelResult.setText("O campo IP não pode estar vazio!");
            resetResultLabel(1500);
            return;
        }

        if (portaString.isEmpty()){
            labelResult.setText("O campo PORTA não pode estar vazio!");
            resetResultLabel(1500);
            return;
        }

        Integer porta;
        try {
            porta = Integer.parseInt(portaString);
            if (porta < 0) throw new NumberFormatException();
        }catch (NumberFormatException e){
            labelResult.setText("O campo PORTA precisa ser um inteiro positovo!");
            resetResultLabel(1500);
            return;
        }

        labelResult.setText("Iniciando conexão...");
        final Button button = (Button) event.getSource();
        button.setDisable(true);
        ClientSideTCP.initialize(ip,porta);
        if (ClientSideTCP.isConnected()){
            labelResult.setText("Conexão Aceita!");
            new Sleeper(){
                @Override
                public void run() {
                    CheckersController.show();
                }
            }.scheduleDelay(200);
        }else {
            labelResult.setText("Conexão recusada...");
            resetResultLabel(1500);
            button.setDisable(false);

        }
    }

    private void resetResultLabel(int delay){
        new Sleeper(){
            @Override
            public void run() {
                labelResult.setText("...");
            }
        }.scheduleDelay(delay);
    }

}
