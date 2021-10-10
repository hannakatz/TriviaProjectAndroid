package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

public class GameByCategoryActivity extends AppCompatActivity {
    private int presCounter = 0;
    private int maxPresCounter;
    private String[] keys = {"R", "I", "B", "D", "X", "X", "X", "X", "X", "X", "X"};
    private String textAnswer = "BIRD";
    private Player player;
    TextView  textQuestion, textTitle, setScore;
    Animation animation, shakeAnimation, aniFade;
    private static int score = 0;
    private static int lives = 3;
    private static int counterProgress = 0;
    private static boolean newGame = true;
    private static boolean animationRunning = false;
    private ImageView live1, live2, live3 , hurryUp;
    private Button btnClear;
    ProgressBar progressBar;
    CountDownTimer mCountDownTimer;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";
    private Questions questionGet = new Questions("","","");
    ArrayList<String> keyss = new ArrayList<>();
    private SQLiteDatabase database;
    final String TABLE_NAME = "Trivia_Questions";
    Cursor myCursor;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_category);

        Intent intent = getIntent();
        category = intent.getExtras().getString("Category");
        GetQuestionCategory(category);
        maxPresCounter = questionGet.getAnswer().length();
        getkeyssArray(questionGet.getAnswer());

        LinearLayout linearLayout1 = findViewById(R.id.layout_parent1);
        LinearLayout linearLayout2 = findViewById(R.id.layout_parent2);
        LinearLayout linearLayout3 = findViewById(R.id.layout_parent3);
        EditText editText = findViewById(R.id.edit_text);

        animation = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);

        progressBar = findViewById(R.id.progressbar);
        btnClear = findViewById(R.id.clear_btn2);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                linearLayout1.removeAllViews();
                linearLayout2.removeAllViews();
                linearLayout3.removeAllViews();
                presCounter = 0;
                setLetters();
            }
        });
        live1 = findViewById(R.id.live_1);
        live2 = findViewById(R.id.live_2);
        live3 = findViewById(R.id.live_3);
        hurryUp = findViewById(R.id.hurry_up);

        setScore = findViewById(R.id.text_count);


        mCountDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counterProgress++;

                if(counterProgress > 22 && counterProgress < 25){
                    Drawable progressDrawableRed = getResources().getDrawable(R.drawable.progress_red);
                    progressBar.setProgressDrawable(progressDrawableRed);
                    hurryUp.startAnimation(shakeAnimation);
                    animationRunning = true;

                }
                if(counterProgress > 25 && animationRunning == true){
                    hurryUp.startAnimation(aniFade);
                    animationRunning = false;

                }
                if (newGame == true) {
                    if(counterProgress < 25 && counterProgress > 22){
                        hurryUp.startAnimation(aniFade);
                    }
                    counterProgress = 0;
                    progressBar.setProgress(0);
                    Drawable progressDrawableRed = getResources().getDrawable(R.drawable.progress_green);
                    progressBar.setProgressDrawable(progressDrawableRed);
                    newGame = false;
                    animationRunning = false;
                    onFinishCorrect();
                }
                progressBar.setProgress((int)counterProgress*100/(30000/1000));
            }
            @Override
            public void onFinish() {
                setHurt();
                editText.setText("");
                linearLayout1.removeAllViews();
                linearLayout2.removeAllViews();
                linearLayout3.removeAllViews();

                GetQuestion();
                maxPresCounter = questionGet.getAnswer().length();
                getkeyssArray(questionGet.getAnswer());
                textQuestion.setText(questionGet.getQuestion());
                counterProgress = 0;

                setLetters();
                newGame = false;
                animationRunning = false;
                mCountDownTimer.start();
            }
            public void onFinishCorrect(){
                editText.setText("");
                linearLayout1.removeAllViews();
                linearLayout2.removeAllViews();
                linearLayout3.removeAllViews();

                GetQuestion();
                maxPresCounter = questionGet.getAnswer().length();
                getkeyssArray(questionGet.getAnswer());
                textQuestion.setText(questionGet.getQuestion());
                counterProgress = 0;

                setLetters();
            }


        };
        mCountDownTimer.start();

        setLetters();

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = credentials.getString("Player","");
        player = gson.fromJson(json,Player.class);

    }

    private ArrayList<String> shuffleArray(ArrayList<String> array){
        Random random = new Random();
        for(int i = array.size()-1 ; i>0 ; i--){
            int index = random.nextInt(i+1);
            String a = array.get(index);
            array.set(index, array.get(i));
            array.set(i,a);
        }
        return array;
    }
    private void addView(LinearLayout viewParent, final String text, final EditText editText){
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutParams.rightMargin = 15;
        final TextView textView = new TextView(this);
        textView.setLayoutParams(linearLayoutParams);
        textView.setBackgroundResource(R.drawable.bgpink);
        textView.setTextColor(this.getResources().getColor(R.color.purple));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextSize(32);
        textView.setClickable(true);
        textView.setFocusable(true);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/FredokaOneRegular.ttf");
        textQuestion = findViewById(R.id.text_question);
        textQuestion.setText(questionGet.getQuestion());
        textTitle = findViewById(R.id.text_title);
        textTitle.setText(category);

        textQuestion.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        editText.setTypeface(typeface);
        textView.setTypeface(typeface);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presCounter < maxPresCounter) {
                    if (presCounter == 0) {
                        editText.setText("");
                    }
                    editText.setText(editText.getText().toString() + text);
                    textView.startAnimation(animation);
                    textView.animate().alpha(0).setDuration(300);
                    presCounter++;
                    //viewParent.removeView(textView);
                    textView.setVisibility(View.INVISIBLE);
                    //viewParent.addView(textView);

                    if (presCounter == maxPresCounter) {
                        doValidate();
                    }

                }
            }
        });
        viewParent.addView(textView);
    }

    private void doValidate(){
        presCounter = 0;
        EditText editText = findViewById(R.id.edit_text);
        LinearLayout linearLayout1 = findViewById(R.id.layout_parent1);
        LinearLayout linearLayout2 = findViewById(R.id.layout_parent2);
        LinearLayout linearLayout3 = findViewById(R.id.layout_parent3);

        if(editText.getText().toString().equals(questionGet.getAnswer())) {
            Toast.makeText(GameByCategoryActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            newGame = true;
            score++;

            mCountDownTimer.cancel();

            if (score == 10) {
                setScore();
                player.setHighScore(score);
                Intent intent = new Intent(GameByCategoryActivity.this, WinActivity.class);
                startActivity(intent);
                onDestroy();
            }
            String scoreString = Integer.toString(score);
            setScore.setText(scoreString);
            mCountDownTimer.start();
        }
        else {
            Toast.makeText(GameByCategoryActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
            editText.setText("");
            linearLayout1.removeAllViews();
            linearLayout2.removeAllViews();
            linearLayout3.removeAllViews();
        }
        setLetters();



    }

    private void setScore(){
        player.playerScores.add(new PlayerScore(score, "By Category", player.getUserName()));

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = credentials.edit();
        Gson gson = new Gson();
        String json = gson.toJson(player);
        editor.putString("Player",json);
        editor.commit();
    }

    private void setHurt(){
        if(lives == 3){
            live3.setBackgroundResource(R.color.transparent);
        }
        if(lives == 2){
            live2.setBackgroundResource(R.color.transparent);
        }
        if(lives == 1){
            player.setHighScore(score);
            live1.setBackgroundResource(R.color.transparent);
            newGame = true;
            setScore();
            Intent intent = new Intent(GameByCategoryActivity.this, GameOverActivity.class);
            startActivity(intent);
            onDestroy();
        }
        lives--;
    }

    private void setLetters(){
        int x = 0;
        keyss = shuffleArray(keyss);
        for(String key : keyss){

            if(x < 6){
                addView(((LinearLayout)findViewById(R.id.layout_parent1)), key, ((EditText) findViewById(R.id.edit_text)));
            }
            else if(x > 5 && x < 12){
                addView(((LinearLayout)findViewById(R.id.layout_parent2)), key, ((EditText) findViewById(R.id.edit_text)));
            }
            else {
                addView(((LinearLayout)findViewById(R.id.layout_parent3)), key, ((EditText) findViewById(R.id.edit_text)));
            }
            x++;
        }
        maxPresCounter = questionGet.getAnswer().length();
    }
    // Copy character by character from String to ArrayList - Set's the Letters for the optional answer selection
    private void getkeyssArray(String answer)
    {
        keyss.clear();
        for (int i = 0; i < answer.length(); i++) {
            keyss.add(String.valueOf(answer.charAt(i)));
        }

    }
    //Get a question that hasn't been asked.
    //Function returns a question object, and updates the database on the row of the specific returned question that it has been asked.
    private void GetQuestion() {
        String notAsked = "n";
        database = openOrCreateDatabase("TriviaQ", MODE_PRIVATE,null);
        myCursor = database.query(TABLE_NAME,new String[] {"id","category","question","answer","asked"},"asked LIKE '"+notAsked+"'",null,null,null,null,null);
        int categoryIndex = myCursor.getColumnIndex("category");
        int questionIndex = myCursor.getColumnIndex("question");
        int answerIndex = myCursor.getColumnIndex("answer");
        int questionID = myCursor.getColumnIndex("id");
        myCursor.moveToNext();
        questionGet.setQuestion(myCursor.getString(questionIndex));
        questionGet.setAnswer(myCursor.getString(answerIndex));
        questionGet.setCategory(myCursor.getString(categoryIndex));
        UpdateQuestionWasAsked(myCursor.getString(questionID));
    }

    //Update that a question has been asked.
    private void UpdateQuestionWasAsked(String questionID) {
        ContentValues contentVal = new ContentValues();
        contentVal.put("asked", "y");
        database.update(TABLE_NAME,contentVal,"Id" + " = "+questionID , null);
    }

    //Get a question that hasn't been asked, from a specific category.
    //Function returns a question object, and updates the database on the row of the specific returned question that it has been asked.
    private void GetQuestionCategory(String category) {
        String notAsked = "n";
        database = openOrCreateDatabase("TriviaQ", MODE_PRIVATE,null);
        myCursor = database.query(TABLE_NAME,new String[] {"id","category","question","answer","asked"},"asked LIKE '"+notAsked+"' and category LIKE '"+category+"'",null,null,null,null,null);
        int categoryIndex = myCursor.getColumnIndex("category");
        int questionIndex = myCursor.getColumnIndex("question");
        int answerIndex = myCursor.getColumnIndex("answer");
        int questionID = myCursor.getColumnIndex("id");
        myCursor.moveToNext();
        questionGet.setQuestion(myCursor.getString(questionIndex));
        questionGet.setAnswer(myCursor.getString(answerIndex));
        questionGet.setCategory(myCursor.getString(categoryIndex));
        UpdateQuestionWasAsked(myCursor.getString(questionID));
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit game")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(GameByCategoryActivity.this, MainActivity.class);
                        startActivity(intent);
                        onDestroy();
                    }
                }).create().show();
    }
}

