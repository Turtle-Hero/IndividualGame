package trevorwarner.individualgame;

import android.content.SharedPreferences;

/**
 * Created by the_guz on 4/19/15.
 * Class that will house all the methods that are called
 * when an upgrade is bought/called from save state.
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
    //initializes all upgrade values to variables from shared pref
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

    /**
     * Sets the correct amount of damage to be dealt to
     * the brick by the bomb ability and returns that
     * value.
     * @param currentHealth
     * @return total 'hits' the bomb will do to the brick
     */
    public int bombPower(int currentHealth) {
        int bombDmg = (currentHealth / 3);
        return bombDmg;
    }

    /**
     * Returns the clickPower.
     * @return Int # of click powers purchased
     */
    public int getClickPower(){return clickCount ;}

    /*
     * @return the amount of swipe upgrades bought.
     */
    public int getSwipeUpgrade() {return swipeCount;}

    /**
     * Sets the swipe power depending on amount
     * of swipe upgrades bought.
     * @return Int power of each swipe
     */
    private int setSwipePower(){
        swipePower = (swipeCount * 2) + 1;
        return swipePower;
    }

    /**
     * returns the current swipe power.
     * @return int power of each swipe
     */
    public int getSwipePower(){return swipePower;}

    /**
     * returns the amount of bombs bought
     * @return Int # of bomb upgrades purchased
     */
    public int getBombUpgrade(){return bombPower;}

    /**
     * returns the amount of nukes bought
     * @return Int # of nuke upgrades purchased
     */
    public int getNukeUpgrade(){return nukePower;}

    /**
     * returns the amount of timer upgrades
     * @return Int # of timer upgrades purchased
     */
    public int getTimerPower(){return timerPower;}

    /**
     * returns the amount of money upgrades
     * @return Int # of money upgrades purchased
     */
    public int getMoneyUpgrade(){return moneyCount;}

    /**
     * sets the money multiplier used to calculate
     * brick bits.
     * @return Double value of what the moneyPower multiplier is
     */
    private double setMoneyPower(){
        moneyPower = (moneyCount * 0.1) + 1;
        return moneyPower;
    }

    /**
     * returns the money multiplier
     * @return Double value of what the moneyPower multiplier is
     */
    public double getMoneyPower(){return moneyPower;}



}
