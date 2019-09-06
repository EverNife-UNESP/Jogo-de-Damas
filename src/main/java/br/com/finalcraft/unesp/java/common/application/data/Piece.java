package br.com.finalcraft.unesp.java.common.application.data;

import br.com.finalcraft.unesp.java.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.common.application.data.enums.MoveAttempt;
import br.com.finalcraft.unesp.java.common.application.data.enums.PlayerType;

import java.io.Serializable;

public class Piece implements Serializable {

    private final PlayerType owner;
    private SquareField squareField = null;

    public Piece(PlayerType owner) {
        this.owner = owner;
    }

    public void setSquareField(SquareField squareField){
        this.squareField = squareField;
    }

    public SquareField getSquareField() {
        return squareField;
    }

    public PlayerType getOwner() {
        return owner;
    }

    public MoveAttempt canMoveTo(SquareField target){
        int xCoord = this.squareField.getXCoord();
        int yCoord = this.squareField.getYCoord();
        int targetXCoord = target.getXCoord();
        int targetYCoord = target.getYCoord();

        if (targetXCoord == xCoord + 1 && targetYCoord == yCoord + 1) return new MoveAttempt(MoveAttempt.Direction.SIMPLE);
        if (targetXCoord == xCoord - 1 && targetYCoord == yCoord - 1) return new MoveAttempt(MoveAttempt.Direction.SIMPLE);
        if (targetXCoord == xCoord + 1 && targetYCoord == yCoord - 1) return new MoveAttempt(MoveAttempt.Direction.SIMPLE);
        if (targetXCoord == xCoord - 1 && targetYCoord == yCoord + 1) return new MoveAttempt(MoveAttempt.Direction.SIMPLE);

        int intermediumXCoord = xCoord;
        int intermediumYCoord = yCoord;

        if (targetXCoord == xCoord + 2 && targetYCoord == yCoord + 2) {intermediumXCoord += 2; intermediumYCoord += 2;}
        if (targetXCoord == xCoord - 2 && targetYCoord == yCoord - 2) {intermediumXCoord -= 2; intermediumYCoord -= 2;}
        if (targetXCoord == xCoord + 2 && targetYCoord == yCoord - 2) {intermediumXCoord += 2; intermediumYCoord -= 2;}
        if (targetXCoord == xCoord - 2 && targetYCoord == yCoord + 1) {intermediumXCoord -= 2; intermediumYCoord += 2;}

        SquareField squareField = CheckersTheGame.instance.getSquareField(intermediumXCoord,intermediumYCoord);
        if (squareField != null && squareField.getPiece().getOwner() != this.getOwner()){
            return new MoveAttempt(MoveAttempt.Direction.KILL).setIntermedium(squareField.getPiece());
        }
        return new MoveAttempt(MoveAttempt.Direction.UNAVAILABLE);

    }

    public void moveTo(SquareField squareField){
        this.getSquareField().setPiece(null);
        squareField.setPiece(this);
    }

    @Override
    public String toString() {
        return "[Piece Owner: " + owner + "" + this.getSquareField() + "]";
    }

}
