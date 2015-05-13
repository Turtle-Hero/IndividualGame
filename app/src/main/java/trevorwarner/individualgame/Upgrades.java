package trevorwarner.individualgame;

import android.content.SharedPreferences;

/**
 * Created by the_guz on 4/19/15.
 * Class that will house all the methods that are called
 * when an upgrade is bought/from save state.
 */

public class Upgrades {

    private int clickCount;
    private int swipeCount;
    private int swipePower;
    private int nukePower;
    private int bombPower;
    private int timerPower;
    private int moneyCount;
    private double moneyPower;

    //Upgrade constructor
    public Upgrades (SharedPreferences prefs){
        this.clickCount = prefs.getInt("clickPowerCount", 1);
        this.swipeCount = prefs.getInt("swipeCount", 0);
        this.swipePower = setSwipePower();
        this.bombPower = prefs.getInt("bombCount", 0);
        this.nukePower = prefs.getInt("nukeCount", 0);
        this.timerPower = prefs.getInt("timerCount", 0);
        this.moneyCount = prefs.getInt("moneyCount", 0);
        this.moneyPower =  setMoneyPower();
    }

    public int bombPower(int currentHealth) {
        int bombDmg = (currentHealth / 3);
        return bombDmg;
    }

    public int getClickPower(){return clickCount ;}

    public int getSwipeUpgrade() {return swipeCount;}

    private int setSwipePower(){
        swipePower = (swipeCount * 2) + 1;
        return swipePower;
    }

    public int getSwipePower(){return swipePower;}

    public int getBombUpgrade(){return bombPower;}

    public int getNukeUpgrade(){return nukePower;}

    public int getTimerPower(){return timerPower;}

    public int getMoneyUpgrade(){return moneyCount;}

    private double setMoneyPower(){
        moneyPower = (moneyCount * 0.1) + 1;
        return moneyPower;
    }

    public double getMoneyPower(){return moneyPower;}



}
