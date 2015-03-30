package trevorwarner.individualgame;

import android.graphics.drawable.Drawable;
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
    private int score;
    private TextView brickView;
    private String brickTapString;
    private ImageButton brickButton;
    private Drawable crackBrick;
    private String TAG = " TAG ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        brickView = (TextView) findViewById(R.id.brickCount);
        brickButton = (ImageButton) findViewById(R.id.brickButton);
        totalBrickHealth = 3;
        brickHealth = totalBrickHealth;

        setHealthMarks();
        brickButton.setOnClickListener(brickListener);
    }

    View.OnClickListener brickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           brickTapSetter();

           updateBrickHealth();
        }
    };

    public void brickTapSetter() {
        brickTapString = brickView.getText().toString();
        brickTapString = brickTapString.substring(0, 5);
        brickTapCount++;
        brickView.setText(brickTapString + " " + brickTapCount);
    }

    public void updateBrickHealth() {
        brickHealth--;
        if (brickHealth == twoThirdsHealth){
            brickButton.setImageResource(R.drawable.cracked_brick);
        }
    }

    public void setHealthMarks() {
        twoThirdsHealth = (totalBrickHealth / 3) * 2;
        Log.d(TAG, " "+ twoThirdsHealth);
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
