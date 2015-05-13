package trevorwarner.individualgame;

import android.content.SharedPreferences;

/**
 * BrickBitBank
 */
public class BrickBitBank {

    private static BrickBitBank mainBank = null;

    public static BrickBitBank getMainBrickBitBank(SharedPreferences prefs) {
        if (mainBank == null) {
            mainBank = new BrickBitBank(prefs);
        }
        return mainBank;
    }

    private SharedPreferences sharedPref;

    private BrickBitBank(SharedPreferences prefs) {
        this.sharedPref = prefs;
    }

    public long getBrickBits() {
        return sharedPref.getLong("BrickBitTotal", 0);
    }

    public void increaseBrickBits(long amountToIncrease) {
        setBrickBits(getBrickBits() + amountToIncrease);
    }

    public void decreaseBrickBits(long amountToDecrease) throws InsufficientBrickBitsException {
        if(getBrickBits() - amountToDecrease < 0) {
            throw new InsufficientBrickBitsException();
        } else {
            setBrickBits(getBrickBits() - amountToDecrease);
        }
    }

    public void resetBrickBits() { setBrickBits(0);}

    private void setBrickBits(long newValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("BrickBitTotal", newValue);
        editor.commit();
    }

    public class InsufficientBrickBitsException extends Exception {

    }
}
