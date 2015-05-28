package trevorwarner.individualgame;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

//leaderboard 1st place trophy image found from iconFinder.com
//All trophy images were modified from that image
public class LeaderBoard extends ActionBarActivity {

    //initializes Strings for string of corresponding textviews
    String firstVal, secondVal, thirdVal, fourthVal, fifthVal;
    //initializes textViews that display each place
    TextView first, second, third, fourth, fifth;

    long newScore;  //new score
    String newName; //newName input by user
    ActionBar myBar;
    Button button;  //main menu button
    //initialized shared pref + edit
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    //initialized score array list + name array list
    List<Long> scoreList = new ArrayList<>();
    List<String> nameList = new CustomStringList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creates leaderboard shared pref + allows to be edited
        prefs = getApplicationContext().getSharedPreferences("leaderBoardPref", MODE_PRIVATE);
        editor = prefs.edit();
        setContentView(R.layout.activity_leader_board);

        //sets actionbar title
        myBar = getSupportActionBar();
        myBar.setTitle("Brick Bash");

        //sets all textviews to corresponding variables
        first = (TextView) findViewById(R.id.firstPlace);
        second = (TextView) findViewById(R.id.secondPlace);
        third = (TextView) findViewById(R.id.thirdPlace);
        fourth = (TextView) findViewById(R.id.fourthPlace);
        fifth = (TextView) findViewById(R.id.fifthPlace);
        //sets menu button + listener
        button = (Button) findViewById(R.id.menuButton);
        button.setOnClickListener(menuListener);
        //gets new score from save state
        newScore = prefs.getLong("SavedScore", 0);
        //sets leaderboard display + score/name lists
        setLeaderBoard();
        setScoreList();
        setNameList();

        //if the new score is greater than any of the leaderbaord scores
        //then start update process
        for(int i = 0; i < scoreList.size(); i++){
           if (newScore > scoreList.get(i)){
               namePrompt();
               break;
           }
        }
    }

    /*
        sets all textView to saved state
     */
    public void setLeaderBoard() {
        first.setText(prefs.getString("SavedFirst", "Trevor 0"));
        second.setText(prefs.getString("SavedSecond", "Omar 0"));
        third.setText(prefs.getString("SavedThird", "Tyrant Ben 0"));
        fourth.setText(prefs.getString("SavedFourth", "Grace 0"));
        fifth.setText(prefs.getString("SavedFifth", "Logan 0"));
    }

    /*
    -gets textview String for each place
    -adds score to scoreList for each place by removing all non-digit characters from string
     */
    public void setScoreList() {
        firstVal = first.getText().toString();
        scoreList.add(Long.parseLong(firstVal.replaceAll("[^\\d]", "")));

        secondVal = second.getText().toString();
        scoreList.add(Long.parseLong(secondVal.replaceAll("[^\\d]", "")));

        thirdVal = third.getText().toString();
        scoreList.add(Long.parseLong(thirdVal.replaceAll("[^\\d]", "")));

        fourthVal = fourth.getText().toString();
        scoreList.add(Long.parseLong(fourthVal.replaceAll("[^\\d]", "")));

        fifthVal = fifth.getText().toString();
        scoreList.add(Long.parseLong(fifthVal.replaceAll("[^\\d]", "")));
    }

    /*
    -gets textview String for each place
    -adds name to nameList for each place by removing all digit characters from string
     */
    public void setNameList() {
        firstVal = first.getText().toString();
        nameList.add(firstVal.replaceAll("[0-9]", ""));

        secondVal = second.getText().toString();
        nameList.add(secondVal.replaceAll("[0-9]", ""));

        thirdVal = third.getText().toString();
        nameList.add(thirdVal.replaceAll("[0-9]", ""));

        fourthVal = fourth.getText().toString();
        nameList.add(fourthVal.replaceAll("[0-9]", ""));

        fifthVal = fifth.getText().toString();
        nameList.add(fifthVal.replaceAll("[0-9]", ""));
    }

    /*
    creates an alert dialog for getting a user inputed name
    -on save of name calls validator then updates accordingly
     */
    public void namePrompt() {
        //creates a dialog box which prompts user for name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Your Name");
        final EditText input = new EditText(this);
       builder.setView(input);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        builder.setView(input);


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //adds space to end of name to be comparable to other stored names
                newName = input.getText().toString() + " ";
                //if name is validated call 1 of 2 update sequences
                if (validated(input, newName)) {
                    //if name already exists, call method which updates that names score
                    if (nameList.contains(newName)) {
                        updateContainsName();
                    //else call normal score updater
                    } else {
                        updateScore(newScore);
                    }
                //call namePrompt again if input name contained digits or was too long
                } else {
                    namePrompt();
                }
            }
        });
        builder.show();
    }

    /*
    -Checks to see which place the new score got
    -then puts score in that place, and all subsequent places shift down
    -finally save changes to shared preferences
     */
    public void updateScore(long newScore) {
            if (newScore > scoreList.get(0)) {
                fifth.setText(nameList.get(3) + scoreList.get(3));
                fourth.setText(nameList.get(2) + scoreList.get(2));
                third.setText(nameList.get(1) + scoreList.get(1));
                second.setText(nameList.get(0) + scoreList.get(0));
                first.setText(newName + newScore);
            } else if (newScore > scoreList.get(1)) {
                fifth.setText(nameList.get(3) + scoreList.get(3));
                fourth.setText(nameList.get(2) + scoreList.get(2));
                third.setText(nameList.get(1) + scoreList.get(1));
                second.setText(newName + newScore);
            } else if (newScore > scoreList.get(2)) {
                fifth.setText(nameList.get(3) + scoreList.get(3));
                fourth.setText(nameList.get(2) + scoreList.get(2));
                third.setText(newName + newScore);
            } else if (newScore > scoreList.get(3)) {
                fifth.setText(nameList.get(3) + scoreList.get(3));
                fourth.setText(newName + newScore);
            } else {
                fifth.setText(newName + newScore);
            }
        saveChanges();
    }

    /*
    returns true if name is acceptable
    -limits character length of name to 12 characters
    -limits name to only characters (no digits)
     */
    public boolean validated(EditText input, String name){
        if (name.length() > 13){
            Toast toast = Toast.makeText(getBaseContext(), "Name must be 12 characters or less", Toast.LENGTH_LONG);
            toast.setGravity( Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        //goes through each character of name
        for(int i = 0; i< name.length(); i++){
            char c = name.charAt(i);

            //if a character is a digit, clear the editText and return false
            if(Character.isDigit(c))
            {
                input.getText().clear();
                Toast toast = Toast.makeText(getBaseContext(), "No Digits Allowed", Toast.LENGTH_LONG);
                toast.setGravity( Gravity.CENTER, 0, 0);
                toast.show();

                return false;
            }
        }
        return true;
    }

    /*
    if name already exists in leaderboard:
    -find index of name
    -if the name got a new highscore, remove old score and name position from list
    -update + reorder list with new items
     */
    public void updateContainsName() {
        int nameIndex = 0;
        for (int i = 0; i < nameList.size(); i++){
            if (nameList.get(i).equalsIgnoreCase(newName)){
                nameIndex = i;
                break;
            }
        }
        /*
        -find index (place) of name
        -if the entered name got a new highscore, replace old score with new score and re-order
        -replacing score: delete score and name from both array lists
        -add placeholders to end of both lists to avoid indexOutOfBounds
        -then call updateScore to add newScore and newName to leaderBoard
        */
        if (nameIndex == 0 && newScore > scoreList.get(0)){
            first.setText(nameList.get(0) + newScore);
            saveChanges();
        } else if (nameIndex == 1 && newScore > scoreList.get(1)){
            nameList.remove(1);
            nameList.add("placeholder");
            scoreList.remove(1);
            scoreList.add((long) 0);
            updateScore(newScore);
        } else if (nameIndex == 2 && newScore > scoreList.get(2)) {
            nameList.remove(2);
            nameList.add("placeholder");
            scoreList.remove(2);
            scoreList.add((long) 0);
            updateScore(newScore);
        } else if (nameIndex == 3 && newScore > scoreList.get(3)) {
            nameList.remove(3);
            nameList.add("placeholder");
            scoreList.remove(3);
            scoreList.add((long) 0);
            updateScore(newScore);
        } else if (nameIndex == 4 && newScore > scoreList.get(4)) {
            nameList.remove(4);
            nameList.add("placeholder");
            scoreList.remove(4);
            scoreList.add((long) 0);
            updateScore(newScore);
        }else {
            //if name entered already exists and didn't achieve a high score...
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Sorry, " + newName + " did not achieve a new high score", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /*
    commits changes to save state
     */
    public void saveChanges() {
        editor.putString("SavedFirst", first.getText().toString());
        editor.putString("SavedSecond", second.getText().toString());
        editor.putString("SavedThird", third.getText().toString());
        editor.putString("SavedFourth", fourth.getText().toString());
        editor.putString("SavedFifth", fifth.getText().toString());
        editor.remove("SavedScore");
        editor.apply();
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
       switch (item.getItemId()){
           case R.id.action_reset_brick_bits:

               return true;

           case R.id.action_Reset:  //resets leaderboard
               editor.clear();
               editor.apply();
               finish();
               return true;

           default:
               return super.onOptionsItemSelected(item);
       }
    }
}

//allows names to be compared regardless of case
class CustomStringList extends ArrayList<String> {
    @Override
    public boolean contains(Object o) {
        String paramStr = (String)o;
        for (String s : this) {
            if (paramStr.equalsIgnoreCase(s)) return true;
        }
        return false;
    }
}


