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

/**
 * Created by Jan on 20/02/2015.
 */

//class BoardState implements Serializable {
//	private long board;
//	private PylosGameState state;
//	private PylosPlayerColor color;
//
//	//private int eval;
//
//	public BoardState(long board, PylosGameState gameState, PylosPlayerColor color) {
//		this.board = board;
//		this.state = gameState;
//		this.color = color;
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//		BoardState that = (BoardState) o;
//		return Objects.equals(board, that.board) && state == that.state && color == that.color;
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(board, state, color);
//	}
//
//	public boolean isBoardState(long board, PylosGameState gameState, PylosPlayerColor color) {
//		if (this.board == board && this.state == gameState && this.color == color ) return true;
//		else return false;
//	}
//
//	@Override
//	public String toString() {
//		return "BoardState{" +
//				"board=" + board +
//				", state=" + state +
//				", color=" + color +
//				'}';
//	}
//}

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
		/* board methods
		 * 	PylosLocation[] allLocations = board.getLocations();
		 * 	PylosSphere[] allSpheres = board.getSpheres();
		 * 	PylosSphere[] mySpheres = board.getSpheres(this);
		 * 	PylosSphere myReserveSphere = board.getReserve(this); */

		/* game methods
		 * game.moveSphere(myReserveSphere, allLocations[0]); */
		if (DEBUG) System.out.println("student player color:" + PLAYER_COLOR);
		if (DEBUG) System.out.println("start simulator init");
		init(game, board);

		if (DEBUG) System.out.println("start minimax alogrithm with depth " + MAX_DEPTH);
		int eval = minimax(0, -999, +999);

		if (DEBUG) System.out.println("evaluation: " + eval);
		if (DEBUG) System.out.println("game state: " + game.getState().toString());
		if (DEBUG) System.out.println("best action: " + bestAction.toString());
		if (DEBUG) System.out.println();
		if (bestAction == null) System.err.println("de bestaction is nul");
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
		//System.out.println("max depth is reached, value is " + eval);
		//werken naar een square toe is meer waard
		if (state == PylosGameState.REMOVE_FIRST || state == PylosGameState.REMOVE_SECOND)
			eval = (color == PLAYER_COLOR) ? eval + 2 : eval - 2;

//		//neem hoogte ballen mee in eval -> hoe hoger ballen, hoe beter => maakt hem blijkbaar slechter
		PylosSphere[] mySpheres = this.simulatorBoard.getSpheres(PLAYER_COLOR);
		int playerSum = 0;
		for (int i = 0; i < mySpheres.length && !mySpheres[i].isReserve(); i++) {
			playerSum += (int) mySpheres[i].getLocation().Z / 30;        //delen door 30 omdat anders het voordeliger is om twee ballen weg te nemen van boven, wat zorgt voor meer reserveballen
		}

		PylosSphere[] otherSpheres = this.simulatorBoard.getSpheres(PLAYER_COLOR.other());
		int otherSum = 0;
		for (int i = 0; i < otherSpheres.length && !otherSpheres[i].isReserve(); i++) {
			otherSum -= (int) otherSpheres[i].getLocation().Z / 30;
		}

		eval = eval + playerSum - otherSum;

		return eval;
	}

	public int minimax(int depth, int alpha, int beta) {
		PylosGameState state = simulator.getState();
		PylosPlayerColor color = simulator.getColor();

		if (depth == MAX_DEPTH || state == PylosGameState.COMPLETED) {
			return evaluationFunction(state, color);
		}

		int MiniMaxEval;

		if (state == PylosGameState.MOVE) {
			MiniMaxEval = minimaxMove(depth, alpha);
		} else if (state == PylosGameState.REMOVE_FIRST) {
			MiniMaxEval = minimaxRemoveFirst(depth, beta);
		} else if (state == PylosGameState.REMOVE_SECOND) {
			MiniMaxEval = minimaxRemoveSecond(depth, beta);
		} else {
			MiniMaxEval = simulator.getWinner() == PLAYER_COLOR ? 2000 : -2000;
			if (simulator.getWinner() == PLAYER_COLOR) {
				MiniMaxEval -= depth;
			} else {
				MiniMaxEval += depth;
			}
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
//		PylosSphere reserve = simulatorBoard.getReserve(color);
//		PylosSphere[] allSpheres = simulatorBoard.getSpheres(color);
		PylosLocation[] locations = simulatorBoard.getLocations();
//
//		int size;
//		if (reserve != null) { size = 15 - simulatorBoard.getReservesSize(color) + 1; }
//		else { size = 15 - simulatorBoard.getReservesSize(color); }

		/* shuffle */
		ArrayList<PylosLocation> locationsList = new ArrayList(Arrays.asList(locations));
		Collections.shuffle(locationsList, getRandom());
		locations = new PylosLocation[locations.length];
		for (int i = 0; i < locations.length; i++) {
			locations[i] = locationsList.get(i);
		}
//		ArrayList<PylosSphere> sphereList = new ArrayList<>(Arrays.asList(allSpheres));
//		Collections.shuffle(sphereList, getRandom());
//		PylosSphere[] spheres = new PylosSphere[size];
//		for (int i = 0; i < size-1; i++) {
//			PylosSphere ps = sphereList.get(i);
//			if (!(ps.getLocation() != null)) {
//				spheres[i] = ps;
//			}
//		}
//		//add reserve sphere
//		if (reserve != null ) spheres[spheres.length-1] = reserve;

		for (int i = 0; i < spheres.length && !prune; i++) {
			PylosSphere sphere = spheres[i];
			for (PylosLocation location : simulatorBoard.getLocations()) {
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
							if (depth > 0) extraPrune = true;	//prune on locations also -> much faster for larger depth
						}
					} else {
						if (eval < MiniMaxEval) {
							MiniMaxEval = eval;
							localBestAction = new Action(location, sphere);
						}
						if (MiniMaxEval <= alpha) {
							prune = true;
							if (depth > 0) extraPrune = true;
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
				simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.REMOVE_FIRST, color);
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


//	public int minimax(int depth, int alpha, int beta){
//		PylosGameState state = simulator.getState();
//		PylosPlayerColor color = simulator.getColor();
//
//		boolean prune = false;
//		//System.out.println("State: " + simulator.getState() + " Color: " + color + " depth: " + depth);
//
//		if(depth == MAX_DEPTH || state == PylosGameState.COMPLETED)
//			return evaluationFunction(state, color);
//
//		Action localBestAction = null;
//		int MiniMaxEval = (color == PLAYER_COLOR)? -999 : +999;
//
//		//first look for the spheres on the board, and start with them
//		PylosSphere[] spheres = simulatorBoard.getSpheres(color);
//		Arrays.sort(spheres, (s1, s2) ->{if(s1.getLocation() == null && s2.getLocation() != null) return 1;
//			if(s1.getLocation() != null && s2.getLocation() == null) return -1; return 0;});
//
//		//for (PylosSphere sphere : simulatorBoard.getSpheres(color)) {
//		for (PylosSphere sphere : spheres) {
//			if (!prune) {
//				PylosLocation prevLocation = sphere.getLocation();
//				if (state == PylosGameState.MOVE) {
//					//TODO bezette locations eerst nog wegfilteren
//					for (PylosLocation location : simulatorBoard.getLocations()) {
//						if (sphere.canMoveTo(location) && !prune) {
//							simulator.moveSphere(sphere, location);
////						final Long currentState = addGameState(simulatorBoard.toLong(), state, color);
////						Integer eval = prevResults.get(currentState);
////						if (eval == null) {
////							eval = minimax(depth + 1, alpha, beta);
////						}
//
//							int eval = minimax(depth + 1, alpha, beta);
//
////						BoardState currentBoardState = new BoardState(simulatorBoard.toLong(), state, color);
////						int eval;
////						if (prevResults.containsKey(currentBoardState.toString())) eval = prevResults.get(currentBoardState.toString());
////						else eval = minimax(depth+1, alpha, beta);
//
//							if (color == PLAYER_COLOR) {
//								alpha = Math.max(alpha, eval);
//								if (MiniMaxEval <= eval) {
//									MiniMaxEval = eval;
//									localBestAction = new Action(location, sphere);
//								}
//								if (MiniMaxEval > alpha) prune = true;
//							} else {
//								beta = Math.min(beta, eval);
//								if (MiniMaxEval >= eval) {
//									MiniMaxEval = eval;
//									localBestAction = new Action(location, sphere);
//								}
//								if (MiniMaxEval < beta) prune = true;
//							}
//
////						if ((color == PLAYER_COLOR && MiniMaxEval <= eval) || (color != PLAYER_COLOR && MiniMaxEval >= eval)) {
////							MiniMaxEval = eval;
////							localBestAction = new Action(location, sphere);
////						}
////
////						if (color == PLAYER_COLOR)
////							alpha = Math.max(alpha, eval);
////						else
////							beta = Math.min(beta, eval);
//
//							if (prevLocation != null) {
//								simulator.undoMoveSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//							} else {
//								simulator.undoAddSphere(sphere, PylosGameState.MOVE, color);
//							}
//
//							//if (beta <= alpha) break;
//						}
//
//					}
//				} else if ((state == PylosGameState.REMOVE_FIRST || state == PylosGameState.REMOVE_SECOND) && sphere.canRemove()) {
//					if (!prune) {
//						simulator.removeSphere(sphere);
////				final Long currentState = addGameState(simulatorBoard.toLong(), state, color);
////				Integer eval = prevResults.get(currentState);
////				if (eval == null) {
////					eval = minimax(depth + 1, alpha, beta);
////				}
//
//						int eval = minimax(depth + 1, alpha, beta);
//
////				BoardState currentBoardState = new BoardState(simulatorBoard.toLong(), state, color);
////				int eval;
////				if (prevResults.containsKey(currentBoardState.toString())) eval = prevResults.get(currentBoardState.toString());
////				else eval = minimax(depth+1, alpha, beta);
//
//						if (color == PLAYER_COLOR) {
//							alpha = Math.max(alpha, eval);
//							if (MiniMaxEval <= eval) {
//								localBestAction = new Action(null, sphere);
//								MiniMaxEval = eval;
//							}
//							if (MiniMaxEval > alpha) prune = true;
//
//						} else {
//							beta = Math.min(beta, eval);
//							if (MiniMaxEval >= eval) {
//								localBestAction = new Action(null, sphere);
//								MiniMaxEval = eval;
//							}
//							if (MiniMaxEval < beta) prune = true;
//						}
//
//
////				if ((color == PLAYER_COLOR && MiniMaxEval <= eval) || (color != PLAYER_COLOR && MiniMaxEval >= eval)) {
////					MiniMaxEval = eval;
////					localBestAction = new Action(null, sphere);
////				}
////
////				if (color == PLAYER_COLOR)
////					alpha = Math.max(alpha, eval);
////				else
////					beta = Math.min(beta, eval);
//
//						if (state == PylosGameState.REMOVE_FIRST)
//							simulator.undoRemoveFirstSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//						else
//							simulator.undoRemoveSecondSphere(sphere, prevLocation, PylosGameState.MOVE, color);
//
//						//if (beta <= alpha) break;
//					}
//				}
//			}
//		}
//
//		bestAction = localBestAction;
////		if (bestAction == null) {
////			System.out.println(simulator.getColor());
////			System.out.println(simulator.getState());
////			System.out.println(simulatorBoard.getReservesSize(simulator.getColor()));
////			System.out.println(simulatorBoard.getNumberOfSpheresOnBoard());
////		}
//
////		final Long currentState = addGameState(simulatorBoard.toLong(), state, color);
////		Integer eval = prevResults.get(currentState);
////		if (eval == null) {
////			prevResults.put(currentState, MiniMaxEval);
////		}
//
////		BoardState currentBoardState = new BoardState(simulatorBoard.toLong(), state, color);
////		if (!prevResults.containsKey(currentBoardState.toString())) {
////			prevResults.put(currentBoardState.toString(), MiniMaxEval);
////		}
//
//		return MiniMaxEval;
//	}


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
}
