package br.com.finalcraft.unesp.java.jogodamas.common.application.data;

import br.com.finalcraft.unesp.java.jogodamas.common.SmartLogger;
import br.com.finalcraft.unesp.java.jogodamas.common.application.CheckersTheGame;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.MoveAttempt;
import br.com.finalcraft.unesp.java.jogodamas.common.application.data.enums.PlayerType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Piece implements Serializable {

    private final PlayerType owner;
    private boolean isDama = false;
    private SquareField pieceSquareField = null;

    public Piece(PlayerType owner) {
        this.owner = owner;
    }

    public void setSquareField(SquareField pieceSquareField){
        this.pieceSquareField = pieceSquareField;
    }

    public SquareField getSquareField() {
        return pieceSquareField;
    }

    public PlayerType getOwner() {
        return owner;
    }

    public MoveAttempt canMoveTo(SquareField target){

        int xCoord = this.pieceSquareField.getXCoord();
        int yCoord = this.pieceSquareField.getYCoord();

        int targetXCoord = target.getXCoord();
        int targetYCoord = target.getYCoord();

        int xDislock = targetXCoord - xCoord;
        int yDislock = targetYCoord - yCoord;

        SmartLogger.debugLogical("\n\n");
        SmartLogger.debugLogical("Origin: " + getSquareField());
        SmartLogger.debugLogical("Target: " + target);
        SmartLogger.debugLogical("Resulting --> xDislock: " + xDislock);
        SmartLogger.debugLogical("Resulting --> yDislock: " + yDislock);

        boolean canMoveDownwards    = this.getOwner() == PlayerType.PLAYER_TWO;
        boolean canMoveUpwards      = this.getOwner() == PlayerType.PLAYER_ONE;
        boolean canBidirectMove     = isDama || CheckersTheGame.instance.isASecondConsecutiveMove();

        MoveAttempt moveAttempt = new MoveAttempt(this,target);

        int absDistance = Math.abs(xDislock);//Tanto faz xDislock ou yDislock... pq sao iguais

        if (absDistance == 0) {              //Nela mesma
            return moveAttempt.setDirection(MoveAttempt.Direction.OVER_PIECE);
        }

        if (target.hasPiece()) {                            //Em cima de outra peça
            return moveAttempt.setDirection(MoveAttempt.Direction.OVER_PIECE);
        }

        if (Math.abs(xDislock) != Math.abs(yDislock) || !target.isValid()){          //Fora das cores e/ou direções válidas!
            SmartLogger.debugLogical("Out of field (Diagonal)");
            return moveAttempt.setDirection(MoveAttempt.Direction.UNAVAILABLE);
        }

        if (this.getSquareField() == target){                           //Mover nele mesmo :/
            SmartLogger.debugLogical("Self move");
            return moveAttempt.setDirection(MoveAttempt.Direction.UNAVAILABLE);
        }

        if (!canBidirectMove && !canMoveDownwards && xDislock > 0){     //Não pode ir para baixo
            SmartLogger.debugLogical("Cant move downwards");
            return moveAttempt.setDirection(MoveAttempt.Direction.UNAVAILABLE);
        }

        if (!canBidirectMove && !canMoveUpwards && xDislock < 0){     //Não pode ir para cima
            SmartLogger.debugLogical("Cant move upwards");
            return moveAttempt.setDirection(MoveAttempt.Direction.UNAVAILABLE);
        }


        List<SquareField> squareFieldsBetweenFields = new ArrayList<>();

        int temporaryXCoord = xCoord;
        int temporaryYCoord = yCoord;

        for (int i = 1; i < absDistance; i++){
            temporaryXCoord += (xDislock > 0 ? 1 : -1);
            temporaryYCoord += (yDislock > 0 ? 1 : -1);
            SquareField squareFieldBetween = CheckersTheGame.instance.getSquareField(temporaryXCoord,temporaryYCoord);
            squareFieldsBetweenFields.add(squareFieldBetween);
        }

        int fieldsFound = squareFieldsBetweenFields.size();
        int piecesBetween = (int) squareFieldsBetweenFields.stream().filter(squareField -> squareField.hasPiece()).count();
        boolean hasAnyPieceBetween = piecesBetween > 0;


        if (!hasAnyPieceBetween){   //Caso não tenha ninguem para ser comido!
            if (absDistance == 1){
                SmartLogger.debugLogical("SimpleMoveFound");
                return moveAttempt.setDirection(MoveAttempt.Direction.SIMPLE);
            }else if (isDama){
                SmartLogger.debugLogical("DamaMoveFound");
                return moveAttempt.setDirection(MoveAttempt.Direction.SIMPLE);
            }
            return moveAttempt.setDirection(MoveAttempt.Direction.UNAVAILABLE);
        }

        if (piecesBetween == 1){
            Piece killedPiece = squareFieldsBetweenFields.stream().filter(squareField -> squareField.hasPiece()).findFirst().get().getPiece();
            if (killedPiece.getOwner() == this.getOwner()){
                return moveAttempt.setDirection(MoveAttempt.Direction.FRIEND_FIRE).setKilledPiece(killedPiece);
            }
            if (absDistance == 2){
                SmartLogger.debugLogical("SimpleEatFound");
                return moveAttempt.setDirection(MoveAttempt.Direction.KILL).setKilledPiece(killedPiece);
            }else if (absDistance > 2 && isDama){
                SmartLogger.debugLogical("DamaEatFound");
                return moveAttempt.setDirection(MoveAttempt.Direction.KILL).setKilledPiece(killedPiece);
            }
        }
        return moveAttempt.setDirection(MoveAttempt.Direction.UNAVAILABLE);


        /*
        int intermediumXCoord = xCoord;
        int intermediumYCoord = yCoord;


        if (canBidirectMove || owner == PlayerType.PLAYER_ONE){
            if (targetXCoord == xCoord - 2 && targetYCoord == yCoord + 2) {intermediumXCoord -= 1; intermediumYCoord += 1;}
            if (targetXCoord == xCoord - 2 && targetYCoord == yCoord - 2) {intermediumXCoord -= 1; intermediumYCoord -= 1;}
        }
        if (canBidirectMove || owner == PlayerType.PLAYER_TWO){
            if (targetXCoord == xCoord + 2 && targetYCoord == yCoord + 2) {intermediumXCoord += 1; intermediumYCoord += 1;}
            if (targetXCoord == xCoord + 2 && targetYCoord == yCoord - 2) {intermediumXCoord += 1; intermediumYCoord -= 1;}
        }

        SquareField squareField = CheckersTheGame.instance.getSquareField(intermediumXCoord,intermediumYCoord);
        if (squareField != null && squareField.hasPiece() && squareField.getPiece().getOwner() != this.getOwner()){
            return new MoveAttempt(MoveAttempt.Direction.KILL).setKilledPiece(squareField.getPiece());
        }
        return new MoveAttempt(MoveAttempt.Direction.UNAVAILABLE);
        */
    }

    public void moveTo(SquareField squareField){
        this.getSquareField().setPiece(null);
        squareField.setPiece(this);
    }

    public void checkForTurnIntoDama(){
        if (!isDama && owner == PlayerType.PLAYER_ONE && this.getSquareField().getXCoord() == 0) isDama = true;
        if (!isDama && owner == PlayerType.PLAYER_TWO && this.getSquareField().getXCoord() == 7) isDama = true;
    }

    @Override
    public String toString() {
        return "[Piece Owner: " + owner + "" + this.getSquareField() + "]";
    }

    public boolean isDama() {
        return isDama;
    }
}
