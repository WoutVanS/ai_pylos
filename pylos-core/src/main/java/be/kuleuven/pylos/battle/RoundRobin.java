package be.kuleuven.pylos.battle;

import be.kuleuven.pylos.player.PylosPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class RoundRobin {

    public static void play(Collection<RRCombination> combinations, int nRuns, int nThreads) {
        System.out.println("Starting round robin tournament: " + combinations.size() + " games of " + nRuns + " runs across " + nThreads + " threads");
        long start = System.currentTimeMillis();
        int i = 1;
        for (RRCombination combination : combinations) {
            System.out.println("\t" + i + ": " + combination.c1.getSimpleName() + " vs. " + combination.c2.getSimpleName());
            BattleResult result = BattleMT.play(combination.c1, combination.c2, nRuns, nThreads, false);
            combination.battleResult = result;
            i++;
        }
        long end = System.currentTimeMillis();
        System.out.println("tournament finished in " + (end - start) + " ms");
    }

    public static Set<RRCombination> createTournament(List<Class<? extends PylosPlayer>> players) {
        if (players.stream().map(p -> p.getSimpleName()).distinct().count() != players.size()) {
            throw new RuntimeException("All players must have unique names, please wrap players from the same class in separate wrapper classes");
        }

        Set<RRCombination> combinations = new HashSet<>();
        for (Class<? extends PylosPlayer> player : players) {
            combinations.addAll(createCombinationsForPlayer(player, players));
        }
        return combinations;
    }

    public static Set<RRCombination> createCombinationsForPlayer(Class<? extends PylosPlayer> player, List<Class<? extends PylosPlayer>> allPlayers) {
        Set<RRCombination> combinations = new HashSet<>();
        for (Class<? extends PylosPlayer> opponent : allPlayers) {
            if (opponent != player) {
                combinations.add(new RRCombination(player, opponent));
            }
        }
        return combinations;
    }

    public static void printWinsMatrix(Collection<BattleResult> results, List<Class<? extends PylosPlayer>> players) {
        //Formatted to be easily copy-pasted into Excel

        System.out.println();
        System.out.println("tournament results:");
        Integer[][] pointMatrix = new Integer[players.size()][players.size()];

        List<String> playerNames = players.stream().map(p -> p.getSimpleName()).collect(Collectors.toList());

        for (BattleResult result : results) {
            if (result != null) {
                int indexP1 = playerNames.indexOf(result.p1Name);
                int indexP2 = playerNames.indexOf(result.p2Name);
                pointMatrix[indexP1][indexP2] = result.p1StartP1Wins + result.p2StartP1Wins;
                pointMatrix[indexP2][indexP1] = result.p2StartP2Wins + result.p1StartP2Wins;
            } else {
                throw new RuntimeException("result is null");
            }
        }

        for (int i = 0; i < players.size(); i++) {
            System.out.print("\t" + players.get(i).getSimpleName());
        }
        System.out.println();
        for (int i = 0; i < players.size(); i++) {
            System.out.print(players.get(i).getSimpleName());
            for (int j = 0; j < players.size(); j++) {
                if (pointMatrix[i][j] == null) {
                    System.out.format("\t ");
                } else {
                    System.out.format("\t" + pointMatrix[i][j]);
                }
            }
            System.out.println();
        }
    }
}
