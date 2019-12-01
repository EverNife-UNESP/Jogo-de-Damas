package br.com.finalcraft.unesp.java.jogodamas.main.javafx.controller;

import br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller.CheckersController;
import br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller.ClientController;
import br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller.SimulatorController;
import br.com.finalcraft.unesp.java.jogodamas.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.PlayerType;
import br.com.finalcraft.unesp.java.jogodamas.server.javafx.controller.ServerController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.Random;

public class TrueMainController {

    public static TrueMainController instance;

    public static String playerName;
    public static PlayerType playerType;


    @FXML
    void initialize() {
        instance = this;
        playerName = "Visitante" + new Random().nextInt(25505);
        nameLabel.setText(playerName);
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField nameLabel;

    public void checkIfNameWasChanged(){
        String newName = nameLabel.getText();
        if (nameLabel.getText().equals(playerName) || nameLabel.getText().isEmpty()){
            return;
        }
        playerName = nameLabel.getText();
        System.out.println("PlayerName changed to: " + playerName);
    }

    @FXML
    void onHospedarPartida() {
        checkIfNameWasChanged();
        playerType = PlayerType.PLAYER_ONE;
        ServerController.show();
    }

    @FXML
    void onProcurarPartida() {
        checkIfNameWasChanged();
        playerType = PlayerType.PLAYER_TWO;
        ClientController.show();
    }

    @FXML
    void onJogarSolo() {
        checkIfNameWasChanged();
        playerType = PlayerType.PLAYER_ONE;
        CheckersTheGame.instance.isSinglePlayer = true;
        CheckersController.show();
    }

    @FXML
    void onSimularPartidas() {
        SimulatorController.show();
    }

}
