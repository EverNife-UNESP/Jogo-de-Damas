package br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller;

import br.com.finalcraft.unesp.java.jogodamas.common.Sleeper;
import br.com.finalcraft.unesp.java.jogodamas.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.jogodamas.common.application.render.CheckersRender;
import br.com.finalcraft.unesp.java.jogodamas.common.application.render.data.PieceStackPane;
import br.com.finalcraft.unesp.java.jogodamas.main.JavaFXMain;
import br.com.finalcraft.unesp.java.jogodamas.main.javafx.view.MyFXMLs;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.Random;

public class DebuggerController {

    public static DebuggerController instance;
    public static Scene scene;

}
