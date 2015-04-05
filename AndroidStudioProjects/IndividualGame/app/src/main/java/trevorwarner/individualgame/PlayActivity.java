package trevorwarner.individualgame;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class PlayActivity extends ActionBarActivity {

    private int brickTapCount;
    private int brickHealth;
    private int prevRoundHealth;
    private int totalBrickHealth;
    private int twoThirdsHealth;
    private int oneThirdHealth;
    private int score;
    private TextView brickView;
    private TextView scoreKeeper;
    private TextView roundKeeper;
    private TextView timeKeeper;
    private String timeSec;
    private String timeMilli;
    private String brickTapString;
    private ImageButton brickButton;
    int soundID;
    long millis;
    SoundPool buttonHitSound;
    Timer cdTimer;
    int roundCount = 0;
    private String TAG = " TAG ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        brickView = (TextView) findViewById(R.id.brickCount);
        scoreKeeper = (TextView) findViewById(R.id.scoreKeeper);
        roundKeeper = (TextView) findViewById(R.id.roundKeeper);
        timeKeeper = (TextView) findViewById(R.id.timeKeeper);
        brickButton = (ImageButton) findViewById(R.id.brickButton);
        timeKeeper.setText("10:00");
        buttonHitSound = new SoundPool(10, AudioManager.STREAM_MUSIC,1);
        soundID = buttonHitSound.load(this, R.raw.hit_sound, 1);
        brickButton.setOnClickListener(brickListener);
        cdTimer = new Timer(10000, 10);

        newRound();

    }

    public void newRound() {
        roundCount++;
        newBrick();
        roundKeeper.setText("" + roundCount);
        setHealthMarks();
        cdTimer.start();

    }



    View.OnClickListener brickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           buttonHitSound.play(soundID,1,1,1,0,1);
           brickTapSetter();
           updateBrickHealth();
        }
    };

    //display the # of taps
    public void brickTapSetter() {
        if (brickHealth > 0) {
            brickTapCount++;
        }
        brickView.setText(""+brickTapCount);
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
            cdTimer.cancel();
            brickButton.setImageResource(R.drawable.pile_rocks);
            updateScore();
            endRound();

        }
    }

    public void newBrick() {
        brickButton.setImageResource(R.drawable.brick);
    }

    public void setHealthMarks() {
        if (roundCount == 1){
            totalBrickHealth = 3;
        }else {
            totalBrickHealth = prevRoundHealth + (roundCount + (roundCount - 1));
        }
        prevRoundHealth = totalBrickHealth;
        brickHealth = totalBrickHealth;
        twoThirdsHealth = (totalBrickHealth / 3) * 2;
        oneThirdHealth = (totalBrickHealth / 3);

    }

    public void updateScore() {
        score = (score + 1) + (int) (millis / 1000);
        scoreKeeper.setText("" + score);
    }

    public void endRound() {
        brickTapCount = 0;
        brickView.setText("" + 0);
        totalBrickHealth = totalBrickHealth + (1 + totalBrickHealth/2);
        brickHealth = totalBrickHealth;
        newRound();
    }

    public class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
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
