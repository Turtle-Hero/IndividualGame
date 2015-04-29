package trevorwarner.individualgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrades_menu);

        actionbar = getSupportActionBar();
        actionbar.hide();

        menuButton = (Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(menuListener);

        clickPowerButton = (ImageButton) findViewById(R.id.clickUpgrade);
        clickPowerButton.setOnClickListener(clickPowerListner);

        prefs = getApplicationContext().getSharedPreferences("LeaderBoardSaves", MODE_PRIVATE);
        editor = prefs.edit();

    }

    View.OnClickListener menuListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener clickPowerListner = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            clickPower = true;
            saveChanges();
        }
    };

    public void saveChanges() {
        //commits changes to shared preferences
        editor.putBoolean("clickPowerBoolean", clickPower);
        editor.commit();
    }



}
