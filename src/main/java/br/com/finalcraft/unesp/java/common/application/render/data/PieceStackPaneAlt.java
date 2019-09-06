package br.com.finalcraft.unesp.java.common.application.render.data;

import br.com.finalcraft.unesp.java.common.SmartLogger;
import javafx.scene.layout.StackPane;

public class PieceStackPaneAlt extends StackPane {

    private double mouseX, mouseY;
    private double oldX, oldY;

    public PieceStackPaneAlt() {
        super();
    }

    public void setDragProperties(){
        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnDragEntered(e -> {
            oldX = this.getTranslateX();
            oldY = this.getTranslateY();
        });

        setOnMouseDragged(e -> {
            altRelocate(e.getSceneX() - mouseX, e.getSceneY() - mouseY);
            if (SmartLogger.DEBUG_MODE){
                SmartLogger.info("relocate(" + (e.getSceneX() - mouseX + oldX) + " : " + (e.getSceneY() - mouseY + oldY));
            }
        });

        setOnMouseDragReleased(event -> {
            altRelocate(oldX,oldY);
        });
    }

    public void altRelocate(double x, double y) {
        setTranslateX(x - getLayoutBounds().getMinX());
        setTranslateY(y - getLayoutBounds().getMinY());
    }

}
