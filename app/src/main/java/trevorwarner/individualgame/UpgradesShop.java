package trevorwarner.individualgame;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
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
    int price = 0;
    SharedPreferences upgradePref;

    TextView clickPrice, swipePrice, bombPrice, timerPrice, nukePrice, moneyPrice;
    TextView clickCountText, swipeCountText, bombCountText, timerCountText, nukeCountText, moneyCountText;

    private int soundID;
    private SoundPool buttonHitSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test2", "test2");
        super.onCreate(savedInstanceState);

        SharedPreferences brickBitPrefs = getApplicationContext().getSharedPreferences("brickBitPref", MODE_PRIVATE);
        upgradePref = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
        brickBitsBank = BrickBitBank.getMainBrickBitBank(brickBitPrefs);

        setContentView(R.layout.activity_upgrades_menu);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

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


        setPrices();

        buttonHitSound = new SoundPool(8, AudioManager.STREAM_MUSIC,1);
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
                brickBitsBank.decreaseBrickBits(increasePrice(100, swipeCount));
                swipeCount++;
                changedItem = "swipeCount";
                saveChanges(changedItem, swipeCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                swipePrice.setText("$" + increasePrice(100, swipeCount));
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
                brickBitsBank.decreaseBrickBits(increasePrice(200, timerCount));
                timerCount++;
                changedItem="timerCount";
                saveChanges(changedItem, timerCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                timerPrice.setText("$" + increasePrice(200, timerCount));
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



    public void saveChanges(String item, int change) {
        //commits changes to shared preferences
        SharedPreferences.Editor editor = upgradePref.edit();
        editor.putInt(item, change);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
        return true;
    }

    public void setPrices() {
        clickCountText.setText("x" + (upgradePref.getInt("clickPowerCount", 1) - 1));
        swipeCountText.setText("x" + (upgradePref.getInt("swipeCount", 0)));
        bombCountText.setText("x" + (upgradePref.getInt("bombCount", 0)));
        timerCountText.setText("x" + (upgradePref.getInt("timerCount", 0)));
        nukeCountText.setText("x" + (upgradePref.getInt("nukeCount", 0)));
        moneyCountText.setText("x" + (upgradePref.getInt("moneyCount", 0)));

        clickPrice.setText("$" + increasePrice(100, upgradePref.getInt("clickPowerCount", 1) - 1));
        swipePrice.setText("$" + increasePrice(100, upgradePref.getInt("swipeCount", 0)));
        bombPrice.setText("$" + increasePrice(75, upgradePref.getInt("bombCount", 0)));
        timerPrice.setText("$" + increasePrice(200, upgradePref.getInt("timerCount", 0)));
        nukePrice.setText("$" + increasePrice(250, upgradePref.getInt("nukeCount", 0)));
        moneyPrice.setText("$" + increasePrice(400, upgradePref.getInt("moneyCount", 0)));
    }

    public int increasePrice(int oldPrice, int upgradeAmount){
        if (upgradeAmount == 0){
            return oldPrice;
        } else {
            int newPrice = oldPrice * (2 * upgradeAmount);
            return newPrice;
        }
    }


}
