package trevorwarner.individualgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by the_guz on 4/24/15.
 */
public class UpgradesShop extends ActionBarActivity {

    boolean clickPower = false;
    boolean swipeUpgradeEnabled = false;
    BrickBitBank brickBitsBank;
    ImageButton swipeUpgradeButton;

    ImageButton bombButton;
    int bombCount = 0;

    ImageButton nukeButton;
    int nukeCount = 0;

    int clickPowerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test2", "test2");
        super.onCreate(savedInstanceState);

        SharedPreferences brickBitPrefs = getApplication().getSharedPreferences("brickBitPref", MODE_PRIVATE);
        brickBitsBank = BrickBitBank.getMainBrickBitBank(brickBitPrefs);

        setContentView(R.layout.activity_upgrades_menu);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        ImageButton clickPowerButton = (ImageButton) findViewById(R.id.clickUpgrade);
        clickPowerButton.setOnClickListener(clickPowerListener);

        swipeUpgradeButton = (ImageButton) findViewById(R.id.swipeUpgrade);
        swipeUpgradeButton.setOnClickListener(swipeUpgradeListener);

        bombButton = (ImageButton) findViewById(R.id.bombButton);
        bombButton.setOnClickListener(bombButtonListener);

        TextView brickBitView = (TextView) findViewById(R.id.brickBitView);
        brickBitView.setText("Brick Bits: " + brickBitsBank.getBrickBits());

        nukeButton = (ImageButton) findViewById(R.id.nukeButton);
        nukeButton.setOnClickListener(nukeButtonListener);

    
    }

    View.OnClickListener clickPowerListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            clickPowerCount++;
            saveChanges();
        }
    };

    View.OnClickListener swipeUpgradeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            swipeUpgradeEnabled = true;
            saveChanges();
        }
    };

    View.OnClickListener bombButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bombCount++;
            saveChanges();
        }
    };

    View.OnClickListener nukeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nukeCount++;
            saveChanges();
        }
    };



    public void saveChanges() {
        //commits changes to shared preferences
        SharedPreferences upgradePrefs = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
        SharedPreferences.Editor editor = upgradePrefs.edit();
        editor.putInt("clickPowerCount", clickPowerCount);
        editor.putBoolean("swipePowerBoolean", swipeUpgradeEnabled);
        editor.putInt("bombCount", bombCount);
        editor.putInt("nukeCount", nukeCount);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
        return true;
    }


}
