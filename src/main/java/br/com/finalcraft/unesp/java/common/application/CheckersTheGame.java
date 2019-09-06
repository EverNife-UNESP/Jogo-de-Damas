package br.com.finalcraft.unesp.java.common.application;

import br.com.finalcraft.unesp.java.common.application.data.*;
import br.com.finalcraft.unesp.java.common.application.data.enums.MoveAttempt;
import br.com.finalcraft.unesp.java.common.application.data.enums.MoveResult;
import br.com.finalcraft.unesp.java.common.application.data.enums.PlayerType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckersTheGame implements Serializable {

    public static CheckersTheGame instance = new CheckersTheGame().iniciarTabelueiro();

    public SquareField[][] tabuleiro = new SquareField[8][8];
    public PlayerType playersTurn = null;
    public List<SquareField> allSquareFields = new ArrayList<>();

    public CheckersTheGame iniciarTabelueiro(){
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                tabuleiro[i][j] = new SquareField(i,j);
                allSquareFields.add(tabuleiro[i][j]);
            }
        }

        mountTable(PlayerType.PLAYER_ONE);
        return this;
    }

    public void mountTable(PlayerType playerToPlayerFirst){
        playersTurn = playerToPlayerFirst;
        //Setando peças
        for (int i = 0; i < 3; i++) {                       //Linha 1 2 e 3
            for (int j = 0; j < tabuleiro[i].length; j++) {
                if (tabuleiro[i][j].isValid()){
                    tabuleiro[i][j].setPiece(new Piece(playerToPlayerFirst));
                }
            }
        }

        for (int i = 5; i < tabuleiro.length; i++) {        //Linha 6 7 8
            for (int j = 0; j < tabuleiro[i].length; j++) {
                if (tabuleiro[i][j].isValid()){
                    tabuleiro[i][j].setPiece(new Piece(playerToPlayerFirst.getOpponent()));
                }
            }
        }

    }

    public SquareField getSquareField(int xCoord, int yCoord){
        try {
            return tabuleiro[xCoord][yCoord];
        }catch (Exception ignored){
            return null;
        }
    }

    public MoveResult tryToMove(Piece piece, SquareField target){
        if (!target.isValid() ||  target.hasPiece()) {
            return MoveResult.FAIL;
        }

        MoveAttempt moveAttempt = piece.canMoveTo(target);
        if (moveAttempt.getDirection() == MoveAttempt.Direction.UNAVAILABLE){        //Trying impossible Movement
            return MoveResult.FAIL;
        }

        piece.moveTo(target);

        return MoveResult.SUCESS;
    }



}
