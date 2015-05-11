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

    ImageButton clickPowerButton;
    int clickPowerCount = 1;

    ImageButton swipeUpgradeButton;
    int swipeCount = 0;

    ImageButton bombButton;
    int bombCount = 0;

    ImageButton nukeButton;
    int nukeCount = 0;

    ImageButton timerButton;
    int timerCount = 0;

    ImageButton moneyButton;
    int moneyCount = 0;

    private int soundID;
    private SoundPool buttonHitSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test2", "test2");
        super.onCreate(savedInstanceState);

        SharedPreferences brickBitPrefs = getApplication().getSharedPreferences("brickBitPref", MODE_PRIVATE);
        brickBitsBank = BrickBitBank.getMainBrickBitBank(brickBitPrefs);

        setContentView(R.layout.activity_upgrades_menu);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

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
                brickBitsBank.decreaseBrickBits(100);
                clickPowerCount++;
                Log.d("TEST", "" + clickPowerCount);
                changedItem = "clickPowerCount";
                saveChanges(changedItem, clickPowerCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
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
                brickBitsBank.decreaseBrickBits(100);
                swipeCount++;
                changedItem = "swipeCount";
                saveChanges(changedItem, swipeCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
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
                brickBitsBank.decreaseBrickBits(75);
                bombCount++;
                changedItem = "bombCount";
                saveChanges(changedItem, bombCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
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
                brickBitsBank.decreaseBrickBits(200);
                timerCount++;
                changedItem="timerCount";
                saveChanges(changedItem, timerCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
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
                brickBitsBank.decreaseBrickBits(250);
                nukeCount++;
                changedItem = "nukeCount";
                saveChanges(changedItem, nukeCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
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
                brickBitsBank.decreaseBrickBits(400);
                moneyCount++;
                changedItem="moneyCount";
                saveChanges(changedItem, moneyCount);
                buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
            } catch (BrickBitBank.InsufficientBrickBitsException e){
                Toast toast = Toast.makeText(getApplicationContext(), "Insufficient BrickBits", Toast.LENGTH_SHORT);
                toast.show();
            }
            brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());
        }
    };



    public void saveChanges(String item, int change) {
        //commits changes to shared preferences
        SharedPreferences upgradePrefs = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = upgradePrefs.edit();
        editor.putInt(item, change);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
        return true;
    }


}
