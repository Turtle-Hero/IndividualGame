package trevorwarner.individualgame;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by the_guz on 4/19/15.
 * Class that will house all the methods that are called
 * when an upgrade is bought/from save state.
 */
public class Upgrades {

   private double newPower;
   private boolean swipingEnabled;

    //constructor initially sets the newPower to the old Power. (meaning the user has no upgrades)
    //Each upgrade then changes the newPower
    public Upgrades (double oldPower){
        newPower = oldPower;
        swipingEnabled = false;
    }

    public double clickPower (double oldPower){
        //double newPower = oldPower + (oldPower * .1););
        newPower = oldPower * 2;
        return newPower;
    }

    // first swipe upgrade. When the user swipes, it does three times as much damage as a tap
    public double swipe (double oldPower){
        swipingEnabled = true;
        newPower = oldPower * 3;
        return newPower;
    }

    public boolean swipingIsEnabled() {
        return swipingEnabled;
    }
}
