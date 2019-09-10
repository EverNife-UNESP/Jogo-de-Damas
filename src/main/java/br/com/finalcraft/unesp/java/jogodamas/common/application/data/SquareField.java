package br.com.finalcraft.unesp.java.jogodamas.common.application.data;

import br.com.finalcraft.unesp.java.jogodamas.common.application.CheckersTheGame;

import java.io.Serializable;

public class SquareField implements Serializable {

    private final boolean isValid;
    private final int xCoord;
    private final int yCoord;

    private Piece piece = null;

    public SquareField(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.isValid = (xCoord + yCoord) % 2 != 0;
    }

    public boolean isValid() {
        return isValid;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean hasPiece(){
        return piece != null;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null) piece.setSquareField(this);
    }

    public SquareField getAdjacent(int xDislock, int yDislock){
        return CheckersTheGame.instance.getSquareField(this.xCoord + xDislock, this.yCoord + yDislock);
    }

    @Override
    public String toString() {
        return "[SquareField xCoord: " + xCoord + ", yCoord: " + yCoord + ", isValid: " + isValid + ", hasPiece: " + hasPiece() + "]";
    }
}
