package be.kuleuven.pylos.battle;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Random;

public class Battle {

	public static BattleResult play(PylosPlayer p1, PylosPlayer p2, int runs) {
		return play(p1, p2, runs, true);
	}

	public static BattleResult play(PylosPlayer p1, PylosPlayer p2, int runs, boolean print) {
		if (runs % 2 != 0) {
			throw new IllegalArgumentException("Please specify an even number of runs");
		}

		Random random = new Random();
		String p1Name = p1.getClass().getSimpleName();
		String p2Name = p2.getClass().getSimpleName();

		long totalPlayTime = 0;

		int p1StartP1Win = 0;
		int p1StartP2Win = 0;
		int p1StartDraw = 0;

		if (print) System.out.println("Starting battle: " + p1Name + " vs " + p2Name);

		for (int i = 0; i < runs / 2; i++) {
			if (print) System.out.print("*");
			PylosBoard board = new PylosBoard();
			PylosGame game = new PylosGame(board, p1, p2, random);
			long startTime = System.currentTimeMillis();
			game.play();
			long playTime = System.currentTimeMillis() - startTime;
			totalPlayTime += playTime;
			if(game.getState()==PylosGameState.DRAW) {
				p1StartDraw++;
			}else{
				if (game.getWinner() == p1) {
					p1StartP1Win++;
				} else {
					p1StartP2Win++;
				}
			}
		}

		int p2StartP1Win = 0;
		int p2StartP2Win = 0;
		int p2StartDraw = 0;

		for (int i = 0; i < runs / 2; i++) {
			if (print) System.out.print("*");
			PylosBoard board = new PylosBoard();
			PylosGame game = new PylosGame(board, p2, p1, random);
			long startTime = System.currentTimeMillis();
			game.play();
			long playTime = System.currentTimeMillis() - startTime;
			totalPlayTime += playTime;
			if(game.getState()==PylosGameState.DRAW) {
				p2StartDraw++;
			}else {
				if (game.getWinner() == p1) {
					p2StartP1Win++;
				} else {
					p2StartP2Win++;
				}
			}
		}

		if (print) System.out.println();

		BattleResult battleResult = new BattleResult(p1Name, p2Name, totalPlayTime, p1StartP1Win, p1StartDraw, p1StartP2Win, p2StartP1Win, p2StartDraw, p2StartP2Win);

		if(print) {
			battleResult.print();
		}

		return battleResult;
	}
}

