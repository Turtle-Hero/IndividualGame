package trevorwarner.individualgame;

import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by the_guz on 4/19/15.
 * Class that will house all the methods that are called
 * when an upgrade is bought/from save state.
 */
public class Upgrades {

   private int clickPower;
   private int swipePower;
   private boolean swipingEnabled;

    //constructor initially sets the newPower to the old Power. (meaning the user has no upgrades)
    //Each upgrade then changes the newPower
    public Upgrades (){
        clickPower = 1;
        swipePower= 3;
        swipingEnabled = false;
    }

    public int setClickPower (){
        //double newPower = oldPower + (oldPower * .1););
        clickPower= clickPower * 2;
        return clickPower;
    }

    // first swipe upgrade. When the user swipes, it does three times as much damage as a tap
    public void setSwipePower (){
        swipingEnabled = true;
        swipePower = swipePower+3;
    }

    public int getClickPower(){return clickPower;}

    public int getSwipePower(){return swipePower;}

    public boolean swipingIsEnabled() {
        return swipingEnabled;
    }
}
