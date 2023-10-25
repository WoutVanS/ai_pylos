package be.kuleuven.pylos.battle;

import be.kuleuven.pylos.battle.wrappers.PylosPlayerMiniMax2;
import be.kuleuven.pylos.battle.wrappers.PylosPlayerMiniMax4;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.codes.PylosPlayerBestFit;
import be.kuleuven.pylos.player.codes.PylosPlayerMiniMax;
import be.kuleuven.pylos.player.codes.PylosPlayerRandomFit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoundRobinMain {

    static int N_THREADS = 8;
    static int N_RUNS_PER_GAME = 10000;

    public static void main(String[] args) {
        List<Class<? extends PylosPlayer>> players = new ArrayList<>();

        //Please ensure all players have a default no-argument constructor,
        //otherwise the RoundRobin class will not be able to instantiate them.
        //If required, create wrapper classes around variants of the same player

        //Ensure your player does not use Collections.shuffle(List<?> list),
        //as this is not thread-safe. Use Random instead.
        //(Collections.shuffle(List<?> list, Random random) is thread-safe)

        players.add(PylosPlayerRandomFit.class);
        players.add(PylosPlayerBestFit.class);
        players.add(PylosPlayerMiniMax2.class);
        players.add(PylosPlayerMiniMax4.class);

        Set<RRCombination> combinations = RoundRobin.createTournament(players);
        RoundRobin.play(combinations, N_RUNS_PER_GAME, N_THREADS);
        List<BattleResult> results = combinations.stream().map(c -> c.battleResult).collect(Collectors.toList());
        RoundRobin.printWinsMatrix(results, players);
    }
}
