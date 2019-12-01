package br.com.finalcraft.unesp.java.jogodamas.main.sql.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLPlayer implements Comparable<SQLPlayer>{

    private final String name;

    private int wins = 0;
    private int draws = 0;
    private int loses = 0;
    private int damasGiven = 0;
    private int damasTaken = 0;
    private int pontos = 0;

    public SQLPlayer(ResultSet resultSet, List<SQLBattleLog> allBattles) throws SQLException {
        this.name = resultSet.getString("name");
        for (SQLBattleLog aBattle : allBattles) {
            if (aBattle.isPlayer(name)){
                if (aBattle.getWinner().equals("Empate")){
                    pontos++;
                }else if (aBattle.isWinner(name)){
                    pontos+=3;
                    wins++;
                }else {
                    loses++;
                }

                damasGiven += aBattle.getDamasOf(name);
                damasTaken += aBattle.getDamasOfOpponentOf(name);
            }
        }
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLoses() {
        return loses;
    }

    public int getDamasGiven() {
        return damasGiven;
    }

    public int getDamasTaken() {
        return damasTaken;
    }

    public int getPontos() {
        return pontos;
    }

    public String getName() {
        return name;
    }

    public int getSaldo(){
        return this.damasGiven - this.damasTaken;
    }

    @Override
    public int compareTo(SQLPlayer other) {
        return Integer.compare(this.pontos,other.pontos);
    }

    @Override
    public String toString() {
        return "[name: " + name +
                ", wins: " + wins +
                ", loses: " + loses +
                ", damasGiven: " + damasGiven +
                ", damasTaken: " + damasTaken +
                ", pontos: " + pontos +
                "]";
    }
}
