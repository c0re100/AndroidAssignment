package moe.husky.paperscissorstone;

public class Player {
    private String playerName, oppoName, date, status;
    private int playerHand, oppoHand;
    private double time;

    public Player(String playerName, String oppoName, String date, double time, String status, int playerHand, int oppoHand) {
        this.playerName = playerName;
        this.oppoName = oppoName;
        this.date = date;
        this.time = time;
        this.status = status;
        this.playerHand = playerHand;
        this.oppoHand = oppoHand;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOppoName() {
        return oppoName;
    }

    public String getDate() {
        return date;
    }

    public double getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public int getPlayerHand() {
        return playerHand;
    }

    public int getOppoHand() {
        return oppoHand;
    }
}
