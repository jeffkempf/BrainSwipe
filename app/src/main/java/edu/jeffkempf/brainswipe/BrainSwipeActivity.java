package edu.jeffkempf.brainswipe;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class BrainSwipeActivity extends Activity {

    private float x1, x2, y1, y2;
    private String direction;
    private int score;
    private String criteria;
    private int randNum;
    private String color;
    private boolean started = false;
    private CountDownTimer gameTimer;
    private Button startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        generateNumberPic();
        startButton = (Button)findViewById(R.id.start);
        startButton.setEnabled(true);
        resetScore();

    }

    public boolean onTouchEvent(MotionEvent me) {
        if(started) {
            direction = "";
            switch (me.getAction()) {
                //get coords when user first touches screen. ACTION_DOWN is pressing down, not swiping downward
                case MotionEvent.ACTION_DOWN: {
                    x1 = me.getX();
                    y1 = me.getY();
                    break;
                }

                //get ending coords when user removes finger
                case MotionEvent.ACTION_UP: {
                    x2 = me.getX();
                    y2 = me.getY(); //don't need to track up/down but leaving this in for now if change mind

                    //right swipe
                    if (x1 < x2) {
                        //Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                        direction = "right";
                    }

                    //left swipe
                    if (x1 > x2) {
                        //Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
                        direction = "left";
                    }
                    break; //for action up
                }
            }
            if (!direction.equals("")) //only want this called 2nd time through method
            {
                answer();
            }
            return false;//method gets called twice for each swipe. 1st to set x1, 2nd to set x2 and direction
        }
        else {
            return false;
        }
    }

    // FOLLOWING 2 METHODS FOR BUTTON CLICKS

    //displays instructions for game
    public void instructions_Click(View view) {
        String message = "A number will appear below. \nPay attention to the label above the number. " +
                "If it says color, match the color. If it says number, then match the number. 0 counts as even." +
                " Match the image by swiping in the correct direction\n\n" +
                "Swipe left for odd/red \nSwipe right for even/blue.\n\nYou have 60 seconds to get as many" +
                " correct as possible. You gain 1 point for a correct answer and lose 1 point for an incorrect one";
        DialogFrag diag = new DialogFrag();
        diag.setMessage(message);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        diag.show(ft, "Instructions");
    }

    //starts game
    public void start_Click(View view) {
        generateCriteria();
        generateNumberPic();
        startButton.setEnabled(false);
        //start timer
        gameTimer = new CountDownTimer(60000, 1000) {
            TextView timer = (TextView)findViewById(R.id.time);
            public void onTick(long millisUntilFinished) {
                timer.setText("Time remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("done!");
                started = false;
                String message = "Time's up! \n\nYour final score is: " + score;
                DialogFrag diag = new DialogFrag();
                diag.setMessage(message);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                diag.show(ft, "Final Score");
                startButton.setEnabled(true);
                resetScore();
            }
        }.start();
        started = true;
    }

    //FOLLOWING 2 METHODS GENERATE IMAGES/CRITERIA/ETC

    //will randomly generate a value to determine which number shown
    public void generateNumberPic() {
        Random rand = new Random();
        randNum = rand.nextInt(10); //should generate 0 - 9
        int randColor = rand.nextInt(2); //0 for red, 1 for blue
        color = "";
        if(randColor == 0) {
            color = "r";
        }
        else {
            color = "b";
        }
        String pic = "" + color + randNum;
        //return pic;
        ImageView target = (ImageView)findViewById(R.id.target);
        //target.setImageResource(R.drawable. + pic);
        target.setImageResource(getResources().getIdentifier(pic, "drawable", getPackageName()));
    }

    //will randomly decide between color and number
    public void generateCriteria() {
        Random rand = new Random();
        int r = rand.nextInt(2);
        criteria = "";
        if(r == 0) {
            criteria = "color";
        }
        else {
            criteria = "number";
        }
        TextView match = (TextView)findViewById(R.id.criteria);
        match.setText(criteria);

    }

    // FOLLOWING 2 METHODS DETERMINE IF ANSWER CORRECT AND UPDATE SCORE, IMAGE, AND CRITERIA

    //following method determines if user is correct
    public void answer() {
        if(criteria.equals("color")) {
            if(color.equals("r")) {
                //red and left
                if(direction.equals("left")) {
                    ++score;
                    displayScore();
                    //Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                }
                //red and right
                else if(direction.equals("right")) {
                    --score;
                    displayScore();
                    //Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Direction not set", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                //blue and left
                if(direction.equals("left")) {
                    --score;
                    displayScore();
                    //Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
                }
                //blue and right
                else if(direction.equals("right")) {
                    ++score;
                    displayScore();
                    //Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Direction not set", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(criteria.equals("number")) {
            switch(randNum)
            {
                case 0:
                case 2:
                case 4:
                case 6:
                case 8:
                {
                    //even and left
                    if(direction.equals("left")) {
                        --score; //not updating
                        displayScore();
                        //Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
                    }
                    //even and right
                    else if(direction.equals("right")) {
                        ++score; //not updating
                        displayScore();
                        //Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "Direction not set", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                case 1:
                case 3:
                case 5:
                case 7:
                case 9:
                {
                    //odd and right
                    if(direction.equals("right")) {
                        --score;
                        displayScore();
                        //Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
                    }
                    //odd and left
                    else if(direction.equals("left")) {
                        ++score;
                        displayScore();
                        //Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(this, "Direction not set", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }//end switch
        }
        else {
            //for testing
            Toast.makeText(this, "No criteria set", Toast.LENGTH_SHORT).show();
        }
    }

    public void displayScore() {
        TextView currScore = (TextView)findViewById(R.id.score);
        currScore.setText("Score: " + score); //update score in real time
        generateCriteria(); //generate new matching criteria
        generateNumberPic(); //generate new pic to match
    }

    public void resetScore() {
        score = 0;
        TextView currScore = (TextView)findViewById(R.id.score);
        currScore.setText("Score: " + score); //should go back to 0
    }
}
