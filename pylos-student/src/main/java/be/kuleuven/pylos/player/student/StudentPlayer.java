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

import java.io.Serializable;
import java.util.*;

public class StudentPlayer extends PylosPlayer {

	private static final boolean DEBUG = false;
	private static final int MAX_DEPTH = 5;
	private Action bestAction;

	private HashMap<String, Integer> prevResults = new HashMap<>();

	private PylosGameSimulator simulator;
	private PylosBoard simulatorBoard;

	private class Action {
		private PylosLocation location;
		private PylosSphere sphere;

		public Action() {
			this.location = null;
			this.sphere = null;
		}

		;

		public Action(PylosLocation location, PylosSphere sphere) {
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

		init(game, board);
		int eval = minimax(0, -999, +999);
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

		if (bestAction == null)
			game.pass();
		else
			game.removeSphere(bestAction.getSphere());
	}

	private void init(PylosGameIF game, PylosBoard board) {
		this.simulator = new PylosGameSimulator(game.getState(), PLAYER_COLOR, board);
		this.simulatorBoard = board;
		this.bestAction = new Action();

	}

	public int evaluationFunction(PylosGameState state, PylosPlayerColor color) {
		int eval = this.simulatorBoard.getReservesSize(PLAYER_COLOR) - this.simulatorBoard.getReservesSize(PLAYER_COLOR.other());
		//werken naar een square toe is meer waard
		if (state == PylosGameState.REMOVE_FIRST || state == PylosGameState.REMOVE_SECOND) // TODO Waarom 2 verminderen als second (dan heb je toch al 1 verwijdert dus verwijder je er te veel)
			eval = (color == PLAYER_COLOR) ? eval + 2 : eval - 2;
		return eval;
	}

	public int minimax(int depth, int alpha, int beta) {
		PylosGameState state = simulator.getState();
		PylosPlayerColor color = simulator.getColor();

		int MiniMaxEval = 0;

		if (depth == MAX_DEPTH) {
			return evaluationFunction(state, color);
		}

		if (state == PylosGameState.COMPLETED) {
			if (simulator.getWinner() == PLAYER_COLOR) MiniMaxEval = 990;
			else MiniMaxEval = -990;
		}else if (state == PylosGameState.MOVE) {
			MiniMaxEval = minimaxMove(depth, alpha);
		} else if (state == PylosGameState.REMOVE_FIRST) {
			MiniMaxEval = minimaxRemoveFirst(depth, beta);
		} else if (state == PylosGameState.REMOVE_SECOND) {
			MiniMaxEval = minimaxRemoveSecond(depth, beta);
		}

		return MiniMaxEval;
	}

	//problemen met huidige implementatie
	// player probeert nooit een square te vormen (wel blocken)
	// player legt nooit altijd resrvebal op laagste verdiep, nooit op hoger (zelfs als er een plaats vrij is)

	public int minimaxMove(int depth, int alpha) {
		PylosPlayerColor color = simulator.getColor();
		PylosGameState state = simulator.getState();

		boolean prune = false;
		boolean extraPrune = false;


		Action localBestAction = null;
		int MiniMaxEval = (color == PLAYER_COLOR) ? -999 : +999;

		//first look for the spheres on the board, and start with them -> will first try to move a sphere up, and only then add other sphere
		PylosSphere[] spheres = simulatorBoard.getSpheres(color);
		Arrays.sort(spheres, (s1, s2) -> {
			if (s1.getLocation() == null && s2.getLocation() != null) return 1;
			if (s1.getLocation() != null && s2.getLocation() == null) return -1;
			return 0;
		});

		//error nog zoeken,  waarom wordt sphere null?
		PylosLocation[] locations = simulatorBoard.getLocations();

		//shuffle locations so player does not always start with same move
		ArrayList<PylosLocation> locationsList = new ArrayList(Arrays.asList(locations));
		Collections.shuffle(locationsList, this.getRandom());
		locations = new PylosLocation[locations.length];
		for (int i = 0; i < locations.length; i++) {
			locations[i] = locationsList.get(i);
		}

		for (int i = 0; i < spheres.length && !prune; i++) {
			PylosSphere sphere = spheres[i];
			for (PylosLocation location : locations) {
				if (sphere.canMoveTo(location) && !extraPrune) {
					PylosLocation prevLocation = sphere.getLocation();
					simulator.moveSphere(sphere, location);
					int eval = minimax(depth + 1, MiniMaxEval, alpha);
					if (color == PLAYER_COLOR) {
						if (eval > MiniMaxEval) {
							MiniMaxEval = eval;
							localBestAction = new Action(location, sphere);
						}

						if (MiniMaxEval >= alpha) {
							prune = true;
							if (depth > 3) extraPrune = true;	//prune on locations also -> much faster for larger depth
						}
					} else {
						if (eval < MiniMaxEval) {
							MiniMaxEval = eval;
							localBestAction = new Action(location, sphere);
						}
						if (MiniMaxEval <= alpha) {
							prune = true;
							if (depth >3) extraPrune = true;
						}
					}
					if (prevLocation != null) {
						simulator.undoMoveSphere(sphere, prevLocation, PylosGameState.MOVE, color);
					} else {
						simulator.undoAddSphere(sphere, PylosGameState.MOVE, color);
					}
				}
			}

		}

		bestAction = localBestAction;
		return MiniMaxEval;
	}

	public int minimaxRemoveFirst(int depth, int beta) {
		PylosPlayerColor color = simulator.getColor();
		PylosGameState state = simulator.getState();
		boolean prune = false;

		Action localBestAction = null;
		int MiniMaxEval = (color == PLAYER_COLOR) ? -999 : +999;

		PylosSphere[] spheres = simulatorBoard.getSpheres(color);

		for (int i = 0; i < spheres.length && !prune; i++) {
			PylosSphere sphere = spheres[i];
			if (sphere.canRemove()) {
				PylosLocation prevLocation = sphere.getLocation();
				simulator.removeSphere(sphere);
				int eval = minimax(depth + 1, beta, beta);
				if (color == PLAYER_COLOR) {
					if (eval > MiniMaxEval) {
						MiniMaxEval = eval;
						localBestAction = new Action(null, sphere);
					}
					if (MiniMaxEval >= beta) prune = true;
				} else {
					if (eval < MiniMaxEval) {
						MiniMaxEval = eval;
						localBestAction = new Action(null, sphere);
					}
					if (MiniMaxEval <= beta) prune = true;
				}
				 if(simulator.getState() == PylosGameState.REMOVE_SECOND) simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.REMOVE_FIRST, color);
				 else simulator.undoRemoveSecondSphere(sphere, prevLocation, PylosGameState.REMOVE_SECOND, color);
			}
		}
		bestAction = localBestAction;
		return MiniMaxEval;
	}

	public int minimaxRemoveSecond(int depth, int beta) {
		PylosPlayerColor color = simulator.getColor();
		int MiniMaxEval = (color == PLAYER_COLOR) ? -999 : +999;
		MiniMaxEval = minimaxRemoveFirst(depth, MiniMaxEval);
		PylosGameState state = simulator.getState();

		if (depth == MAX_DEPTH || state == PylosGameState.COMPLETED)
			return evaluationFunction(state, color);


		simulator.pass();
		//int eval = evaluationFunction(state, color); -> mag niet met deze functie want dan krijgt pass sowieso voordeel (omdat we in state remove second zitten)
		int eval = this.simulatorBoard.getReservesSize(PLAYER_COLOR) - this.simulatorBoard.getReservesSize(PLAYER_COLOR.other());
		if (color == PLAYER_COLOR) {
			if (eval > MiniMaxEval) {
				MiniMaxEval = eval;
				bestAction = null;
			}
		} else {
			if (eval < MiniMaxEval) {
				MiniMaxEval = eval;
				bestAction = null;
			}
		}
		simulator.undoPass(PylosGameState.REMOVE_SECOND, color);

		return MiniMaxEval;
	}
}