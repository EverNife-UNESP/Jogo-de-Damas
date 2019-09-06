package br.com.finalcraft.unesp.java.common.application.data.enums;

import br.com.finalcraft.unesp.java.common.application.data.Piece;

import java.io.Serializable;

public class MoveAttempt implements Serializable {

    public Direction direction;
    public Piece intermedium = null;

    public MoveAttempt(Direction direction) {
        this.direction = direction;
    }

    public Piece getIntermedium() {
        return intermedium;
    }

    public Direction getDirection() {
        return direction;
    }

    public MoveAttempt setIntermedium(Piece intermedium) {
        this.intermedium = intermedium;
        return this;
    }

    public static enum Direction implements Serializable{
        UNAVAILABLE,
        SIMPLE,
        KILL;
    }
}
