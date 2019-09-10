package br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums;

import java.io.Serializable;

public enum PlayerType implements Serializable {
    PLAYER_ONE,
    PLAYER_TWO;

    public PlayerType getOpponent(){
        return getOpponent(this);
    }
    public static PlayerType getOpponent(PlayerType playerType){
        return playerType == PLAYER_ONE ? PLAYER_TWO : PLAYER_ONE;
    }
}
