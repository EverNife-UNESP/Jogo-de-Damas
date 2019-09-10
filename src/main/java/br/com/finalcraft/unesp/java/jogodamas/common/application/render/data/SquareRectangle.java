package br.com.finalcraft.unesp.java.jogodamas.common.application.render.data;

import br.com.finalcraft.unesp.java.jogodamas.common.application.data.SquareField;
import javafx.scene.shape.Rectangle;


public class SquareRectangle extends Rectangle {

    private final SquareField squareField;

    public SquareRectangle(SquareField squareField) {
        super();
        this.squareField = squareField;
    }

    public SquareField getSquareField() {
        return squareField;
    }

    public void isSelected(double xCoord, double yCoord){

    }
}
