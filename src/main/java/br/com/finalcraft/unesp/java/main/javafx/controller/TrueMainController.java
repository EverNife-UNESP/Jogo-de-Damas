package br.com.finalcraft.unesp.java.main.javafx.controller;

import br.com.finalcraft.unesp.java.client.javafx.controller.CheckersController;
import br.com.finalcraft.unesp.java.client.javafx.controller.ClientController;
import br.com.finalcraft.unesp.java.common.application.data.enums.PlayerType;
import br.com.finalcraft.unesp.java.main.JavaFXMain;
import br.com.finalcraft.unesp.java.server.javafx.controller.ServerController;
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

        CheckersController.show();
        //ClientController.show();
    }
}
