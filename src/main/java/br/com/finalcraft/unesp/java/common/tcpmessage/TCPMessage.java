package br.com.finalcraft.unesp.java.common.tcpmessage;

import java.io.Serializable;
import java.math.BigInteger;

public abstract class TCPMessage implements Serializable {

    TCPMessageDirection direction;
    public TCPMessageDirection getDirection() {
        return direction;
    }

    public static class Calculator extends TCPMessage{
        String theExpression;

        public String getTheExpression() {
            return theExpression;
        }

        public Calculator(String theExpression, TCPMessageDirection direction) {
            this.theExpression = theExpression;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "[" + direction + " - TheExpression: \"" + theExpression + "\"]";
        }
    }

}
