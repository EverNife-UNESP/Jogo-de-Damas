package br.com.finalcraft.unesp.java.jogodamas.common.application.render.data;

import br.com.finalcraft.unesp.java.jogodamas.common.SmartLogger;
import br.com.finalcraft.unesp.java.jogodamas.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.Piece;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.SquareField;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.MoveResult;
import br.com.finalcraft.unesp.java.jogodamas.common.application.render.CheckersRender;
import javafx.scene.layout.StackPane;

public class PieceStackPane extends StackPane {

    private double mouseX, mouseY;
    private double screenX, screenY;
    private final Piece piece;

    private double newX;
    private double newY;

    public PieceStackPane(Piece piece) {
        super();
        this.piece = piece;
    }


    public void setDragProperties(){

        if (!CheckersTheGame.instance.isMyTurn(this.getPiece())) return;

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            if (screenX == -1 && screenY == -1){
                screenX = e.getX();
                screenY = e.getY();
            }
        });

        setOnMouseDragged(e -> {
            newX = e.getSceneX() - mouseX;
            newY = e.getSceneY() - mouseY;
            altRelocate(newX,newY);
            SmartLogger.debugScreen(this.piece + "  relocate -> " + newX + "," + newY);
        });

        setOnMouseReleased(e ->{
            newX = e.getSceneX() - mouseX;
            newY = e.getSceneY() - mouseY;

            SquareField targetField;
            SmartLogger.debugScreen(this.piece + "  returningBackToOrigin -> " + screenX + "," + screenY);
            SmartLogger.debugScreen("Calculation Target: " + (targetField = calculateTargetSquareField((int) newX, (int) newY)));

            if (targetField != null){
                MoveResult moveResult = CheckersTheGame.instance.tryToMove(this.getPiece(), targetField);
                if (moveResult == MoveResult.SUCESS){
                    CheckersTheGame.instance.endMyTurn();
                    return;
                }
            }
            altRelocate(0,0);

        });
    }

    public SquareField calculateTargetSquareField(int xDislocation, int yDislocation){

        int TAMANHO_QUADRADO = CheckersRender.TAMANHO_QUADRADO;

        int signX = xDislocation < 0 ? -1 : 1;
        int signY = yDislocation < 0 ? -1 : 1;

        xDislocation = Math.abs(xDislocation);
        yDislocation = Math.abs(yDislocation);

        int dislockX = (xDislocation / TAMANHO_QUADRADO) + (xDislocation % TAMANHO_QUADRADO > (TAMANHO_QUADRADO/2) ? 1 : 0);
        int dislockY = (yDislocation / TAMANHO_QUADRADO) + (yDislocation % TAMANHO_QUADRADO > (TAMANHO_QUADRADO/2) ? 1 : 0);// + (yDislocation % 100 < -50 ? 1 : 0);;

        dislockX = dislockX * signX;
        dislockY = dislockY * signY;

        SmartLogger.debugScreen("Dislocating on Screen --> " + dislockX + "," + dislockY);

        return this.getPiece().getSquareField().getAdjacent(dislockY,dislockX); //Precisa ser o inverso, pois ná matrix é Y por X e nao X por Y
    }

    //Alternaive Relocation function
    public void altRelocate(double x, double y) {
        setTranslateX(x - getLayoutBounds().getMinX());
        setTranslateY(y - getLayoutBounds().getMinY());
    }

    public Piece getPiece() {
        return piece;
    }
}
