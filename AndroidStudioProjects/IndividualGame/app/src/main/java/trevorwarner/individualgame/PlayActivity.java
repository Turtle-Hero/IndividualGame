package trevorwarner.individualgame;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;



public class PlayActivity extends ActionBarActivity {

    private int brickTapCount;
    private int brickHealth;
    private int totalBrickHealth;
    private int twoThirdsHealth;
    private int oneThirdHealth;
    private int score;
    private TextView brickView;
    private TextView scoreKeeper;
    private TextView timeKeeper;
    private String timeSec;
    private String timeMilli;
    private String brickTapString;
    private ImageButton brickButton;
    long millis;
    private String TAG = " TAG ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        brickView = (TextView) findViewById(R.id.brickCount);
        scoreKeeper = (TextView) findViewById(R.id.scoreKeeper);
        timeKeeper = (TextView) findViewById(R.id.timeKeeper);
        brickButton = (ImageButton) findViewById(R.id.brickButton);
        setHealthMarks();
        brickButton.setOnClickListener(brickListener);

        timeKeeper.setText("10:00");

        timer(); //starts the 10sec countdown

    }



    View.OnClickListener brickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           brickTapSetter();

           updateBrickHealth();
        }
    };

    //display the # of taps
    public void brickTapSetter() {
        brickTapString = brickView.getText().toString();
        brickTapString = brickTapString.substring(0, 5);
        brickTapCount++;
        brickView.setText(brickTapString + " " + brickTapCount);
    }

    //updates the health values for brick
    public void updateBrickHealth() {
        brickHealth--;
        if (brickHealth == twoThirdsHealth){
            brickButton.setImageResource(R.drawable.cracked_brick);
        }
        if ( brickHealth == oneThirdHealth ){
            brickButton.setImageResource(R.drawable.broken_brick);
        }
        if (brickHealth == 0){
            updateScore();

        }
    }

    public void setHealthMarks() {
        totalBrickHealth = 3;
        brickHealth = totalBrickHealth;
        twoThirdsHealth = (totalBrickHealth / 3) * 2;
        oneThirdHealth = (totalBrickHealth / 3);

    }

    public void updateScore() {
        score++;
        scoreKeeper.setText("" + score);
    }
    //countdown timer accurate to tenths place
    public void timer() {
        new  CountDownTimer(10000, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                millis = millisUntilFinished;
                timeSec = "" + millis / 1000;
                timeMilli = "" + (millis / 100);
                if (timeMilli.length() > 1) {
                    timeMilli = timeMilli.substring(1);
                }
                timeKeeper.setText(timeSec + "." + timeMilli);

            }

            @Override
            public void onFinish() {
                timeKeeper.setText("0.0");
            }
        }.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
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
