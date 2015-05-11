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

    //objects initialized
    private Brick brickObject;
    private BrickBitBank brickBitsBank;
    private Upgrades upgrades;

    //tracked variables throughout round
    private int score;
    private boolean endRoundState = false;
    private int roundCount = 0;

    //xml variables
    private TextView brickView;
    private TextView scoreKeeper;
    private TextView timeKeeper;
    private ImageButton brickButton;
    private ImageButton bombButton;
    private ImageButton nukeButton;

    //countdown timer variables
    private String timeSec;
    private String timeMilli;
    private long millis;
    private Timer cdTimer;
    private String totTime;

    //used for small break b/w rounds
    private Handler myHandler = new Handler();

    //sound variables
    private int soundID;
    private SoundPool buttonHitSound;

    private SharedPreferences upgradePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //hides actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        upgradePref = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
        upgrades = new Upgrades(upgradePref);

        //initialized textviews
        brickView = (TextView) findViewById(R.id.brickCount);
        scoreKeeper = (TextView) findViewById(R.id.scoreKeeper);
        timeKeeper = (TextView) findViewById(R.id.timeKeeper);
        brickButton = (ImageButton) findViewById(R.id.brickButton);


        //initialize brick hit noise
        buttonHitSound = new SoundPool(15, AudioManager.STREAM_MUSIC,1);
        soundID = buttonHitSound.load(this, R.raw.hit_sound, 1);
//        brickButton.setOnClickListener(brickListener);

        //Grace added:
        brickButton.setOnTouchListener(brickSwipeListener);

        //checks for bomb and nuke upgrade buttons, and sets things accordingly
        checkAddButtons();

        //initializes countdown timer
        if (upgrades.getTimerPower()== 1){
            cdTimer = new Timer(15000, 10);
            totTime = "15.0";
        } else {
            cdTimer = new Timer(10000, 100);
            totTime = "10.0";
        }
        newRound();

    }

    public void checkAddButtons() {
        if (upgrades.getBombUpgrade() >= 1){
            bombButton = (ImageButton) findViewById(R.id.bombButton);
            bombButton.setVisibility(View.VISIBLE);
            bombButton.setOnClickListener(bombButtonListener);
        } else if(upgrades.getNukeUpgrade() >= 1){
            nukeButton = (ImageButton) findViewById(R.id.nukeButton);
            nukeButton.setVisibility(View.VISIBLE);
            nukeButton.setOnClickListener(nukeButtonListener);
        }
    }

    View.OnClickListener bombButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            brickObject.setBrickHealth(upgrades.bombPower(brickObject.getCurrentBrickHealth()));
            bombButton.setVisibility(View.GONE);

            if(brickObject.getCurrentBrickHealth()<=0){
                endRound();
            }
        }
    };

    View.OnClickListener nukeButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            nukeButton.setVisibility(View.GONE);
            endRound();
        }
    };

    //starts every round. Updates round #, creates new brick image, updates brick health
    public void newRound() {
        endRoundState = false;
        brickView.setText("" + 0);
        //updates and sets to textview Round count
        roundCount++;
        //create and show the new brick
        brickObject=new Brick(roundCount, brickButton);
        showBrick();
        //Shows startTime for Round
        timeKeeper.setText(totTime);
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
                        updateBrickHealth(false);
                        brickTapSetter();
                    }
                } else if (((Math.abs(startX - endX) > 5) && (Math.abs(startY - endY) > 5))) {
                    Log.d(TAG, "swipe");
                    buttonHitSound.play(soundID, 1, 1, 1, 0, 1);
                    if (!endRoundState) {
                        updateBrickHealth(true);
                        brickTapSetter();
                    }
                }
            }
            return false;
        }
    };

    //display the # of taps
    public void brickTapSetter() {
        brickView.setText("" + brickObject.getBrickHits());
    }

    //updates the health values for brick and brick image
    public void updateBrickHealth(boolean swipeOccurred) {

        //grace put in the if/else statement
        if((upgrades.getSwipeUpgrade() >= 1) && swipeOccurred){
            Log.d(TAG, "swipe counted ");
            brickObject.setBrickHealth(upgrades.getSwipePower());
        }else{
            Log.d(TAG, "click counted");
            brickObject.setBrickHealth(upgrades.getClickPower());
        }

        if(brickObject.getCurrentBrickHealth()<=0){
            endRound();
        }
    }

    //creates new brick image
    public void showBrick() {
        brickObject.getBrickButton();
    }


    public void updateScore() {
        score = score + (brickObject.getBrickHits() / 4) + (int) (millis / 1000);
        score = (int) (score * upgrades.getMoneyPower());
        scoreKeeper.setText("" + score);
    }



    //resets variables and textviews, then initializes new round.
    public void endRound () {
       endRoundState = true;
       cdTimer.cancel();
       updateScore();
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
        SharedPreferences brickBitPref = getApplicationContext().getSharedPreferences("brickBitPref", MODE_PRIVATE);
        BrickBitBank brickBitsBank = BrickBitBank.getMainBrickBitBank(brickBitPref);
        brickBitsBank.increaseBrickBits(score);

        final Intent intent = new Intent(PlayActivity.this, LeaderBoard.class);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You Lost at round " + roundCount + "!");
        builder.setMessage("Score: " + score + "\n BrickBits = " + brickBitsBank.getBrickBits());
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setPositiveButton("LeaderBoard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences leaderBoardPref = getApplicationContext().getSharedPreferences("leaderBoardPref", MODE_PRIVATE);
                SharedPreferences.Editor leaderBoardEditor;
                leaderBoardEditor = leaderBoardPref.edit();
                leaderBoardEditor.putInt("SavedScore", score);
                leaderBoardEditor.commit();
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
                timeMilli = timeMilli.substring(timeMilli.length() -1);
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
        if (id == R.id.action_reset_brick_bits) {
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

}
