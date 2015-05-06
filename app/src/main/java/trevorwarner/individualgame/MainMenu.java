package trevorwarner.individualgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

//comment for sharing on GitHub

//Play Button Taken from http://buyadoo.com/css-Animated-Button/
//LeaderBoard Icon taken from http://corporaterewards.com/automating-sales-incentives-telecommunications-giant/
public class MainMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //initialize Play and Leaderboard button w/Listeners
        ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(playListener);

        ImageButton leaderBoard = (ImageButton) findViewById(R.id.LButton);
        leaderBoard.setOnClickListener(leaderBoardListener);

        ImageButton upgrade = (ImageButton) findViewById(R.id.UButton);
        upgrade.setOnClickListener(upgradeListener);
    }

    //Open Activity where game happens
    View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //SharedPreferences prefs = getApplicationContext().getSharedPreferences("LeaderBoardSaves", MODE_PRIVATE);
            //SharedPreferences.Editor editor = prefs.edit();
            //editor.clear();
            //editor.commit();

            Intent intent = new Intent(MainMenu.this, PlayActivity.class);

            startActivity(intent);
        }
    };

    //Open LeaderBoard activity
    View.OnClickListener leaderBoardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainMenu.this, LeaderBoard.class);
            startActivity(i);
        }
    };

    View.OnClickListener upgradeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainMenu.this, UpgradesShop.class);
            startActivity(i);
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset_brick_bits) {
            SharedPreferences upgrades = getApplicationContext().getSharedPreferences("brickBitPref", MODE_PRIVATE);
            SharedPreferences.Editor brickBitEditor = upgrades.edit();
            brickBitEditor.clear();
            brickBitEditor.apply();
        }
        if (id == R.id.action_reset_upgrades) {
            SharedPreferences upgrades = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
            SharedPreferences.Editor upgradeEditor = upgrades.edit();
            upgradeEditor.clear();
            upgradeEditor.apply();
        }

        return super.onOptionsItemSelected(item);
    }
}
