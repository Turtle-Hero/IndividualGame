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
     * @return
     */
    public int bombPower(int currentHealth) {
        int bombDmg = (currentHealth / 3);
        return bombDmg;
    }

    /**
     * Returns the clickPower.
     * @return
     */
    public int getClickPower(){return clickCount ;}

    /**
     * Returns the amount of swipe upgrades bought.
     * @return
     */
    public int getSwipeUpgrade() {return swipeCount;}

    /**
     * Sets the swipe power depending on amount
     * of swipe upgrades bought.
     * @return
     */
    private int setSwipePower(){
        swipePower = (swipeCount * 2) + 1;
        return swipePower;
    }

    /**
     * returns the current swipe power.
     * @return
     */
    public int getSwipePower(){return swipePower;}

    /**
     * returns the amount of bombs bought
     * @return
     */
    public int getBombUpgrade(){return bombPower;}

    /**
     * returns the amount of nukes bought
     * @return
     */
    public int getNukeUpgrade(){return nukePower;}

    /**
     * returns the amount of timer upgrades
     * @return
     */
    public int getTimerPower(){return timerPower;}

    /**
     * returns the amount of money upgrades
     * @return
     */
    public int getMoneyUpgrade(){return moneyCount;}

    /**
     * sets the money multiplier used to calculate
     * brick bits.
     * @return
     */
    private double setMoneyPower(){
        moneyPower = (moneyCount * 0.1) + 1;
        return moneyPower;
    }

    /**
     * returns the money multiplier
     * @return
     */
    public double getMoneyPower(){return moneyPower;}



}
