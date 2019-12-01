package br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller;

import br.com.finalcraft.unesp.java.jogodamas.client.tcp.ClientSideTCP;
import br.com.finalcraft.unesp.java.jogodamas.common.Sleeper;
import br.com.finalcraft.unesp.java.jogodamas.common.consoleview.ConsoleView;
import br.com.finalcraft.unesp.java.jogodamas.main.JavaFXMain;
import br.com.finalcraft.unesp.java.jogodamas.main.javafx.view.MyFXMLs;
import br.com.finalcraft.unesp.java.jogodamas.main.sql.BancoDeDados;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SimulatorController {

    public static SimulatorController instance;
    public static Scene scene;

    public static void show(){
        JavaFXMain.thePrimaryStage.setScene(scene = new Scene(MyFXMLs.loadSimulator()));
    }

    @FXML
    void initialize() {
        instance = this;
        ConsoleView.initialize();
    }

    @FXML
    private TextField player1Name;

    @FXML
    private Label labelResult;

    @FXML
    private TextField player2Name;

    @FXML
    private TextField player1Damas;

    @FXML
    private TextField player2Damas;

    @FXML
    private CheckBox vencedor0;

    @FXML
    private CheckBox vencedor1;

    @FXML
    private CheckBox vencedor2;


    private boolean checkEmpty(TextField textField){
        if (textField.getText().isEmpty()){
            setResultLabelTimed(textField.getId() + " não pode estar vazio!");
            return true;
        }
        return false;
    }

    private Integer getNumber(TextField textField){
        try {
            return Integer.parseInt(textField.getText());
        }catch (Exception e){
            setResultLabelTimed(textField.getId() + " precisa ser inteiro positivo!");
        }
        return null;
    }

    @FXML
    void onCheckBox(ActionEvent event) {
        if (event.getSource() == vencedor1){
            vencedor2.setSelected(false);
            vencedor0.setSelected(false);
        }else if (event.getSource() == vencedor2){
            vencedor0.setSelected(false);
            vencedor1.setSelected(false);
        }else {
            vencedor1.setSelected(false);
            vencedor2.setSelected(false);
        }
    }

    private int getWinner(){
        if (vencedor1.isSelected()) return 1;
        if (vencedor2.isSelected()) return 2;
        return 0;
    }


    @FXML
    void onSimulate() {

        if (checkEmpty(player1Name) || checkEmpty(player2Name)){
            return;
        }

        String playerOneName = player1Name.getText();
        String playerTwoName = player2Name.getText();

        if (playerOneName.equalsIgnoreCase(playerTwoName)){
            setResultLabelTimed("Os nomes devem ser diferentes!");
            return;
        }

        Integer playerOneDamas = getNumber(player1Damas);
        if (playerOneDamas == null) return;

        Integer playerTwoDamas = getNumber(player2Damas);
        if (playerTwoDamas == null) return;


        String winner = getWinner() == 1 ? playerOneName : getWinner() == 2 ? playerTwoName : "Empate";

        BancoDeDados.computarBatalha(playerOneName,playerTwoName,playerOneDamas,playerTwoDamas,winner);
    }

    private void setResultLabelTimed(String timedMSG){
        labelResult.setText(timedMSG);
        resetResultLabel(1500);
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
