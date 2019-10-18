package br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums;

import br.com.finalcraft.unesp.java.jogodamas.common.application.data.Piece;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.SquareField;

import java.io.Serializable;

public class MoveAttempt implements Serializable {

    public Piece actorPiece;
    public SquareField targetField;

    public Direction direction;
    public Piece killedPiece = null;

    public MoveAttempt(Piece actorPiece, SquareField targetField) {
        this.actorPiece = actorPiece;
        this.targetField = targetField;
    }

    public Piece getKilledPiece() {
        return killedPiece;
    }

    public MoveAttempt setDirection(Direction direction){
        this.direction = direction;
        return this;
    }

    public Direction getDirection() {
        return direction;
    }

    public Piece getActorPiece() {
        return actorPiece;
    }

    public SquareField getTargetField() {
        return targetField;
    }

    public MoveAttempt setKilledPiece(Piece intermedium) {
        this.killedPiece = intermedium;
        return this;
    }

    public static enum Direction implements Serializable{
        UNAVAILABLE,
        OVER_PIECE,
        SIMPLE,
        KILL,
        FRIEND_FIRE;
    }

    @Override
    public String toString() {
        return "MoveAttempt[actorPiece: " + actorPiece + ", targetField:" + targetField + "]";
    }
}
