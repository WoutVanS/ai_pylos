

package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.*;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.Arrays;

/**
 * Created by Jan on 20/02/2015.
 */
public class StudentPlayer extends PylosPlayer {

	private static final boolean DEBUG = false;
	private static final int MAX_DEPTH = 3;
	private Action bestAction;

	private PylosGameSimulator simulator;
	private PylosBoard simulatorBoard;

	private PylosLocation[] locations;

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

		locations = board.getLocations();
		Arrays.sort(locations, (l1, l2) -> Integer.compare(l2.Z, l1.Z));

		/* game methods
		 * game.moveSphere(myReserveSphere, allLocations[0]); */
		if(DEBUG) System.out.println("student player color:" + PLAYER_COLOR);
		if(DEBUG)System.out.println("start simulator init");
		init(game, board);

		if(DEBUG)System.out.println("start minimax alogrithm with depth " + MAX_DEPTH);
		int eval = minimax(0, Integer.MIN_VALUE, Integer.MAX_VALUE);

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
		minimax(0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		game.removeSphere(bestAction.getSphere());
	}

	@Override
	public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
		/* game methods
		 * game.removeSphere(mySphere);
		 * game.pass() */

		init(game, board);
		minimax(0, Integer.MIN_VALUE, Integer.MAX_VALUE);

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
			eval = (color == PLAYER_COLOR)? eval+2 : eval -2;
		return eval;
	}
	public int minimax(int depth, int alpha, int beta){
		PylosGameState state = simulator.getState();
		PylosPlayerColor color = simulator.getColor();

		if(depth == MAX_DEPTH || state == PylosGameState.COMPLETED) {
			return evaluationFunction(state, color);
		}

		Action localBestAction = null;
		int MiniMaxEval = (color == PLAYER_COLOR)? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int eval = MiniMaxEval;

		PylosSphere[] spheres = simulatorBoard.getSpheres(color);
		Arrays.sort(spheres, (s1, s2) -> {
			if (s1.getLocation() != null && s2.getLocation() == null)return -1;
			if (s1.getLocation() == null && s2.getLocation() != null)return 1;
			else return 0;
		});


		for (PylosSphere sphere : spheres){


			PylosLocation prevLocation = sphere.getLocation();

			if(state == PylosGameState.MOVE){
				for(PylosLocation location : locations){
					if(sphere.canMoveTo(location)){
						simulator.moveSphere(sphere, location);
						eval = minimax( depth+1, alpha, beta);

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
				eval = minimax( depth+1, alpha, beta);

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

			}
			else if(state == PylosGameState.REMOVE_SECOND && !sphere.canRemove()){
				simulator.pass();
				eval = minimax( depth+1, alpha, beta);

				if ((color == PLAYER_COLOR && MiniMaxEval < eval) || (color != PLAYER_COLOR && MiniMaxEval > eval)) {
					MiniMaxEval = eval;
					localBestAction = new Action(null, null);
				}

				if(color == PLAYER_COLOR)
					alpha = Math.max(alpha, eval);
				else
					beta = Math.min(beta, eval);

				simulator.undoPass(state, color);
			}
			if (beta <= alpha) break;
		}

		bestAction = localBestAction;
		return MiniMaxEval;
	}
}




//package be.kuleuven.pylos.player.student;
//
//import be.kuleuven.pylos.game.*;
//import be.kuleuven.pylos.player.PylosPlayer;
//
//import java.util.Arrays;
//
///**
// * Created by Jan on 20/02/2015.
// */
//public class StudentPlayer extends PylosPlayer {
//
//	private static final boolean DEBUG = false;
//	private static final int MAX_DEPTH = 5;
//
//	private static PylosGameSimulator simulator;
//	private static PylosBoard simBoard;
//
//	private PylosLocation[] locations;
//
//	private Action bestAction;
//
//	private class Action{
//		private PylosLocation location;
//		private PylosSphere sphere;
//		public Action(){
//			this.location = null;
//			this.sphere = null;
//		};
//		public Action(PylosLocation location, PylosSphere sphere){
//			this.location = location;
//			this.sphere = sphere;
//		}
//		public PylosLocation getLocation() {
//			return location;
//		}
//		public PylosSphere getSphere() {
//			return sphere;
//		}
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
//		 * 	PylosLocation[] allLocations = board.getLocations();
//		 * 	PylosSphere[] allSpheres = board.getSpheres();
//		 * 	PylosSphere[] mySpheres = board.getSpheres(this);
//		 * 	PylosSphere myReserveSphere = board.getReserve(this); */
//
//			init(game, board);
//			minimax(MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
//			game.moveSphere(bestAction.getSphere(), bestAction.getLocation());
//
//	}
//
//
//
//	@Override
//	public void doRemove(PylosGameIF game, PylosBoard board) {
//		init(game, board);
//		minimax(MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
//		game.removeSphere(bestAction.getSphere());
//	}
//
//	@Override
//	public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
//		init(game, board);
//		minimax(MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
//
//		if(bestAction.getSphere() == null)
//			game.pass();
//		else
//			game.removeSphere(bestAction.getSphere());
//	}
//
//	private void init(PylosGameIF game, PylosBoard board){
//		simBoard = board;
//		simulator = new PylosGameSimulator(game.getState(), PLAYER_COLOR, simBoard);
//
//		locations = board.getLocations();
//	}
//
//	public int evaluationFunction(PylosGameState state,PylosPlayerColor color){
//		int eval = simBoard.getReservesSize(PLAYER_COLOR) - simBoard.getReservesSize(PLAYER_COLOR.other());
//		return eval;
//	}
//	public int minimax(int depth, int alpha, int beta){
//		boolean reserveSphereChecked = false;
//		PylosGameState currentState = simulator.getState();
//		PylosPlayerColor currentColor = simulator.getColor();
//
//		if(depth == 0 || currentState == PylosGameState.COMPLETED)
//			return evaluationFunction(currentState, currentColor);
//
//		PylosSphere [] spheres = simBoard.getSpheres(currentColor);
//
//		Arrays.sort(spheres, (s1, s2) -> {
//		if (s1.getLocation() != null && s2.getLocation() == null)return -1;
//		if (s1.getLocation() == null && s2.getLocation() != null)return 1;
//		else return 0;
//		});
//
//		Action localBestAction = null;
//		int MiniMaxEval = (currentColor == PLAYER_COLOR)? Integer.MIN_VALUE : Integer.MAX_VALUE;
//		int eval = MiniMaxEval;
//
//		for (PylosSphere sphere : spheres){
//			if(sphere.getLocation() == null) {
//				if (!reserveSphereChecked) reserveSphereChecked = true;
//				else break;
//			}
//			if(!sphere.canMove()) continue;
//
//			PylosLocation prevLocation = sphere.getLocation();
//
//			if(currentState == PylosGameState.MOVE) {
//				for (PylosLocation location : locations) {
//
//					simulator.moveSphere(sphere, location);
//					eval = minimax(depth - 1, alpha, beta);
//
//					if ((currentColor == PLAYER_COLOR && MiniMaxEval < eval) || (currentColor != PLAYER_COLOR && MiniMaxEval > eval)) {
//						MiniMaxEval = eval;
//						localBestAction = new Action(location, sphere);
//					}
//
//					if (currentColor == PLAYER_COLOR)
//						alpha = Math.max(alpha, eval);
//					else
//						beta = Math.min(beta, eval);
//
//					if (prevLocation != null)
//						simulator.undoMoveSphere(sphere, prevLocation, PylosGameState.MOVE, currentColor);
//					else
//						simulator.undoAddSphere(sphere, PylosGameState.MOVE, currentColor);
//
//					if (beta <= alpha) break;
//
//				}
//
//			}else if((currentState == PylosGameState.REMOVE_FIRST || currentState == PylosGameState.REMOVE_SECOND) && sphere.canRemove()){
//
//				simulator.removeSphere(sphere);
//				eval = minimax( depth-1, alpha, beta);
//
//				if ((currentColor == PLAYER_COLOR && MiniMaxEval < eval) || (currentColor != PLAYER_COLOR && MiniMaxEval > eval)) {
//					MiniMaxEval = eval;
//					localBestAction = new Action(null, sphere);
//				}
//
//				if(currentColor == PLAYER_COLOR)
//					alpha = Math.max(alpha, eval);
//				else
//					beta = Math.min(beta, eval);
//
//				if (currentState == PylosGameState.REMOVE_FIRST)
//					simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.MOVE, currentColor);
//				else
//					simulator.undoRemoveSecondSphere(sphere, prevLocation, PylosGameState.MOVE, currentColor);
//			}
//			else if(currentState == PylosGameState.REMOVE_SECOND && !sphere.canRemove()){
//
//				simulator.pass();
//				eval = minimax( depth-1, alpha, beta);
//
//				if ((currentColor == PLAYER_COLOR && MiniMaxEval < eval) || (currentColor != PLAYER_COLOR && MiniMaxEval > eval)) {
//					MiniMaxEval = eval;
//					localBestAction = new Action(null, null);
//				}
//
//				if(currentColor == PLAYER_COLOR)
//					alpha = Math.max(alpha, eval);
//				else
//					beta = Math.min(beta, eval);
//
//				simulator.undoPass(currentState, currentColor);
//			}
//
//			if (beta <= alpha) break;
//		}
//
//		bestAction = localBestAction;
//		return MiniMaxEval;
//	}
//}
