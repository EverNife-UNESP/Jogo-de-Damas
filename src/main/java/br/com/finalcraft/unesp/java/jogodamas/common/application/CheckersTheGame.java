package br.com.finalcraft.unesp.java.jogodamas.common.application;

import br.com.finalcraft.unesp.java.jogodamas.client.javafx.controller.CheckersController;
import br.com.finalcraft.unesp.java.jogodamas.client.tcp.ClientSideTCP;
import br.com.finalcraft.unesp.java.jogodamas.common.SmartLogger;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.Piece;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.MoveAttempt;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.MoveResult;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.PlayerType;
import br.com.finalcraft.unesp.java.jogodamas.common.tcpmessage.TCPMessage;
import br.com.finalcraft.unesp.java.jogodamas.common.tcpmessage.TCPMessageDirection;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.SquareField;
import br.com.finalcraft.unesp.java.jogodamas.main.javafx.controller.TrueMainController;
import br.com.finalcraft.unesp.java.jogodamas.server.tcphandler.ServerSideTCP;
import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckersTheGame implements Serializable {

    public static CheckersTheGame instance = new CheckersTheGame().iniciarTabelueiro();

    public static boolean isSinglePlayer = false;

    public static void updateNewInstance(CheckersTheGame checkersTheGame){
        instance = checkersTheGame;
        instance.refreshGameRenderAndLogic();
    }

    public SquareField[][] tabuleiro = new SquareField[8][8];
    public PlayerType playersTurn = null;
    public List<SquareField> allSquareFields = new ArrayList<>();
    public transient List<MoveAttempt> obligatedMoves = new ArrayList<>();
    public transient MoveAttempt lastMoveAttempt = null;
    private transient boolean consecutiveKilling = false;

    public boolean isASecondConsecutiveMove(){
        return consecutiveKilling;
    }

    public boolean isMyTurn(Piece piece){
        if (isSinglePlayer){
            return piece.getOwner() == playersTurn;
        }else {
            return instance.playersTurn == TrueMainController.playerType && piece.getOwner() == playersTurn;
        }
    }

    public void endMyTurn(){

        boolean playAgain = false;
        if (lastMoveAttempt.getDirection() == MoveAttempt.Direction.KILL){
            consecutiveKilling = true;
            List<MoveAttempt> moveAttemptList = checkForAssassination(lastMoveAttempt.getActorPiece());
            playAgain = moveAttemptList.size() > 0;
        }
        if (playAgain == false){
            lastMoveAttempt.getActorPiece().checkForTurnIntoDama();
        }
        consecutiveKilling = playAgain;


        if (playersTurn == PlayerType.PLAYER_ONE){
            if (!playAgain) instance.playersTurn = PlayerType.PLAYER_TWO;
            if (!isSinglePlayer) ServerSideTCP.getClient().sendToClient(new TCPMessage.CheckersTable(instance, TCPMessageDirection.CLIENT_TO_SERVER));
        }else {
            if (!playAgain) instance.playersTurn = PlayerType.PLAYER_ONE;
            if (!isSinglePlayer) ClientSideTCP.sendToServer(new TCPMessage.CheckersTable(instance, TCPMessageDirection.CLIENT_TO_SERVER));
        }
        refreshGameRenderAndLogic();
    }

    public void refreshGameRenderAndLogic(){
        //Game Logic
        calculateObligatedMoves();

        //Game Render
        Platform.runLater(() -> {
            CheckersController.instance.updateCheckersTable();
        });
    }

    public List<MoveAttempt> getObligatedMoves() {
        return obligatedMoves;
    }

    private void calculateObligatedMoves(){
        List<MoveAttempt> newObligatedMoves = new ArrayList<>();
        for (SquareField squareField : allSquareFields) {
            if (squareField.hasPiece() && isMyTurn(squareField.getPiece())){
                newObligatedMoves.addAll(checkForAssassination(squareField.getPiece()));
            }
        }
        if (SmartLogger.DEBUG_LOGICAL) newObligatedMoves.forEach( moveAttempt -> SmartLogger.debugLogical(moveAttempt.toString()));
        obligatedMoves = newObligatedMoves;
    }

    // --------------------------------------------------------------

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
                    tabuleiro[i][j].setPiece(new Piece(playerToPlayerFirst.getOpponent()));
                }
            }
        }

        for (int i = 5; i < tabuleiro.length; i++) {        //Linha 6 7 8
            for (int j = 0; j < tabuleiro[i].length; j++) {
                if (tabuleiro[i][j].isValid()){
                    tabuleiro[i][j].setPiece(new Piece(playerToPlayerFirst));
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

    //Most inefficient way to do this, but.... i don't care :/
    public List<MoveAttempt> checkForAssassination(Piece piece){
        List<MoveAttempt> moveAttemptList = new ArrayList<MoveAttempt>();
        for (SquareField squareField : allSquareFields) {
            if (squareField.isValid()){
                MoveAttempt moveAttempt = piece.canMoveTo(squareField);
                if (moveAttempt.getDirection() == MoveAttempt.Direction.KILL && moveAttempt.getKilledPiece() != null){
                    moveAttemptList.add(moveAttempt);
                }
            }
        }
        return moveAttemptList;
    }

    public MoveResult tryToMove(Piece piece, SquareField target){
        MoveAttempt moveAttempt = piece.canMoveTo(target);

        if (obligatedMoves.size() > 0){
            boolean allowedMove = false;
            for (MoveAttempt obligatedMove : obligatedMoves) {
                if (obligatedMove.equals(moveAttempt)){
                    allowedMove = true;
                    break;
                }
            }
            if (!allowedMove){
                return MoveResult.FAIL;
            }
        }

        switch (moveAttempt.getDirection()){
            case KILL:                      //Killing Enemy
                moveAttempt.getKilledPiece().getSquareField().setPiece(null);
            case SIMPLE:
                piece.moveTo(target);
                lastMoveAttempt = moveAttempt;
                return MoveResult.SUCESS;

                //Impossible Moves Bellow
            case OVER_PIECE:               //Trying impossible Movement
            case UNAVAILABLE:               //Trying impossible Movement
            case FRIEND_FIRE:               //Trying impossible Movement
            default:
                return MoveResult.FAIL;
        }
    }



}
