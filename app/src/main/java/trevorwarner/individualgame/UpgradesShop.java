package trevorwarner.individualgame;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * creates the view for the upgrades shop and
 * maintains the button listeners
 * Created by the_guz on 4/24/15.
 */

/*
All upgrade Icons were taken from ClipartPanda.com
 */
//cash register sound from freesound.org/people/kiddpark/sounds/201159/, from user kiddpark
public class UpgradesShop extends ActionBarActivity {

    String changedItem;

    BrickBitBank brickBitsBank;
    TextView brickBitView;

    ImageButton clickPowerButton, swipeUpgradeButton, bombButton, nukeButton, timerButton, moneyButton;

    int clickPowerCount = 1;
    int swipeCount = 0;
    int bombCount = 0;
    int nukeCount = 0;
    int timerCount = 0;
    int moneyCount = 0;
    SharedPreferences upgradePref;

    TextView clickPrice, swipePrice, bombPrice, timerPrice, nukePrice, moneyPrice;
    TextView clickCountText, swipeCountText, bombCountText, timerCountText, nukeCountText, moneyCountText;

    private int soundID;
    private SoundPool buttonHitSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences brickBitPrefs = getApplicationContext().getSharedPreferences("brickBitPref", MODE_PRIVATE);
        upgradePref = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
        brickBitsBank = BrickBitBank.getMainBrickBitBank(brickBitPrefs);

        setContentView(R.layout.activity_upgrades_menu);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //initializes all billion textviews
        clickPrice = (TextView) findViewById(R.id.clickPrice);
        swipePrice = (TextView) findViewById(R.id.swipePrice);
        bombPrice = (TextView) findViewById(R.id.bombPrice);
        timerPrice = (TextView) findViewById(R.id.timerPrice);
        nukePrice = (TextView) findViewById(R.id.nukePrice);
        moneyPrice = (TextView) findViewById(R.id.moneyPrice);
        clickCountText = (TextView) findViewById(R.id.clickCountText);
        swipeCountText = (TextView) findViewById(R.id.swipeCountText);
        bombCountText = (TextView) findViewById(R.id.bombCountText);
        nukeCountText = (TextView) findViewById(R.id.nukeCountText);
        timerCountText = (TextView) findViewById(R.id.timerCountText);
        moneyCountText = (TextView) findViewById(R.id.moneyCountText);

        //sets upgrade variables to their save state
        clickPowerCount = upgradePref.getInt("clickPowerCount", 1);
        swipeCount = upgradePref.getInt("swipeCount", 0);
        bombCount = upgradePref.getInt("bombCount", 0);
        timerCount = upgradePref.getInt("timerCount", 0);
        nukeCount = upgradePref.getInt("nukeCount", 0);
        moneyCount = upgradePref.getInt("moneyCount", 0);

        //sets price of each upgrade
        setPrices();

        buttonHitSound = new SoundPool(10, AudioManager.STREAM_MUSIC,1);
        soundID = buttonHitSound.load(this, R.raw.cash_register, 2);

        brickBitView = (TextView) findViewById(R.id.brickBitView);
        brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());

        clickPowerButton = (ImageButton) findViewById(R.id.clickUpgrade);
        clickPowerButton.setOnClickListener(clickPowerListener);

        swipeUpgradeButton = (ImageButton) findViewById(R.id.swipeUpgrade);
        swipeUpgradeButton.setOnClickListener(swipeUpgradeListener);

        bombButton = (ImageButton) findViewById(R.id.bombButton);
        bombButton.setOnClickListener(bombButtonListener);

        timerButton = (ImageButton) findViewById(R.id.timerButton);
        timerButton.setOnClickListener(timerButtonListener);

        nukeButton = (ImageButton) findViewById(R.id.nukeButton);
        nukeButton.setOnClickListener(nukeButtonListener);

        moneyButton = (ImageButton) findViewById(R.id.moneyButton);
        moneyButton.setOnClickListener(moneyButtonListener);

    }

    /**
     * -All the OnClickListeners checks to make sure the player
     * has sufficient brickBits and deducts the amount
     * of the upgrade cost if the player has enough birck bits.
     * -Then they call the saveChanges, to save the state of the upgrade
     * -play the cash register sound
     * -It then increases the price and the amount of the upgrade
     * the player has
     * -also increases how many of those upgrades have been purchased
     * -finally it also updates how many brickbits the user has
     */

    View.OnClickListener clickPowerListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            try {
                brickBitsBank.decreaseBrickBits(increasePrice(100, clickPowerCount - 1));
                clickPowerCount++;
                changedItem = "clickPowerCount";
                saveChanges(changedItem, clickPowerCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                clickPrice.setText("$" + increasePrice(100, clickPowerCount - 1));
                clickCountText.setText("x" + (upgradePref.getInt("clickPowerCount", 1) - 1));
            } catch (BrickBitBank.InsufficientBrickBitsException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Insufficient BrickBits", Toast.LENGTH_SHORT);
                toast.show();
            }
            brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());
        }
    };

    View.OnClickListener swipeUpgradeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                brickBitsBank.decreaseBrickBits(increasePrice(300, swipeCount));
                swipeCount++;
                changedItem = "swipeCount";
                saveChanges(changedItem, swipeCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                swipePrice.setText("$" + increasePrice(300, swipeCount));
                swipeCountText.setText("x" + (upgradePref.getInt("swipeCount", 0)));
            } catch (BrickBitBank.InsufficientBrickBitsException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Insufficient BrickBits", Toast.LENGTH_SHORT);
                toast.show();
            }
            brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());
        }
    };

    View.OnClickListener bombButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                brickBitsBank.decreaseBrickBits(increasePrice(75, bombCount));
                bombCount++;
                changedItem = "bombCount";
                saveChanges(changedItem, bombCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
               bombPrice.setText("$" + increasePrice(75, bombCount));
                bombCountText.setText("x" + (upgradePref.getInt("bombCount", 0)));
            } catch (BrickBitBank.InsufficientBrickBitsException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Insufficient BrickBits", Toast.LENGTH_SHORT);
                toast.show();
            }
            brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());
        }
    };

    View.OnClickListener timerButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                brickBitsBank.decreaseBrickBits(increasePrice(150, timerCount));
                timerCount++;
                changedItem="timerCount";
                saveChanges(changedItem, timerCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                timerPrice.setText("$" + increasePrice(150, timerCount));
                timerCountText.setText("x" + (upgradePref.getInt("timerCount", 0)));
            }catch (BrickBitBank.InsufficientBrickBitsException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Insufficient BrickBits", Toast.LENGTH_SHORT);
                toast.show();
            }
            brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());
        }
    };

    View.OnClickListener nukeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                brickBitsBank.decreaseBrickBits(increasePrice(250, nukeCount));
                nukeCount++;
                changedItem = "nukeCount";
                saveChanges(changedItem, nukeCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                nukePrice.setText("$" + increasePrice(250, nukeCount));
                nukeCountText.setText("x" + (upgradePref.getInt("nukeCount", 0)));
            } catch (BrickBitBank.InsufficientBrickBitsException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Insufficient BrickBits", Toast.LENGTH_SHORT);
                toast.show();
            }
            brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());
        }
    };

    View.OnClickListener moneyButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                brickBitsBank.decreaseBrickBits(increasePrice(400, moneyCount));
                moneyCount++;
                changedItem="moneyCount";
                saveChanges(changedItem, moneyCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                moneyPrice.setText("$" + increasePrice(400, moneyCount));
                moneyCountText.setText("x" + (upgradePref.getInt("moneyCount", 0)));
            } catch (BrickBitBank.InsufficientBrickBitsException e){
                Toast toast = Toast.makeText(getApplicationContext(), "Insufficient BrickBits", Toast.LENGTH_SHORT);
                toast.show();
            }
            brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());
        }
    };


    /**
     * Saves the changes that the On Click Listener made to the
     * saved prefrences file.
     * @param item
     * @param change
     */
    public void saveChanges(String item, int change) {
        //commits changes to shared preferences
        SharedPreferences.Editor editor = upgradePref.edit();
        editor.putInt(item, change);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
        return true;
    }

    /**
     * sets the price and amounts of each upgrade in their respective text views
     */
    public void setPrices() {
        clickCountText.setText("x" + (clickPowerCount - 1));
        swipeCountText.setText("x" + swipeCount);
        bombCountText.setText("x" + bombCount);
        timerCountText.setText("x" + timerCount);
        nukeCountText.setText("x" + nukeCount);
        moneyCountText.setText("x" + moneyCount);

        clickPrice.setText("$" + increasePrice(100, clickPowerCount - 1));
        swipePrice.setText("$" + increasePrice(300, swipeCount));
        bombPrice.setText("$" + increasePrice(75, bombCount));
        timerPrice.setText("$" + increasePrice(150, timerCount));
        nukePrice.setText("$" + increasePrice(250, nukeCount));
        moneyPrice.setText("$" + increasePrice(400, moneyCount));
    }

    /**
     * Method that increases the price of the upgrades when
     * an upgrade has been bought.
     * @param oldPrice is price that upgrade was just bought for
     * @param upgradeAmount is how many of that upgrade was purchased
     * @return Int of new price for that upgrade
     */
    public int increasePrice(int oldPrice, int upgradeAmount){
        if (upgradeAmount == 0){
            return oldPrice;
        } else {
            int newPrice = oldPrice * (2 * upgradeAmount);
            return newPrice;
        }
    }

    //releases noise effect from memory
    protected void onPause() {
        super.onPause();
        buttonHitSound.release();
    }

}
