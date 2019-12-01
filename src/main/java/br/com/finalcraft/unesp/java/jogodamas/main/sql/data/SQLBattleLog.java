package br.com.finalcraft.unesp.java.jogodamas.main.sql.data;

import br.com.finalcraft.evernifecore.cooldown.FCTimeFrame;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLBattleLog implements Comparable<SQLBattleLog>{

    private String playerOne;
    private String playerTwo;

    private int playerOneDamas;
    private int playerTwoDamas;

    private String winner;

    private FCTimeFrame date;

    public SQLBattleLog(ResultSet resultSet) throws SQLException {
        this.playerOne = resultSet.getString("playerone");
        this.playerTwo = resultSet.getString("playertwo");
        this.playerOneDamas = resultSet.getInt("playeronedamas");
        this.playerTwoDamas = resultSet.getInt("playertwodamas");
        this.winner = resultSet.getString("winner");
        this.date = new FCTimeFrame(resultSet.getLong("date"));
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public int getPlayerOneDamas() {
        return playerOneDamas;
    }

    public int getPlayerTwoDamas() {
        return playerTwoDamas;
    }

    public String getWinner() {
        return winner;
    }

    public FCTimeFrame getDate() {
        return date;
    }

    public boolean isPlayer(String playerName){
        return playerOne.equals(playerName) || playerTwo.equals(playerName);
    }

    public boolean isWinner(String playerName){
        return winner.equals(playerName);
    }

    public boolean isPlayerOne(String playerName){
        return playerOne.equals(playerName);
    }

    public int getDamasOf(String playerName){
        return isPlayerOne(playerName) ? playerOneDamas : playerTwoDamas;
    }

    public int getDamasOfOpponentOf(String playerName){
        return !isPlayerOne(playerName) ? playerOneDamas : playerTwoDamas;
    }

    @Override
    public String toString() {
        return "[playerOne: " + playerOne +
                ", playerTwo: " + playerTwo +
                ", playerOneDamas: " + playerOneDamas +
                ", playerTwoDamas: " + playerTwoDamas +
                ", winner: " + winner +
                ", date: " + date.getFormatedFullDate() +
                "]";
    }

    @Override
    public int compareTo(SQLBattleLog o) {
        return Long.compare(this.date.getMillis(),o.date.getMillis());
    }
}
