package br.com.finalcraft.unesp.java.common.application.render.data;

import br.com.finalcraft.unesp.java.common.SmartLogger;
import br.com.finalcraft.unesp.java.common.application.data.Piece;
import javafx.scene.layout.StackPane;

public class PieceStackPane extends StackPane {

    private double mouseX, mouseY;
    private double oldX, oldY;
    private final Piece piece;

    public PieceStackPane(Piece piece) {
        super();
        this.piece = piece;
    }

    public void setDragProperties(){

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            if (oldX == -1 && oldY == -1){
                oldX = mouseX;
                oldY = mouseY;
            }
        });

        setOnMouseDragged(e -> {
            double newX = e.getSceneX() - mouseX + oldX;
            double newY = e.getSceneY() - mouseY + oldY;
            altRelocate(newX,newY);
            if (SmartLogger.DEBUG_MODE) {
                System.out.println(this.piece + "  relocate -> " + newX + "," + newY);
            }
        });

        setOnMouseReleased(e ->{
            altRelocate(oldX, oldY);
            if (SmartLogger.DEBUG_MODE) {
                System.out.println(this.piece + "  returningBackToOrigin -> " + oldX + "," + oldY);
            }
        });

        setOnMouseDragExited(e -> {
            System.out.println(this.piece + "OnDragReleased");
        });
    }

    public void altRelocate(double x, double y) {
        setTranslateX(x - getLayoutBounds().getMinX());
        setTranslateY(y - getLayoutBounds().getMinY());
    }

}
