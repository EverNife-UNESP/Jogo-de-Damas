package br.com.finalcraft.unesp.java.client.javafx.controller;

import br.com.finalcraft.unesp.java.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.common.application.render.CheckersRender;
import br.com.finalcraft.unesp.java.common.application.render.data.PieceStackPane;
import br.com.finalcraft.unesp.java.main.JavaFXMain;
import br.com.finalcraft.unesp.java.main.javafx.view.MyFXMLs;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.Random;

public class CheckersController {

    public static CheckersController instance;
    public static Scene scene;

    public void updateCheckersTable(){

        CheckersTheGame.instance.allSquareFields.forEach(squareField -> {
            //Render do background
            Rectangle rectangle = CheckersRender.createRender(squareField);
            gripPane.getChildren().add(rectangle);
        });

        CheckersTheGame.instance.allSquareFields.forEach(squareField -> {
            //Render da pessa em si
            if (squareField.hasPiece()){
                PieceStackPane stackPane = CheckersRender.createRender(squareField.getPiece());

                gripPane.getChildren().add(stackPane);
            }

        });
    }

    double x;
    double y;
    @FXML
    void initialize() {
        instance = this;
        JavaFXMain.thePrimaryStage.setResizable(false);

        sideBar.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        sideBar.setOnMouseDragged(event -> {
            JavaFXMain.thePrimaryStage.setX(event.getScreenX() - x);
            JavaFXMain.thePrimaryStage.setY(event.getScreenY() - y);
        });

        headerBar.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        headerBar.setOnMouseDragged(event -> {
            JavaFXMain.thePrimaryStage.setX(event.getScreenX() - x);
            JavaFXMain.thePrimaryStage.setY(event.getScreenY() - y);
        });


        //Seria usado caso eu fosse fazer recimencionalização
        /*
        slidingScale.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number previous,
                    Number now) {
                System.out.println("Previous: " + previous + ", Now: " + now);
                if (!slidingScale.isValueChanging()
                        || now.doubleValue() == slidingScale.getMax()
                        || now.doubleValue() == slidingScale.getMin()) {
                    // This only fires when we're done
                    // or when the slider is dragged to its max/min.
                }
            }
        });
        */

        updateCheckersTable();
    }

    public static void show(){
        Stage newSage = new Stage();
        newSage.initStyle(StageStyle.TRANSPARENT);
        newSage.setOnCloseRequest(e -> System.exit(0));

        JavaFXMain.thePrimaryStage.close();
        JavaFXMain.thePrimaryStage = newSage;
        JavaFXMain.thePrimaryStage.show();

        JavaFXMain.thePrimaryStage.setScene(scene = new Scene(MyFXMLs.loadCheckers()));
    }

    private JFXSlider slidingScale;

    @FXML
    private HBox headerBar;

    @FXML
    private VBox sideBar;

    @FXML
    private BorderPane borderPane;

    @FXML
    void onClose() {
        JavaFXMain.shutDown();
    }

    @FXML
    private GridPane gripPane;


    @FXML
    void onDesistir() {




        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Desistir do Jogo???");
        alert.setHeaderText("Você tem certeza que deseja desistir?");
        alert.setContentText(frasesMotivacionais[(new Random().nextInt(frasesMotivacionais.length))]);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            onClose();
        }
    }

    private static final String[] frasesMotivacionais = new String[]{
            "Se cair levante se deslizar se segure mas nunca pence em desistir por que o quanto mas amarga for a sua queda mas doce sera a sua vitoria.",
            "Eu odiava cada minuto dos treinos, mas dizia para mim mesmo: Não desista! Sofra agora e viva o resto de sua vida como um campeão.",
            "Não desista. Geralmente é a última chave no chaveiro que abre a porta.",
            "Então não desista, sorria. Você é mais forte do que pensa e será mais feliz do que imagina.",
            "Tomara que a gente não desista de ser quem é por nada nem ninguém deste mundo.",
            "Não desista de seu sonho",
            "Não desista jamais e saiba valorizar quem te ama, esses sim merecem seu respeito.",
            "Tomara que a gente não desista de ser quem é por nada nem ninguém deste mundo. Que a gente reconheça o poder do outro sem esquecer do nosso.",
            "Não desista, vá em frente, sempre há uma chance de você tropeçar em algo maravilhoso.",
            "Não desista só porque as coisas estão difíceis.",
            "Não desista do amor, não desista de amar, não se entregue à dor, porque ela um dia vai passar...",
            "-Desista\n" +
                    "-Não... desista você... de me tentar fazer desistir!!!!",
            "Mude sua rota, mas não desista no caminho"
    };
}
