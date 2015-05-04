package trevorwarner.individualgame;

import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

/**
 * BrickBitBank
 */
public class BrickBit {

    private static BrickBit mainBank = null;

    public static BrickBit getMainBrickBitBank(SharedPreferences prefs) {
        if (mainBank == null) {
            mainBank = new BrickBit(prefs);
        }
        return mainBank;
    }

    private SharedPreferences sharedPref;

    private BrickBit(SharedPreferences prefs) {
        this.sharedPref = prefs;
    }

    public int getBrickBits() {
        return sharedPref.getInt("BrickBitTotal", 0);
    }

    public void increaseBrickBits(int amountToIncrease) {
        setBrickBits(getBrickBits() + amountToIncrease);
    }

    public void decreaseBrickBits(int amountToDecrease) throws InsufficientBrickBitsException {
        if(getBrickBits() - amountToDecrease < 0) {
            throw new InsufficientBrickBitsException();
        } else {
            setBrickBits(getBrickBits() - amountToDecrease);
        }
    }

    public void resetBrickBits() { setBrickBits(0);}

    private void setBrickBits(int newValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("BrickBitTotal", newValue);
        editor.commit();
    }

    private class InsufficientBrickBitsException extends Exception {

    }
}
