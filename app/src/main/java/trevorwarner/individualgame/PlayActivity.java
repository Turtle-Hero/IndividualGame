package trevorwarner.individualgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



/*
-The cracked brick image was taken from http://www.clipartpanda.com/, and was modified by me to
make the brick and (more) cracked brick images.
-The rubble pile was also taken from http://www.clipartpanda.com/ and modified by me to look like
brick rubble.
-The brick hitting noise was taken from http://soundbible.com/tags-karate.html
 */
public class PlayActivity extends ActionBarActivity {
    public static final String TAG = "BrickBash";

    private Upgrades upgrades;
    private BrickBit brickBits;
    private int brickTapCount;
    private double brickHealth;
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
    private ImageButton brickButton;
    private SharedPreferences prefs;
    int soundID;
    long millis;
    boolean endRoundState = false;
    Handler myHandler = new Handler();
    SoundPool buttonHitSound;
    Timer cdTimer;
    ActionBar actionbar;
    int roundCount = 0;

    double clickPower = 1;

    //stuff grace added
    double swipePower = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //hides actionbar
        actionbar = getSupportActionBar();
        actionbar.hide();

        prefs = getApplicationContext().getSharedPreferences("LeaderBoardSaves", MODE_PRIVATE);

        //initialized textviews
        brickView = (TextView) findViewById(R.id.brickCount);
        scoreKeeper = (TextView) findViewById(R.id.scoreKeeper);
        roundKeeper = (TextView) findViewById(R.id.roundKeeper);
        timeKeeper = (TextView) findViewById(R.id.timeKeeper);
        brickButton = (ImageButton) findViewById(R.id.brickButton);

        //initialize brick hit noise
        buttonHitSound = new SoundPool(15, AudioManager.STREAM_MUSIC,1);
        soundID = buttonHitSound.load(this, R.raw.hit_sound, 1);
//        brickButton.setOnClickListener(brickListener);

        //Grace added:
        brickButton.setOnTouchListener(brickSwipeListener);

        //initializes countdown timer
        cdTimer = new Timer(10000, 10);

        upgrades = new Upgrades(clickPower);
        brickBits = BrickBit.getMainBrickBitBank(prefs);

        checkUpgrades();
        newRound();

    }

    //starts every round. Updates round #, creates new brick image, updates brick health
    public void newRound() {
        endRoundState = false;
        newBrick();
        brickTapCount = 0;
        brickView.setText("" + 0);
        //updates and sets to textview Round count
        roundCount++;
        roundKeeper.setText("" + roundCount);
        //sets brick health benchmarks
        setHealthMarks();
        //Shows startTime for Round
        timeKeeper.setText("10.0");
        //starts timer when Start dialog button is clicked
        roundAlert();
    }


    //displays round # in an alert, + start button click starts timer
    public void roundAlert() {
        //General syntax for making a custom text view for an alert was found on StackOverflow
        AlertDialog.Builder newRoundAlert = new AlertDialog.Builder(this);
        TextView custom = new TextView(this);
        custom.setText("Round " + roundCount);
        custom.setTextSize(35);
        custom.setGravity(Gravity.CENTER_VERTICAL);
        custom.setGravity(Gravity.CENTER_HORIZONTAL);
        newRoundAlert.setView(custom);
        newRoundAlert.setNeutralButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cdTimer.start();
            }
        });
//Hitting the back button no longer allows a player to play the round with no timer
//Can either end activity as done here or start the round anyway
        newRoundAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        AlertDialog alert = newRoundAlert.create();
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        wmlp.y = 150;
        alert.setCancelable(false);
        alert.show();

    }

    //on a brick hit, play brick hit sound, update tap amount and update brick health
//    View.OnClickListener brickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Log.d(TAG, " onClick called.");
//
//           buttonHitSound.play(soundID,1,1,1,0,1);
//           if (!endRoundState) {
//               brickTapSetter(false);
//               updateBrickHealth(false);
//           }
//        }
//    };

    //Grace added brickSwipeListener and got rid of the onClickListener
    //When a brick is swiped, update tap amount and update brick health
    View.OnTouchListener brickSwipeListener = new View.OnTouchListener(){
        int startX = 0;
        int startY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int actionTaken = event.getAction();

            if(actionTaken == MotionEvent.ACTION_DOWN){

                startX = (int) event.getX();
                startY = (int) event.getY();

            } else if (actionTaken == MotionEvent.ACTION_UP) {
                int endX = (int) event.getX();
                int endY = (int) event.getY();

                if(((Math.abs(startX - endX) <= 5) && (Math.abs(startY - endY) <=5))) {
                    Log.d(TAG, "click");
                    buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                    if (!endRoundState) {
                        brickTapSetter(false);
                        updateBrickHealth(false);
                    }
                } else if (((Math.abs(startX - endX) > 5) && (Math.abs(startY - endY) > 5))) {
                    Log.d(TAG, "swipe");
                    buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                    if (!endRoundState) {
                        brickTapSetter(true);
                        updateBrickHealth(true);
                    }
                }
            }
            return false;
        }
    };

    //display the # of taps
    public void brickTapSetter(boolean swipeOccured) {

        if (brickHealth > 0) {
            if(swipeOccured){
                brickTapCount = (int) (brickTapCount + swipePower);
                brickView.setText("" +brickTapCount);
            } else {

                brickTapCount = (int) (brickTapCount + clickPower);
                brickView.setText("" + brickTapCount);
            }
        }
    }

    //updates the health values for brick and brick image
    public void updateBrickHealth(boolean swipeOccured) {

        //grace put in the if/else statement
        if(upgrades.swipingIsEnabled() && swipeOccured){
            Log.d(TAG, "swipe counted ");
            brickHealth = brickHealth - swipePower;
        }else{
            Log.d(TAG, "click counted");
            brickHealth = brickHealth - clickPower;
        }

        //brickHealth--;
        if ( (brickHealth <= twoThirdsHealth) && (brickHealth > oneThirdHealth) ){
            brickButton.setImageResource(R.drawable.cracked_brick);
        } else if ( brickHealth <= oneThirdHealth && brickHealth > 0){
            brickButton.setImageResource(R.drawable.broken_brick);
        } else if (brickHealth <= 0){
            cdTimer.cancel();
            brickButton.setImageResource(R.drawable.pile_rocks);
            endRound();
        }
    }

    //creates new brick image
    public void newBrick() {
        brickButton.setImageResource(R.drawable.brick);
    }

    //sets brick health each round
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



    //resets variables and textviews, then initializes new round.
    public void endRound () {
       endRoundState = true;
       updateScore();
       totalBrickHealth = totalBrickHealth + (1 + totalBrickHealth / 2);
       brickHealth = totalBrickHealth;
       //newRound();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newRound();
            }
        }, 1000);
    }

    //stores score in shared preference for leaderboard
    //starts alert for Main Menu + leaderboard navigation
    public void endGame() {
        SharedPreferences.Editor editor = prefs.edit();

        brickBits.increaseBrickBits(score);

        Toast toast = Toast.makeText(getApplicationContext(), "BrickBits = " + brickBits.getBrickBits(), Toast.LENGTH_SHORT);
        toast.show();


        editor.putInt("SavedScore", score);
        editor.commit();
        final Intent intent = new Intent(PlayActivity.this, LeaderBoard.class);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You Lost!");
        builder.setMessage("Score: " + score);
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setPositiveButton("LeaderBoard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(intent);
            }
        });
        builder.show();
    }

//Creating a class to extend the CountDownTimer class and creating a constructor method for it
//found on StackOverflow, the rest is me.
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
            endGame();
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

//Stops timer when activity is paused so that CountDownTimer cdTimer will not cause
//application to crash.
    @Override
    protected void onPause() {
        super.onPause();
        cdTimer.cancel();
    }

    public void checkUpgrades() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("LeaderBoardSaves", MODE_PRIVATE);
        Boolean clickPowerBoolean = prefs.getBoolean("clickPowerBoolean", false);
        Boolean swipePowerBoolean = prefs.getBoolean("swipePowerBoolean", false);
        if (clickPowerBoolean){
            clickPower = upgrades.clickPower(clickPower);
        }
       if (swipePowerBoolean){
            swipePower = upgrades.swipe(swipePower);
        }

    }


}
