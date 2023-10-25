package be.kuleuven.pylos.battle;

import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BattleMT {

    static int N_RUNS_PER_JOB = 10; //has to be even

    public static BattleResult play(Class<? extends PylosPlayer> c1, Class<? extends PylosPlayer> c2, int runs, int nThreads) {
        return play(c1, c2, runs, nThreads, true);
    }

    public static BattleResult play(Class<? extends PylosPlayer> c1, Class<? extends PylosPlayer> c2, int runs, int nThreads, boolean print) {
        //10 games per battle
        int nTasks = runs / N_RUNS_PER_JOB;
        int rest = runs % N_RUNS_PER_JOB;

        ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        List<BattleRunnable> battleRunnables = new ArrayList<>();

        for (int i = 0; i < nTasks; i++) {
            BattleRunnable r = new BattleRunnable(c1, c2, N_RUNS_PER_JOB);
            battleRunnables.add(r);
            pool.execute(r);
        }

        if (rest > 0) {
            BattleRunnable r = new BattleRunnable(c1, c2, rest);
            battleRunnables.add(r);
            pool.execute(r);
        }
        pool.shutdown();

        try {
            //wait until the pool has processed all games
            long timeout = runs * 10; //timeout of 10 seconds per game
            pool.awaitTermination(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (battleRunnables.stream().anyMatch(r -> r.result == null)) {
            throw new RuntimeException("Not all battles were completed!");
        }

        BattleResult result = BattleResult.merge(battleRunnables.stream().map(r -> r.result).collect(Collectors.toList()));

        if (print) {
            result.print();
        }

        return result;
    }

    private static class BattleRunnable implements Runnable {
        private Class<? extends PylosPlayer> c1;
        private Class<? extends PylosPlayer> c2;
        private int nRuns;
        private BattleResult result;

        public BattleRunnable(Class<? extends PylosPlayer> c1, Class<? extends PylosPlayer> c2, int nRuns) {
            this.c1 = c1;
            this.c2 = c2;
            this.nRuns = nRuns;
        }

        @Override
        public void run() {
            try {
                PylosPlayer p1 = c1.getConstructor().newInstance();
                PylosPlayer p2 = c2.getConstructor().newInstance();
                result = Battle.play(p1, p2, nRuns, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
