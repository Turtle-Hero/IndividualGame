package trevorwarner.individualgame;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//leaderboard 1st place trophy image found from iconFinder.com
//All trophy images were modified from that image
public class LeaderBoard extends ActionBarActivity {

    String firstVal, secondVal, thirdVal, fourthVal, fifthVal;
    TextView first, second, third, fourth, fifth;
    int nameIndex;
    int newScore;
    String newName;

    Button button;
    ActionBar actionbar;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    ArrayList<Integer> scoreList = new ArrayList<>();
    ArrayList<String> nameList = new CustomStringList();

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
        fourth = (TextView) findViewById(R.id.fourthPlace);
        fifth = (TextView) findViewById(R.id.fifthPlace);
        button = (Button) findViewById(R.id.menuButton);

        button.setOnClickListener(menuListener);
        newScore = prefs.getInt("SavedScore", 0);
        setLeaderBoard();
        setScoreList();
        setNameList();
        for(int i = 0; i < scoreList.size(); i++){
           if (newScore > scoreList.get(i)){
               namePrompt();
               break;
           }
        }
    }

    public void setLeaderBoard() {
        //sets all textView to saved state

        first.setText(prefs.getString("SavedFirst", "Trevor 0"));
        second.setText(prefs.getString("SavedSecond", "Omar 0"));
        third.setText(prefs.getString("SavedThird", "Ben 0"));
        fourth.setText(prefs.getString("SavedFourth", "Grace 0"));
        fifth.setText(prefs.getString("SavedFifth", "Logan 0"));
    }

    public void setScoreList() {
        //seperates score from string and adds to array list
        firstVal = first.getText().toString();
        scoreList.add(Integer.parseInt(firstVal.replaceAll("[^\\d]", "")));

        secondVal = second.getText().toString();
        scoreList.add(Integer.parseInt(secondVal.replaceAll("[^\\d]", "")));

        thirdVal = third.getText().toString();
        scoreList.add(Integer.parseInt(thirdVal.replaceAll("[^\\d]", "")));

        fourthVal = fourth.getText().toString();
        scoreList.add(Integer.parseInt(fourthVal.replaceAll("[^\\d]", "")));

        fifthVal = fifth.getText().toString();
        scoreList.add(Integer.parseInt(fifthVal.replaceAll("[^\\d]", "")));
    }

    public void setNameList() {
        //seperates name from string and adds to array list

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

    public void namePrompt() {
        //creates a dialog box which prompts user for name
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Your Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newName = input.getText().toString() + " ";
                if (nameList.contains(newName)){
                    updateContainsName();
                }else {
                    updateScore(newScore);
                }
            }
        });
        builder.show();
    }

    public void updateScore(int newScore) {
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

    public void updateContainsName() {
        //if the name is already in the leaderboard

        //nameIndex = nameList.indexOf(newName);

        for (int i = 0; i < nameList.size(); i++){
            if (nameList.get(i).equalsIgnoreCase(newName)){
                nameIndex = i;
                break;
            }
        }

        //find index (place) of name
        //if the entered name got a new highscore, replace old score with new score and re-order
        //replacing score -> delete score and name from both array lists and add placeholders to avoid indexOutOfBounds
        //then call updateScore to add newScore and newName to leaderBoard
        if (nameIndex == 0 && newScore > scoreList.get(0)){
            first.setText(nameList.get(0) + newScore);
            saveChanges();
        } else if (nameIndex == 1 && newScore > scoreList.get(1)){
            nameList.remove(1);
            nameList.add("placeholder");
            scoreList.remove(1);
            scoreList.add(0);
            updateScore(newScore);
        } else if (nameIndex == 2 && newScore > scoreList.get(2)) {
            nameList.remove(2);
            nameList.add("placeholder");
            scoreList.remove(2);
            scoreList.add(0);
            updateScore(newScore);
        } else if (nameIndex == 3 && newScore > scoreList.get(3)) {
            nameList.remove(3);
            nameList.add("placeholder");
            scoreList.remove(3);
            scoreList.add(0);
            updateScore(newScore);
        } else if (nameIndex == 4 && newScore > scoreList.get(4)) {
            nameList.remove(4);
            nameList.add("placeholder");
            scoreList.remove(4);
            scoreList.add(0);
            updateScore(newScore);
        }else {
            //if name entered didn't achieve a high score...
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, "Sorry, " + newName + " did not achieve a new high score", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void saveChanges() {
        //commits changes to shared preferences
        editor.putString("SavedFirst", first.getText().toString());
        editor.putString("SavedSecond", second.getText().toString());
        editor.putString("SavedThird", third.getText().toString());
        editor.putString("SavedFourth", fourth.getText().toString());
        editor.putString("SavedFifth", fifth.getText().toString());
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


