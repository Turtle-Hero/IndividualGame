package trevorwarner.individualgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by the_guz on 4/24/15.
 */
public class UpgradesShop extends ActionBarActivity {

    Button menuButton;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    ImageButton clickPowerButton;
    Boolean clickPower = false;
    ActionBar actionbar;

    ImageButton swipeUpgradeButton;
    boolean swipeUpgradeEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test2", "test2");
        super.onCreate(savedInstanceState);
        prefs = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
        editor = prefs.edit();
        setContentView(R.layout.activity_upgrades_menu);

        actionbar = getSupportActionBar();
        actionbar.hide();

        menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(menuListener);

        clickPowerButton = (ImageButton) findViewById(R.id.clickUpgrade);
        clickPowerButton.setOnClickListener(clickPowerListener);

        swipeUpgradeButton = (ImageButton) findViewById(R.id.swipeUpgrade);
        swipeUpgradeButton.setOnClickListener(swipeUpgradeListener);


    }

    View.OnClickListener menuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener clickPowerListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            clickPower = true;
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

    public void saveChanges() {
        //commits changes to shared preferences
        editor.putBoolean("clickPowerBoolean", clickPower);
        editor.putBoolean("swipePowerBoolean", swipeUpgradeEnabled);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
        return true;
    }


}
