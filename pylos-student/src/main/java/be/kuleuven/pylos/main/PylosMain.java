package be.kuleuven.pylos.main;

import be.kuleuven.pylos.battle.*;
import be.kuleuven.pylos.battle.wrappers.PylosPlayerMiniMax2;
import be.kuleuven.pylos.battle.wrappers.PylosPlayerMiniMax4;
import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGame;
import be.kuleuven.pylos.game.PylosGameObserver;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.PylosPlayerObserver;
import be.kuleuven.pylos.player.codes.PylosPlayerBestFit;
import be.kuleuven.pylos.player.codes.PylosPlayerMiniMax;
import be.kuleuven.pylos.player.codes.PylosPlayerRandomFit;
import be.kuleuven.pylos.player.student.StudentPlayer;

import java.util.*;
import java.util.stream.Collectors;

public class PylosMain {

    public static void main(String[] args) {
        /* !!! jvm argument !!! -ea */

        //startSingleGame();
        startBattle();
        //startBattleMultithreaded();
        //startRoundRobinTournament();
    }

    public static void startSingleGame() {

        Random random = new Random(0);

        PylosPlayer playerLight = new PylosPlayerRandomFit();
        PylosPlayer playerDark = new PylosPlayerMiniMax(2);

        PylosBoard pylosBoard = new PylosBoard();
        PylosGame pylosGame = new PylosGame(pylosBoard, playerLight, playerDark, random, PylosGameObserver.CONSOLE_GAME_OBSERVER, PylosPlayerObserver.NONE);

        pylosGame.play();
    }

    public static void startBattle() {
        int nRuns = 100;

        PylosPlayer p1 = new StudentPlayer();
        PylosPlayer p2 = new PylosPlayerMiniMax4();

        Battle.play(p1, p2, nRuns);
    }

    public static void startBattleMultithreaded(){
        //Please ensure all players have a default no-argument constructor,
        //otherwise the RoundRobin class will not be able to instantiate them.
        //If required, create wrapper classes around variants of the same class

        //Please avoid using Collections.shuffle(List<?> list),
        //as this is not ideal for use across multiple threads.
        //Use Collections.shuffle(List<?> list, Random random) instead, with the Random object from the player

        int nRuns = 100;
        int nThreads =10 ;

        Class<? extends PylosPlayer> c1 = StudentPlayer.class;
        Class<? extends PylosPlayer> c2 = PylosPlayerMiniMax4.class;

        BattleMT.play(c1, c2, nRuns, nThreads);
    }

    public static void startRoundRobinTournament(){
        //Same requirements apply as for startBattleMultithreaded()

        List<Class<? extends PylosPlayer>> players = new ArrayList<>();

        players.add(PylosPlayerRandomFit.class);
        players.add(PylosPlayerBestFit.class);
        players.add(PylosPlayerMiniMax2.class);
        players.add(PylosPlayerMiniMax4.class);

        int nRunsPerCombination = 1000;
        int nThreads = 8;

        Set<RRCombination> combinations = RoundRobin.createTournament(players);
        RoundRobin.play(combinations, nRunsPerCombination, nThreads);

        List<BattleResult> results = combinations.stream().map(c -> c.battleResult).collect(Collectors.toList());

        RoundRobin.printWinsMatrix(results, players);
    }
}
