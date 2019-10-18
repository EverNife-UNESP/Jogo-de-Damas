package br.com.finalcraft.unesp.java.jogodamas.common.tcpmessage;

import br.com.finalcraft.unesp.java.jogodamas.common.application.CheckersTheGame;

import java.io.Serializable;

public abstract class TCPMessage implements Serializable {

    TCPMessageDirection direction;
    public TCPMessageDirection getDirection() {
        return direction;
    }

    public static class CheckersTable extends TCPMessage{

        CheckersTheGame theTable;

        public CheckersTable(CheckersTheGame theTable, TCPMessageDirection direction) {
            this.theTable = theTable;
            this.direction = direction;
        }

        public CheckersTheGame getTheTable() {
            return theTable;
        }

        @Override
        public String toString() {
            return "[" + direction + " - The Entire GameBoard: " + theTable + "]";
        }
    }

}
