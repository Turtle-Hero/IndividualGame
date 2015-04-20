package trevorwarner.individualgame;

/**
 * Created by the_guz on 4/19/15.
 * Class that will house all the methods that are called
 * when an upgrade is bought/from save state.
 */
public class Upgrades {

    private double clickPower (int oldPower){
        double newPower = oldPower + (oldPower * .1);
        return newPower;
    }



}
