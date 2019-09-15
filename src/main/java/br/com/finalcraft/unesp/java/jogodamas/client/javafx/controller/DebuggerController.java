package br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller;

import br.com.finalcraft.unesp.java.jogodamas.common.SmartLogger;
import br.com.finalcraft.unesp.java.jogodamas.main.JavaFXMain;
import br.com.finalcraft.unesp.java.jogodamas.main.javafx.view.MyFXMLs;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DebuggerController {

    public static DebuggerController instance;

    private static Stage stage;

    public static void show(){
        if (stage == null) {
            stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.WINDOW_MODAL);
            // Defines a modal window that blocks events from being
            // delivered to any other application window.
            stage.initOwner(JavaFXMain.thePrimaryStage);

            Scene newSceneWindow = new Scene(MyFXMLs.loadDebug());
            stage.setScene(newSceneWindow);
            stage.setOnShowing(event -> {
                if (SmartLogger.DEBUG_LOGICAL) instance.debugLogical.setSelected(true);
                if (SmartLogger.DEBUG_RENDER) instance.debugRender.setSelected(true);
            });
            stage.setOnCloseRequest(event -> {
                if (instance.debugLogical.isSelected()) SmartLogger.DEBUG_LOGICAL = true;
                if (instance.debugRender.isSelected()) SmartLogger.DEBUG_RENDER = true;
            });

            instance.borderPane.setOnMousePressed(event -> {
                instance.x = event.getSceneX();
                instance.y = event.getSceneY();
            });

            instance.borderPane.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - instance.x);
                stage.setY(event.getScreenY() - instance.y);
            });
        }

        stage.show();
    }

    private double x;
    private double y;
    @FXML
    void initialize() {
        instance = this;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXCheckBox debugLogical;

    @FXML
    private JFXCheckBox debugRender;

    @FXML
    void onClose() {
        stage.hide();
    }
}
