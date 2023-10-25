//package be.kuleuven.pylos.player.student;
//
//import be.kuleuven.pylos.game.*;
//import be.kuleuven.pylos.player.PylosPlayer;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Jan on 20/02/2015.
// */
//public class StudentPlayer extends PylosPlayer {
//
//	private static final boolean DEBUG = false;
//	private static final int MAX_DEPTH = 3;
//	private Action bestAction;
//
//	private PylosGameSimulator simulator;
//	private PylosBoard simulatorBoard;
//
//	private class Action{
//		private PylosLocation location;
//		private PylosSphere sphere;
//
//		public Action(){
//			this.location = null;
//			this.sphere = null;
//		};
//
//		public Action(PylosLocation location, PylosSphere sphere){
//			this.location = location;
//			this.sphere = sphere;
//		}
//
//		public PylosLocation getLocation() {
//			return location;
//		}
//
//		public PylosSphere getSphere() {
//			return sphere;
//		}
//
//		@Override
//		public String toString() {
//			return "Action{" +
//					"location=" + location +
//					", sphere=" + sphere +
//					'}';
//		}
//	}
//
//	@Override
//	public void doMove(PylosGameIF game, PylosBoard board) {
//		/* board methods
//			* 	PylosLocation[] allLocations = board.getLocations();
//			* 	PylosSphere[] allSpheres = board.getSpheres();
//			* 	PylosSphere[] mySpheres = board.getSpheres(this);
//			* 	PylosSphere myReserveSphere = board.getReserve(this); */
//
//		/* game methods
//			* game.moveSphere(myReserveSphere, allLocations[0]); */
//		if(DEBUG) System.out.println("student player color:" + PLAYER_COLOR);
//		if(DEBUG)System.out.println("start simulator init");
//		init(game, board);
//
//		if(DEBUG)System.out.println("start minimax alogrithm with depth " + MAX_DEPTH);
//		if(board.getSpheres(PLAYER_COLOR.other()).length - board.getReservesSize(PLAYER_COLOR.other()) ==  3)
//			System.out.println("test");
//		int eval = minimax(0);
//
//		if(DEBUG)System.out.println("evaluation: " + eval);
//		if(DEBUG)System.out.println("game state: " + game.getState().toString());
//		if(DEBUG)System.out.println("best action: " + bestAction.toString());
//		if(DEBUG)System.out.println();
//		game.moveSphere(bestAction.getSphere(), bestAction.getLocation());
//	}
//
//
//
//	@Override
//	public void doRemove(PylosGameIF game, PylosBoard board) {
//		/* game methods
//			* game.removeSphere(mySphere); */
//
//		init(game, board);
//		minimax(0);
//		game.removeSphere(bestAction.getSphere());
//	}
//
//	@Override
//	public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
//		/* game methods
//			* game.removeSphere(mySphere);
//			* game.pass() */
//
//		init(game, board);
//		minimax(0);
//
//		if(bestAction.getSphere() == null)
//			game.pass();
//		else
//			game.removeSphere(bestAction.getSphere());
//	}
//
//	private void init(PylosGameIF game, PylosBoard board){
//		this.simulator = new PylosGameSimulator(game.getState(), PLAYER_COLOR, board);
//		this.simulatorBoard = board;
//		this.bestAction = new Action();
//
//	}
//	private int minimax(int depth){
//		Action localBestAction = null;
//
//		PylosGameState state = simulator.getState();
//		PylosPlayerColor color = simulator.getColor();
//
//		if(depth == MAX_DEPTH || state == PylosGameState.COMPLETED) {
//			int eval = this.simulatorBoard.getReservesSize(PLAYER_COLOR) - this.simulatorBoard.getReservesSize(PLAYER_COLOR.other());
//			//System.out.println("max depth is reached, value is " + eval);
//			if(state == PylosGameState.REMOVE_FIRST || state == PylosGameState.REMOVE_SECOND)
//				eval = (color == PLAYER_COLOR)? eval+10 : eval -10;
//			return eval;
//		}
//
//		List<Action> actions = calculateActions();
//		//System.out.println("calculated actions are: " + actions.toString());
//
//		if (color == PLAYER_COLOR){
//			int maxEval = -999;
//			for(Action action : actions){
//
//				PylosSphere sphere = action.getSphere();
//				PylosLocation location = action.getLocation();
//				PylosLocation prevLocation = sphere.getLocation();
//
//				int eval = maxEval;
//
//				switch (state){
//					case MOVE:
//						simulator.moveSphere(sphere, location);
//						eval = minimax( depth+1);
//						if (prevLocation != null)
//							simulator.undoMoveSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						else
//							simulator.undoAddSphere(sphere, PylosGameState.MOVE, color);
//						break;
//					case REMOVE_FIRST:
//						simulator.removeSphere(sphere);
//						eval = minimax(depth+1);
//						simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						break;
//					case REMOVE_SECOND:
//						simulator.removeSphere(sphere);
//						eval = minimax(depth+1);
//						simulator.undoRemoveSecondSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						break;
//				}
//
//				if (maxEval < eval){
//					maxEval = eval;
//					localBestAction = action;
//				}
//			}
//			bestAction = localBestAction;
//			return maxEval;
//		}
//		else {
//			int minEval = +999;
//			for(Action action : actions){
//
//				PylosSphere sphere = action.getSphere();
//				PylosLocation location = action.getLocation();
//				PylosLocation prevLocation = sphere.getLocation();
//
//				int eval = minEval;
//
//				switch (state){
//					case MOVE:
//						simulator.moveSphere(sphere, location);
//						eval = minimax(depth+1);
//						if (prevLocation != null)
//							simulator.undoMoveSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						else
//							simulator.undoAddSphere(sphere, PylosGameState.MOVE, color);
//						break;
//					case REMOVE_FIRST:
//						simulator.removeSphere(sphere);
//						eval = minimax(depth+1);
//						simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						break;
//					case REMOVE_SECOND:
//						simulator.removeSphere(sphere);
//						eval = minimax(depth+1);
//						simulator.undoRemoveSecondSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						break;
//				}
//
//				if (minEval > eval){
//					minEval = eval;
//					localBestAction = action;
//				}
//			}
//			bestAction = localBestAction;
//			return minEval;
//		}
//    }
//
//	private List<Action> calculateActions(){
//		List<Action> actions = new ArrayList<>();
//		PylosGameState state = simulator.getState();
//		PylosPlayerColor color = simulator.getColor();
//
//
//		switch (state){
//			case MOVE:
//				for (PylosSphere sphere : simulatorBoard.getSpheres(color)){
//					for(PylosLocation location : simulatorBoard.getLocations()){
//						if(sphere.canMoveTo(location))
//							actions.add(new Action(location, sphere));
//					}
//				}
//				break;
//			case REMOVE_FIRST, REMOVE_SECOND:
//				for (PylosSphere sphere : simulatorBoard.getSpheres(color)){
//					if(sphere.canRemove())
//						actions.add(new Action(null, sphere));
//				}
//				break;
//			default: actions = null;
//        }
//
//		return actions;
//	}
//
//}

package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan on 20/02/2015.
 */
public class StudentPlayer extends PylosPlayer {

	private static final boolean DEBUG = false;
	private static final int MAX_DEPTH = 4;
	private Action bestAction;

	private PylosGameSimulator simulator;
	private PylosBoard simulatorBoard;

	private class Action{
		private PylosLocation location;
		private PylosSphere sphere;

		public Action(){
			this.location = null;
			this.sphere = null;
		};

		public Action(PylosLocation location, PylosSphere sphere){
			this.location = location;
			this.sphere = sphere;
		}

		public PylosLocation getLocation() {
			return location;
		}

		public PylosSphere getSphere() {
			return sphere;
		}

		@Override
		public String toString() {
			return "Action{" +
					"location=" + location +
					", sphere=" + sphere +
					'}';
		}
	}

	@Override
	public void doMove(PylosGameIF game, PylosBoard board) {
		/* board methods
		 * 	PylosLocation[] allLocations = board.getLocations();
		 * 	PylosSphere[] allSpheres = board.getSpheres();
		 * 	PylosSphere[] mySpheres = board.getSpheres(this);
		 * 	PylosSphere myReserveSphere = board.getReserve(this); */

		/* game methods
		 * game.moveSphere(myReserveSphere, allLocations[0]); */
		if(DEBUG) System.out.println("student player color:" + PLAYER_COLOR);
		if(DEBUG)System.out.println("start simulator init");
		init(game, board);

		if(DEBUG)System.out.println("start minimax alogrithm with depth " + MAX_DEPTH);
		if(board.getSpheres(PLAYER_COLOR.other()).length - board.getReservesSize(PLAYER_COLOR.other()) ==  3)
			System.out.println("test");
		int eval = minimax(0, -999, +999);

		if(DEBUG)System.out.println("evaluation: " + eval);
		if(DEBUG)System.out.println("game state: " + game.getState().toString());
		if(DEBUG)System.out.println("best action: " + bestAction.toString());
		if(DEBUG)System.out.println();
		if(bestAction == null)
			System.err.println("de bestaction is nul");
		game.moveSphere(bestAction.getSphere(), bestAction.getLocation());
	}



	@Override
	public void doRemove(PylosGameIF game, PylosBoard board) {
		/* game methods
		 * game.removeSphere(mySphere); */

		init(game, board);
		minimax(0, -999, +999);
		game.removeSphere(bestAction.getSphere());
	}

	@Override
	public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
		/* game methods
		 * game.removeSphere(mySphere);
		 * game.pass() */

		init(game, board);
		minimax(0, -999, +999);

		if(bestAction.getSphere() == null)
			game.pass();
		else
			game.removeSphere(bestAction.getSphere());
	}

	private void init(PylosGameIF game, PylosBoard board){
		this.simulator = new PylosGameSimulator(game.getState(), PLAYER_COLOR, board);
		this.simulatorBoard = board;
		this.bestAction = new Action();

	}

	public int evaluationFunction(PylosGameState state,PylosPlayerColor color){
		int eval = this.simulatorBoard.getReservesSize(PLAYER_COLOR) - this.simulatorBoard.getReservesSize(PLAYER_COLOR.other());
		//System.out.println("max depth is reached, value is " + eval);
		if(state == PylosGameState.REMOVE_FIRST || state == PylosGameState.REMOVE_SECOND)
			eval = (color == PLAYER_COLOR)? eval+10 : eval -10;
		return eval;
	}
	public int minimax(int depth, int alpha, int beta){
		PylosGameState state = simulator.getState();
		PylosPlayerColor color = simulator.getColor();

		if(depth == MAX_DEPTH || state == PylosGameState.COMPLETED)
			return evaluationFunction(state, color);

		Action localBestAction = null;
		int MiniMaxEval = (color == PLAYER_COLOR)? -999 : +999;

		for (PylosSphere sphere : simulatorBoard.getSpheres(color)){
			PylosLocation prevLocation = sphere.getLocation();

			if(state == PylosGameState.MOVE){
				for(PylosLocation location : simulatorBoard.getLocations()){
					if(sphere.canMoveTo(location)){
						simulator.moveSphere(sphere, location);
						int eval = minimax( depth+1, alpha, beta);

						if ((color == PLAYER_COLOR && MiniMaxEval < eval) || (color != PLAYER_COLOR && MiniMaxEval > eval)) {
							MiniMaxEval = eval;
							localBestAction = new Action(location, sphere);
						}

						if(color == PLAYER_COLOR)
							alpha = Math.max(alpha, eval);
						else
							beta = Math.min(beta, eval);

						if (prevLocation != null)
							simulator.undoMoveSphere(sphere, prevLocation, PylosGameState.MOVE, color);
						else
							simulator.undoAddSphere(sphere, PylosGameState.MOVE, color);

						if (beta <= alpha) break;
					}
				}

			}else if((state == PylosGameState.REMOVE_FIRST || state == PylosGameState.REMOVE_SECOND) && sphere.canRemove()){
				simulator.removeSphere(sphere);
				int eval = minimax( depth+1, alpha, beta);

				if ((color == PLAYER_COLOR && MiniMaxEval < eval) || (color != PLAYER_COLOR && MiniMaxEval > eval)) {
					MiniMaxEval = eval;
					localBestAction = new Action(null, sphere);
				}

				if(color == PLAYER_COLOR)
					alpha = Math.max(alpha, eval);
				else
					beta = Math.min(beta, eval);

				if (state == PylosGameState.REMOVE_FIRST)
					simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.MOVE, color);
				else
					simulator.undoRemoveSecondSphere(sphere, prevLocation, PylosGameState.MOVE, color);

				if (beta <= alpha) break;
			}

		}
		bestAction = localBestAction;
		return MiniMaxEval;
	}
}



//	public int minimax(int depth){
//		Action localBestAction = null;
//
//		PylosGameState state = simulator.getState();
//		PylosPlayerColor color = simulator.getColor();
//
//		if(depth == MAX_DEPTH || state == PylosGameState.COMPLETED)
//			return evaluationFunction(state, color);
//
//
//		int MiniMaxEval = (color == PLAYER_COLOR)? -999 : +999;
//		switch (state){
//			case MOVE:
//				for (PylosSphere sphere : simulatorBoard.getSpheres(color)){
//					for(PylosLocation location : simulatorBoard.getLocations()){
//						if(sphere.canMoveTo(location)){
//							PylosLocation prevLocation = sphere.getLocation();
//
//							simulator.moveSphere(sphere, location);
//							int eval = minimax( depth+1);
//
//							if (prevLocation != null)
//								simulator.undoMoveSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//							else
//								simulator.undoAddSphere(sphere, PylosGameState.MOVE, color);
//
//							if (color == PLAYER_COLOR) {
//								if (MiniMaxEval < eval) {
//									MiniMaxEval = eval;
//									localBestAction = new Action(location, sphere);
//								}
//							}
//							else{
//								if (MiniMaxEval > eval){
//									MiniMaxEval = eval;
//									localBestAction = new Action(location, sphere);
//								}
//							}
//						}
//					}
//				}
//				break;
//			case REMOVE_FIRST, REMOVE_SECOND:
//				for (PylosSphere sphere : simulatorBoard.getSpheres(color)){
//					if(sphere.canRemove()){
//						PylosLocation prevLocation = sphere.getLocation();
//
//						simulator.removeSphere(sphere);
//						int eval = minimax(depth+1);
//
//						if (state == PylosGameState.REMOVE_FIRST)
//							simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						else
//							simulator.undoRemoveSecondSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//
//						if (color == PLAYER_COLOR) {
//							if (MiniMaxEval < eval) {
//								MiniMaxEval = eval;
//								localBestAction = new Action(null, sphere);
//							}
//						}
//						else{
//							if (MiniMaxEval > eval){
//								MiniMaxEval = eval;
//								localBestAction = new Action(null, sphere);
//							}
//						}
//						break;
//					}
//				}
//				break;
//		}
//		bestAction = localBestAction;
//		return MiniMaxEval;
//	}

