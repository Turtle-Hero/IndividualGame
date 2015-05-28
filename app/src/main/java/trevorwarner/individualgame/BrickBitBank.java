package trevorwarner.individualgame;

import android.content.SharedPreferences;

/**
 * BrickBitBank class that manages the amount of Brick Bits
 * the player has.
 */
public class BrickBitBank {

    private static BrickBitBank mainBank = null;

    private SharedPreferences sharedPref;

    public static BrickBitBank getMainBrickBitBank(SharedPreferences prefs) {
        if (mainBank == null) {
            mainBank = new BrickBitBank(prefs);
        }
        return mainBank;
    }


    private BrickBitBank(SharedPreferences prefs) {
        this.sharedPref = prefs;
    }

    /**
     * returns the amount of brick bits.
     * @return long brickBits
     */
    public long getBrickBits() {
        return sharedPref.getLong("BrickBitTotal", 0);
    }

    /**
     * Increases the amount of brick bits in the bank.
     * @param amountToIncrease
     */
    public void increaseBrickBits(long amountToIncrease) {
        setBrickBits(getBrickBits() + amountToIncrease);
    }

    /**
     * Decreases the amount of brick bits in the bank.
     * @param amountToDecrease
     * @throws InsufficientBrickBitsException
     */
    public void decreaseBrickBits(long amountToDecrease) throws InsufficientBrickBitsException {
        if(getBrickBits() - amountToDecrease < 0) {
            throw new InsufficientBrickBitsException();
        } else {
            setBrickBits(getBrickBits() - amountToDecrease);
        }
    }

    /*
     * resets the amount brick bits
     */
    public void resetBrickBits() { setBrickBits(0);}

    /*
     * Updates the shared prefrences file to save the
     * amount of brick bits.
     * @param newValue
     */
    private void setBrickBits(long newValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("BrickBitTotal", newValue);
        editor.commit();
    }

    //creates exception when user doesn't have enough brickBits
    public class InsufficientBrickBitsException extends Exception {

    }
}
