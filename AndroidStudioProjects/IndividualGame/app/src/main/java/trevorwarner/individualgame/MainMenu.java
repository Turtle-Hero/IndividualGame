package trevorwarner.individualgame;

import android.content.Intent;
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
    }

    //Open Activity where game happens
    View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
