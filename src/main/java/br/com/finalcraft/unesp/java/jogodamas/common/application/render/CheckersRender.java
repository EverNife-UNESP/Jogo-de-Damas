package br.com.finalcraft.unesp.java.jogodamas.common.application.render;

import br.com.finalcraft.unesp.java.jogodamas.common.application.data.Piece;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.SquareField;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.PlayerType;
import br.com.finalcraft.unesp.java.jogodamas.common.application.render.data.PieceStackPane;
import br.com.finalcraft.unesp.java.jogodamas.common.application.render.data.SquareRectangle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class CheckersRender {

    public static int TAMANHO_QUADRADO = 100;

    private static final Color COLOR_SQUARE_WHITE = Color.valueOf("#feb");
    private static final Color COLOR_SQUARE_RED = Color.valueOf("#db5c07");

    public static SquareRectangle createRender(SquareField squareField){
        SquareRectangle rectangle = new SquareRectangle(squareField);

        GridPane.setRowIndex(rectangle, squareField.getXCoord());
        GridPane.setColumnIndex(rectangle, squareField.getYCoord());

        rectangle.setWidth(TAMANHO_QUADRADO);
        rectangle.setHeight(TAMANHO_QUADRADO);

        rectangle.setFill(!squareField.isValid() ? COLOR_SQUARE_WHITE : COLOR_SQUARE_RED);  //Contrario aqui, por algum motivo sobrenatural....
        return rectangle;
    }

    private static final Color COLOR_PIECE_WHITE = Color.valueOf("#f0d78d");
    private static final Color COLOR_PIECE_RED = Color.valueOf("#a84503");
    public static PieceStackPane createRender(Piece piece){

        PieceStackPane stackPane = new PieceStackPane(piece);
        GridPane.setRowIndex(stackPane, piece.getSquareField().getXCoord());
        GridPane.setColumnIndex(stackPane, piece.getSquareField().getYCoord());

        int TAMANHO_QUADRADO = CheckersRender.TAMANHO_QUADRADO;

        Ellipse backGround = new Ellipse(TAMANHO_QUADRADO * 0.3125, TAMANHO_QUADRADO * 0.26);
        backGround.setFill(Color.BLACK);

        backGround.setStroke(Color.BLACK);
        backGround.setStrokeWidth(TAMANHO_QUADRADO * 0.03);

        backGround.setTranslateY(5); // Mover 5 pixels para baixo!

        Ellipse ellipse = new Ellipse(TAMANHO_QUADRADO * 0.3125, TAMANHO_QUADRADO * 0.26);
        ellipse.setFill(piece.getOwner() == PlayerType.PLAYER_ONE ? COLOR_PIECE_WHITE : COLOR_PIECE_RED);

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TAMANHO_QUADRADO * 0.03);

        stackPane.getChildren().addAll(backGround, ellipse);

        if (piece.isDama()){
            int TAMANHO_QUADRADO_SUPERIOR = (int)(TAMANHO_QUADRADO * 0.7);
            Ellipse innerBackGround = new Ellipse(TAMANHO_QUADRADO_SUPERIOR * 0.3125, TAMANHO_QUADRADO_SUPERIOR * 0.26);
            innerBackGround.setFill(Color.BLACK);

            innerBackGround.setStroke(Color.BLACK);
            innerBackGround.setStrokeWidth(TAMANHO_QUADRADO_SUPERIOR * 0.03);

            innerBackGround.setTranslateY(5); // Mover 5 pixels para baixo!

            Ellipse innerElipse = new Ellipse(TAMANHO_QUADRADO_SUPERIOR * 0.3125, TAMANHO_QUADRADO_SUPERIOR * 0.26);
            innerElipse.setFill(piece.getOwner() == PlayerType.PLAYER_ONE ? COLOR_PIECE_WHITE : COLOR_PIECE_RED);

            innerElipse.setStroke(Color.BLACK);
            innerElipse.setStrokeWidth(TAMANHO_QUADRADO_SUPERIOR * 0.03);
            stackPane.getChildren().addAll(innerBackGround,innerElipse);
        }

        //Só criar as configuração de Drag se for a minha vez!
        stackPane.setDragProperties();

        return stackPane;
    }

}
