package trevorwarner.individualgame;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//leaderboard 1st place trophy image found from iconFinder.com
//All trophy images were modified from that image
public class LeaderBoard extends ActionBarActivity {

    String firstVal, secondVal, thirdVal;
    TextView first, second, third;
    int newScore;
    int scoreOne, scoreTwo, scoreThree;
    Button button;
    ActionBar actionbar;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    Log test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getApplicationContext().getSharedPreferences("LeaderBoardSaves", MODE_PRIVATE);
        editor = prefs.edit();
        setContentView(R.layout.activity_leader_board);
        actionbar = getSupportActionBar();
        actionbar.hide();
        first = (TextView) findViewById(R.id.firstPlace);
        second = (TextView) findViewById(R.id.secondPlace);
        third = (TextView) findViewById(R.id.thirdPlace);
        button = (Button) findViewById(R.id.menuButton);

        button.setOnClickListener(menuListener);
        newScore = prefs.getInt("SavedScore", 0);
        first.setText(prefs.getString("SavedFirst", "1st: 0"));
        second.setText(prefs.getString("SavedSecond", "2nd: 0"));
        third.setText(prefs.getString("SavedThird", "3rd: 0"));

        if (newScore != 0) {
            updateScore(newScore);
        }



    }

    public void updateScore(int newScore) {
        firstVal = first.getText().toString().substring(5);
        scoreOne = Integer.parseInt(firstVal);

        secondVal = second.getText().toString().substring(5);
        scoreTwo = Integer.parseInt(secondVal);

        thirdVal = third.getText().toString().substring(5);
        scoreThree = Integer.parseInt(thirdVal);

        if (newScore > scoreOne || newScore > scoreTwo || newScore > scoreThree) {

            if (newScore > scoreOne) {
                third.setText("3rd: " + secondVal);
                second.setText("2nd: " + firstVal);
                first.setText("1st: " + newScore);
            } else if (newScore > scoreTwo) {
                third.setText("3rd: " + secondVal);
                second.setText("2nd: " + newScore);
            } else {
                third.setText("3rd: " + newScore);
            }
        }

        editor.putString("SavedFirst", first.getText().toString());
        editor.putString("SavedSecond", second.getText().toString());
        editor.putString("SavedThird", third.getText().toString());
        editor.remove("SavedScore");
        editor.commit();
    }

    View.OnClickListener menuListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
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
