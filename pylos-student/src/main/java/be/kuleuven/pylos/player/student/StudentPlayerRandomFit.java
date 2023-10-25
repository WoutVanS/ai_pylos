package be.kuleuven.pylos.player.student;

import be.kuleuven.pylos.game.PylosBoard;
import be.kuleuven.pylos.game.PylosGameIF;
import be.kuleuven.pylos.game.PylosLocation;
import be.kuleuven.pylos.game.PylosSphere;
import be.kuleuven.pylos.player.PylosPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Ine on 5/05/2015.
 */
public class StudentPlayerRandomFit extends PylosPlayer{

    @Override
    public void doMove(PylosGameIF game, PylosBoard board) {
        PylosLocation[] allLocations = board.getLocations();
        List<PylosLocation> usableLocations = new ArrayList<>();
        for (PylosLocation pylosLocation: allLocations){
            if(pylosLocation.isUsable())
                usableLocations.add(pylosLocation);
        }

        PylosLocation move = usableLocations.get(new Random().nextInt(usableLocations.size()));
        game.moveSphere(board.getReserve(this), move);
    }

    @Override
    public void doRemove(PylosGameIF game, PylosBoard board) {
        PylosSphere[] mySpheres = board.getSpheres(this);
        List<PylosSphere> removableSpheres = new ArrayList<>();
            for(PylosSphere sphere : mySpheres){
                if(sphere.canRemove() && !sphere.isReserve())
                    removableSpheres.add(sphere);
            }
        PylosSphere remove = removableSpheres.get(new Random().nextInt(removableSpheres.size()));
        game.removeSphere(remove);
        }

    @Override
    public void doRemoveOrPass(PylosGameIF game, PylosBoard board) {
        if(new Random().nextInt(2) == 0)
            doRemove(game, board);
        else
            game.pass();
    }
}
