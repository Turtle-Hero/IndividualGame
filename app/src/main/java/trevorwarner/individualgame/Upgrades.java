package trevorwarner.individualgame;

import android.util.Log;

/**
 * Created by the_guz on 4/19/15.
 * Class that will house all the methods that are called
 * when an upgrade is bought/from save state.
 */
public class Upgrades {

    public double clickPower (double oldPower){
        //double newPower = oldPower + (oldPower * .1););
        double newPower = oldPower * 2;
        return newPower;
    }



}
