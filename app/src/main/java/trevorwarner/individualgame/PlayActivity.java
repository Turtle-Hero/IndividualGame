package trevorwarner.individualgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

/*
-The cracked brick image was taken from http://www.clipartpanda.com/, and was modified by me to
make the brick and (more) cracked brick images.
-The rubble pile was also taken from http://www.clipartpanda.com/ and modified by me to look like
brick rubble.
-the bomb and nuke images were taken from http://www.clipartpanda.com/
-The brick hitting noise was taken from freesound.org/people/nextmaking/sounds/86008/?page=1#, from user nextmaking
-The bomb noise was taken from https://www.freesound.org/people/dkmedic/sounds/104439/, from user dkmedic
-The nuke noise was taken from https://www.freesound.org/people/Lord%20Razu/sounds/107792/, from user Lord Razu
-The background music was taken from https://www.freesound.org/people/oceanictrancer/sounds/234276/, from user oceanictrancer
-The game over noise was taken from https://www.freesound.org/people/SoundEffectsPodcast_com/sounds/256091/,
    from user SoundEffectsPodcast_com
 */
public class PlayActivity extends ActionBarActivity {
    public static final String TAG = "BrickBash";

    //objects initialized, brick object for brick properties
    //upgrades object for any purchased upgrades
    private Brick brickObject;
    private Upgrades upgrades;

    //tracked variables throughout round
    private long score;     //score keeps track of points(brickbits) won each round
    private boolean endRoundState = false;      //endRoundState tracks if endRound or endGame is going
    private int roundCount = 1;     //keeps track of round #

    //xml variables
    private TextView brickView;
    private TextView scoreKeeper;
    private TextView timeKeeper;
    private ImageButton brickButton;
    private ImageButton bombButton;
    private ImageButton nukeButton;

    //countdown timer variables
    private String timeSec;     //concatanated string of seconds remaining
    private String timeMilli;   //concatanated string of milliseconds remaining
    private long millis;        //set to millis until finish in the onTick method of the cdt.
    private Timer cdTimer;
    private String totTime;     //string of total time in round

    //used for small break b/w rounds
    private Handler myHandler = new Handler();

    //sound variables
    private SoundPool buttonHitSound;
    private int brickID;
    private int bombID;
    private int nukeID;
    private int endID;
    private MediaPlayer mp;

    //shared pref for upgrades
    private SharedPreferences upgradePref;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //hides actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //creates media player to play background music, starts and loops it
        mp = MediaPlayer.create(this, R.raw.beats);
        mp.setLooping(true);
        mp.start();

        //creates upgrade object which also initializes all upgrades states
        upgradePref = getApplicationContext().getSharedPreferences("upgradePref", MODE_PRIVATE);
        upgrades = new Upgrades(upgradePref);
        edit = upgradePref.edit();

        //initialized textviews
        brickView = (TextView) findViewById(R.id.brickCount);
        scoreKeeper = (TextView) findViewById(R.id.scoreKeeper);
        timeKeeper = (TextView) findViewById(R.id.timeKeeper);
        brickButton = (ImageButton) findViewById(R.id.brickButton);


        //initializes soundpool and loads each sound to an ID so they are ready to play.
        buttonHitSound = new SoundPool(15, AudioManager.STREAM_MUSIC,1);
        brickID = buttonHitSound.load(this, R.raw.hit_sound, 1);
        bombID = buttonHitSound.load(this, R.raw.bomb_noise, 1);
        nukeID = buttonHitSound.load(this, R.raw.nuke_noise, 1);
        endID = buttonHitSound.load(this, R.raw.gameover, 1);

        //on touch listener for brick button
        brickButton.setOnTouchListener(brickSwipeListener);

        //checks for bomb and nuke upgrade buttons, and sets things accordingly
        checkAddButtons();

        //initializes countdown timer
        //if user has countdown upgrade, set to 15s rounds, else 10s rounds
        if (upgrades.getTimerPower()>= 1){
            cdTimer = new Timer(15000, 100);
            totTime = "15.0";
        } else {
            cdTimer = new Timer(10000, 100);
            totTime = "10.0";
        }

        //starts round 1 dialog
        roundAlert();

    }

    /*
    if bomb/nuke upgrades are present, initializes buttons, makes them visible,
    and sets listeners for them
     */
    public void checkAddButtons() {
        if (upgrades.getBombUpgrade() >= 1){
            bombButton = (ImageButton) findViewById(R.id.bombButton);
            bombButton.setVisibility(View.VISIBLE);
            bombButton.setOnClickListener(bombButtonListener);
        }
        if(upgrades.getNukeUpgrade() >= 1){
            nukeButton = (ImageButton) findViewById(R.id.nukeButton);
            nukeButton.setVisibility(View.VISIBLE);
            nukeButton.setOnClickListener(nukeButtonListener);
        }
    }

    /*
    -if not in endRound/endGame state:
    -Play the bomb noise, reduce brick health accordingly (1/3 of total brick health),
    hides button, and updates upgrade save state that they used a purchase.
    -Finally, if bomb causes brick to break, go to endRound.
     */
    View.OnClickListener bombButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (!endRoundState) {
                buttonHitSound.play(bombID, 1, 1, 1, 0, 1);

                brickObject.setBrickHealth(upgrades.bombPower(brickObject.getCurrentBrickHealth() + 1));
                bombButton.setVisibility(View.GONE);
                edit.putInt("bombCount", (upgrades.getBombUpgrade() - 1));
                edit.apply();

                if (brickObject.getCurrentBrickHealth() <= 0) {
                    endRound();
                }
            }
        }
    };


    /*
    -if not in endRound/endGame state:
    -Play the nuke noise, reduce brick health to 0,
    hides button, and updates upgrade save state that they used a purchase.
    -Then go to endRound.
    */
    View.OnClickListener nukeButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (!endRoundState) {
                buttonHitSound.play(nukeID, 1, 1, 1, 0, 1);

                brickObject.setBrickHealth(brickObject.getCurrentBrickHealth());
                nukeButton.setVisibility(View.GONE);
                edit.putInt("nukeCount", (upgrades.getNukeUpgrade() - 1));
                edit.apply();

                endRound();
            }
        }
    };

    /*
        -Starts every round after roundAlert 'start' button is pressed
        -sets endRoundState to false so buttons work again.
        -Resets the text views: Hit count (brickView) and timer (timeKeeper)
        -creates new brick, and displays it.
     */
    public void newRound() {
        endRoundState = false;
        brickView.setText("" + 0);
        timeKeeper.setText(totTime);
        brickObject=new Brick(roundCount, brickButton);
        showBrick();
    }


    /*
    -displays round # in an alert
    -start button starts countDown timer and begins new Round
    -gives user 'break' in between rounds.
     */
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
                newRound();
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

        //creates the alertDialog
        //sets where the dialog is displayed and then shows it.
        AlertDialog alert = newRoundAlert.create();
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        wmlp.y = 150;
        alert.setCancelable(false);
        alert.show();

    }


    //When a brick is swiped, update tap amount and update brick health
    View.OnTouchListener brickSwipeListener = new View.OnTouchListener(){
        //start of where user touched (x and y coordinates)
        int startX = 0;
        int startY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int actionTaken = event.getAction();

            if(actionTaken == MotionEvent.ACTION_DOWN){
                //gets starting points when user touches down
                startX = (int) event.getX();
                startY = (int) event.getY();

            } else if (actionTaken == MotionEvent.ACTION_UP) {
                //gets ending points when user lifts up finger
                int endX = (int) event.getX();
                int endY = (int) event.getY();

                //if 'swipe' distance is less than or equal to 5, count as touch event
                if(((Math.abs(startX - endX) <= 5) && (Math.abs(startY - endY) <=5))) {
                    //plays brick hit noise
                    buttonHitSound.play(brickID, (float).25, (float).25, 1, 0, 1);
                    //updates brick health and hit amount
                    if (!endRoundState) {
                        updateBrickHealth(false);
                        brickTapSetter();
                    }
                    //if 'swipe' distance is greater than or equal to 5, count as swipe
                } else if (((Math.abs(startX - endX) > 5) && (Math.abs(startY - endY) > 5))) {
                    //plays brick hit noise
                    buttonHitSound.play(brickID, (float).25, (float).25, 1, 0, 1);
                    //updates brick health and hit amount
                    if (!endRoundState) {
                        updateBrickHealth(true);
                        brickTapSetter();
                    }
                }
            }
            //default is a touch event
            return false;
        }
    };

    //display the # of taps to the brickView textview
    public void brickTapSetter() {
        brickView.setText("" + brickObject.getBrickHits());
    }

    //updates the health values for brick and brick image
    public void updateBrickHealth(boolean swipeOccurred) {
        //sees if touch or swipe occured and if upgrades are present
        //when updating brick Health
        if((upgrades.getSwipeUpgrade() >= 1) && swipeOccurred){
            brickObject.setBrickHealth(upgrades.getSwipePower());
        }else{
            brickObject.setBrickHealth(upgrades.getClickPower());
        }

        //if the brick health is 0, end the round
        if(brickObject.getCurrentBrickHealth()<=0){
            endRound();
        }
    }

    //shows new brick image
    public void showBrick() {
        brickObject.getBrickButton();
    }

    /*
    Score updated at end of every round
    -calculated from remaining time in round + 1/4 of hits needed to break brick.
    -then multiplied by money upgrade power
    -then displayed in scorekeeper textview
     */
    public void updateScore() {
        score = (long) (score +  ((brickObject.getBrickHits() / 4) + (millis / 1000)* upgrades.getMoneyPower()));
        scoreKeeper.setText("" + score);
    }



    /*
    -endRoundState set so buttons can't be hit
    -cancel the timer
    -update the score
    -update roundCount for next round
    -delay for 1 sec (otherwise loads to fast and you never see rubble)
    -then run roundAlert for next round
     */
    public void endRound () {
       endRoundState = true;
       cdTimer.cancel();
       updateScore();
        roundCount++;
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                roundAlert();
            }
        }, 1000);
    }

    /*
    -creates brickBit shared Pref + object
    -increases total brick bits by gained amount this game
    -creates alert Dialog showing options after you lose.
     */
    public void endGame() {
        SharedPreferences brickBitPref = getApplicationContext().getSharedPreferences("brickBitPref", MODE_PRIVATE);
        BrickBitBank brickBitsBank = BrickBitBank.getMainBrickBitBank(brickBitPref);
        brickBitsBank.increaseBrickBits(score);

        //-if timer upgrade was used, subtract purchase from save state
        if (upgrades.getTimerPower() >= 1){
            edit.putInt("timerCount", (upgrades.getTimerPower() -1));
            edit.apply();
        }

        mp.stop();      //stops background music
        buttonHitSound.play(endID, 1, 1, 1, 0, 1);      //plays endGame noise

        final Intent intent = new Intent(PlayActivity.this, LeaderBoard.class); //loads leaberboard activity

        //creates alert dialog with how far you got, total score, and total brickBits
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You Lost at round " + roundCount + "!");
        builder.setMessage("Score: " + score + "\n BrickBits = " + brickBitsBank.getBrickBits());

        //brings user back to Main Menu
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        //brings user to leaderBoards
        builder.setPositiveButton("LeaderBoard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //loads leaderboard save state, and allow editing
                SharedPreferences leaderBoardPref = getApplicationContext().getSharedPreferences("leaderBoardPref", MODE_PRIVATE);
                SharedPreferences.Editor leaderBoardEditor;
                leaderBoardEditor = leaderBoardPref.edit();
                //saves current score to it
                leaderBoardEditor.putLong("SavedScore", score);
                leaderBoardEditor.commit();
                finish();
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    /*
    Handles countdown timer object creation
    -what happens every timer 'tick'
    -what happens when timer hits 0
     */
    public class Timer extends CountDownTimer {

        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            millis = millisUntilFinished;
            timeSec = "" + millis / 1000;       //seconds remianing
            timeMilli = "" + (millis / 100);    //milliseconds remaining
            //if milliseconds remaining is greater than 2 digits, get last digit
            if (timeMilli.length() > 1) {
                timeMilli = timeMilli.substring(timeMilli.length() -1);
            }
            //concatanate seconds and milliseconds remaining + display in textView
            timeKeeper.setText(timeSec + "." + timeMilli);
        }


        @Override
        //when timer gets to 0:
        //endRoundState -buttons stop working, set timer text to 0.0
        //call endGame
        public void onFinish() {
            endRoundState = true;
            timeKeeper.setText("0.0");
            endGame();
        }

    }

//Stops timer when activity is paused so that CountDownTimer cdTimer will not cause
//application to crash.
//also releases memory used for background + effect audio
    @Override
    protected void onPause() {
        super.onPause();
        mp.release();
        buttonHitSound.release();

        cdTimer.cancel();
    }

}
