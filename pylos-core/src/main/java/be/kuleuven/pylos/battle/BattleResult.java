package be.kuleuven.pylos.battle;

import java.util.Collection;

public class BattleResult{
    public String p1Name;
    public String p2Name;

    public long runTime;
    public int p1StartP1Wins;
    public int p1StartDraws;
    public int p1StartP2Wins;
    public int p2StartP1Wins;
    public int p2StartDraws;
    public int p2StartP2Wins;

    public BattleResult(String p1Name, String p2Name, long runTime, int p1StartP1Wins, int p1StartDraws, int p1StartP2Wins, int p2StartP1Wins, int p2StartDraws, int p2StartP2Wins) {
        this.p1Name = p1Name;
        this.p2Name = p2Name;
        this.runTime = runTime;
        this.p1StartP1Wins = p1StartP1Wins;
        this.p1StartDraws = p1StartDraws;
        this.p1StartP2Wins = p1StartP2Wins;
        this.p2StartP1Wins = p2StartP1Wins;
        this.p2StartDraws = p2StartDraws;
        this.p2StartP2Wins = p2StartP2Wins;
    }

    public static BattleResult merge(Collection<BattleResult> brs){
        //assert all results are of the same players
        assert brs.stream().map(br -> br.p1Name).distinct().count() == 1;
        assert brs.stream().map(br -> br.p2Name).distinct().count() == 1;

        String p1Name = brs.stream().map(br -> br.p1Name).findFirst().get();
        String p2Name = brs.stream().map(br -> br.p2Name).findFirst().get();
        long runTime = brs.stream().mapToLong(br -> br.runTime).sum();
        int p1StartP1Wins = brs.stream().mapToInt(br -> br.p1StartP1Wins).sum();
        int p1StartDraws = brs.stream().mapToInt(br -> br.p1StartDraws).sum();
        int p1StartP2Wins = brs.stream().mapToInt(br -> br.p1StartP2Wins).sum();
        int p2StartP1Wins = brs.stream().mapToInt(br -> br.p2StartP1Wins).sum();
        int p2StartDraws = brs.stream().mapToInt(br -> br.p2StartDraws).sum();
        int p2StartP2Wins = brs.stream().mapToInt(br -> br.p2StartP2Wins).sum();

        return new BattleResult(p1Name, p2Name, runTime, p1StartP1Wins, p1StartDraws, p1StartP2Wins, p2StartP1Wins, p2StartDraws, p2StartP2Wins);
    }

    public int p1Wins(){
        return p1StartP1Wins + p2StartP1Wins;
    }

    public int p2Wins(){
        return p1StartP2Wins + p2StartP2Wins;
    }

    public void print(){
        int nGamesP1Start = p1StartP1Wins + p1StartDraws + p1StartP2Wins;
        int nGamesP2Start = p2StartP1Wins + p2StartDraws + p2StartP2Wins;
        int nGames = nGamesP1Start + nGamesP2Start;

        System.out.println("");
        System.out.println("----------------------------");
        System.out.println(nGamesP1Start + " games where " + p1Name + " starts:");
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) p1StartP1Wins / nGamesP1Start * 100)) + "% " + p1Name);
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) p1StartP2Wins / nGamesP1Start * 100)) + "% " + p2Name);
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) p1StartDraws / nGamesP1Start * 100)) + "% Draw");
        System.out.println();
        System.out.println(nGamesP2Start + " games where " + p2Name + " starts:");
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) p2StartP1Wins / nGamesP2Start * 100)) + "% " + p1Name);
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) p2StartP2Wins / nGamesP2Start * 100)) + "% " + p2Name);
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) p2StartDraws / nGamesP2Start * 100)) + "% Draw");
        System.out.println();
        System.out.println(nGames + " games in total:");
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) (p1StartP1Wins + p2StartP1Wins) / nGames * 100)) + "% " + p1Name);
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) (p1StartP2Wins + p2StartP2Wins) / nGames * 100)) + "% " + p2Name);
        System.out.println(String.format(" * %6s", String.format("%.2f", (double) (p1StartDraws + p2StartDraws) / nGames * 100)) + "% Draw");
        System.out.println();
        System.out.println("CPU Time: " + String.format("%.2f", (double) runTime / 1000) + " sec (" + String.format("%.2f", (double) runTime / 1000 / nGames) + " sec / game)");
        System.out.println("----------------------------");
    }
}
