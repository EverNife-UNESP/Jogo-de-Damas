package br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums;

import br.com.finalcraft.unesp.java.jogodamas.common.application.data.Piece;

import java.io.Serializable;

public class MoveAttempt implements Serializable {

    public Direction direction;
    public Piece killedPiece = null;

    public MoveAttempt(Direction direction) {
        this.direction = direction;
    }

    public Piece getKilledPiece() {
        return killedPiece;
    }

    public Direction getDirection() {
        return direction;
    }

    public MoveAttempt setKilledPiece(Piece intermedium) {
        this.killedPiece = intermedium;
        return this;
    }

    public static enum Direction implements Serializable{
        UNAVAILABLE,
        SIMPLE,
        KILL;
    }
}
