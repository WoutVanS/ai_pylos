package be.kuleuven.pylos.testbattle;

import be.kuleuven.pylos.battle.Battle;
import be.kuleuven.pylos.player.PylosPlayer;
import be.kuleuven.pylos.player.codes.PylosPlayerBestFit;
import be.kuleuven.pylos.player.codes.PylosPlayerMiniMax;
import be.kuleuven.pylos.player.student.StudentPlayer;

/**
 * Created by Jan on 20/03/2015.
 */
public class TestMain {

	public static void main(String[] args) {

		PylosPlayer bf = new PylosPlayerBestFit();
		PylosPlayer mm2 = new PylosPlayerMiniMax(2);
		PylosPlayer mm5 = new PylosPlayerMiniMax(5);
		PylosPlayer mm8 = new PylosPlayerMiniMax(8);

		PylosPlayer[] players = new PylosPlayer[]{bf, mm2, mm5, mm8};

		for(PylosPlayer codes : players){
			PylosPlayer student = new StudentPlayer();
			int wins = Battle.play(student, codes, 100, false).p1Wins();
			System.out.println(wins);
		}

	}

}
