package my.edu.utar.individualpracticalassignment.comparenumbers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import my.edu.utar.individualpracticalassignment.MainActivity;
import my.edu.utar.individualpracticalassignment.R;
import my.edu.utar.individualpracticalassignment.Visualizer;

public class CompareNumbers extends AppCompatActivity {
//Todos:
    //Fix the ui for the visualizer drawing (mandatory)
    //Make the ui more beautiful (optional)
    private Button  btnCard1, btnCard2;
    private TextView tvInstruction;
    private LinearLayout quizContainer;
    private Visualizer visualizer1;
    private Visualizer visualizer2;
    private TextView levelDisplay;
    private TextView TipTv;
    private boolean error;

    private int questionCount = 0;
    private final int totalQuestions = 6;
    private int correctAnswer;

    private LevelManager levelManager;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.compare_numbers);

      levelManager = new LevelManager();
      error = false;


      Button readyButton = findViewById(R.id.readyButton);
      Button btnBackHome = findViewById(R.id.btn_back_home);

      //Declarations for the quiz view
      btnCard1 = findViewById(R.id.btn_card1);
      btnCard2 = findViewById(R.id.btn_card2);
      TipTv = findViewById(R.id.tipTextView);
      quizContainer = findViewById(R.id.quiz_container);
      tvInstruction = findViewById(R.id.tv_instruction);
      visualizer1 = findViewById(R.id.visualizer1);
      visualizer2 = findViewById(R.id.visualizer2);
      levelDisplay = findViewById(R.id.progressDisplay);

      tvInstruction.setText("Ready?");
      fadeIn(tvInstruction);
      btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close the current activity
        });

      readyButton.setOnClickListener(v -> {
          TipTv.setText("🎂 count as 100 \n" +
                        "🍭 count as 10  \n" +
                        "🍬 count as 1   \n"
          );

         fadeOut(readyButton);
         fadeIn(TipTv);
         fadeIn(quizContainer);
         displayLevel();
         fadeIn(levelDisplay);



         generateNewQuestion();
      });

        // Number Card Clicks
        btnCard1.setOnClickListener(view -> checkAnswer(Integer.parseInt(btnCard1.getText().toString())));
        btnCard2.setOnClickListener(view -> checkAnswer(Integer.parseInt(btnCard2.getText().toString())));
    }
    private void displayLevel(){
        levelDisplay.setText("Level : "+levelManager.getLevel()
                +"\nQuestions Completed: "+this.questionCount
        );
    }
    public void generateNewQuestion(){
        if (questionCount >= totalQuestions) {
            tvInstruction.setText("Quiz Completed!");
            fadeOut(btnCard1);
            fadeOut(btnCard2);
            finish();
            return;
        }

        Random random = new Random();
        int num1 = random.nextInt(levelManager.getLargestBound());
        int num2 = random.nextInt(levelManager.getLargestBound());

        while (num1 == num2) {
            num2 = random.nextInt(levelManager.getLargestBound());
        }

        // Set correct answer
        correctAnswer = Math.max(num1, num2);

        // Randomize button placement
        if (random.nextBoolean()) {
            visualizer1.setNumber(num1);
            btnCard1.setText(String.valueOf(num1));

            visualizer2.setNumber(num2);
            btnCard2.setText(String.valueOf(num2));
        } else {
            visualizer1.setNumber(num2);
            btnCard1.setText(String.valueOf(num2));

            visualizer2.setNumber(num1);
            btnCard2.setText(String.valueOf(num1));
        }

        tvInstruction.setText(R.string.quiz_prompt);

        fadeIn(visualizer1);
        fadeIn(btnCard1);
        fadeIn(visualizer2);
        fadeIn(btnCard2);
        fadeIn(levelDisplay);

    }
    private void checkAnswer(int selectedNumber) {
        if (selectedNumber == correctAnswer) {
            questionCount++;

            showFeedbackPopup(tvInstruction, "Correct!", "Great job! 🎉"); // <- Add this
            if (questionCount == 2 || questionCount == 4) {
                levelManager.increaseLevelAndUpdateDifficulty();
            }
            fadeOutAndNext();
            displayLevel();
        }
        else{
            if (levelManager.getLevel() == 1) {
                showFeedbackPopup(tvInstruction, "Oops!", "🍬 means 1. Keep trying!");
            } else if (levelManager.getLevel() == 2) {
                showFeedbackPopup(tvInstruction, "Hint!", "🍭 = 10 🍬, 🍬 = 1 🍬.");
            } else {
                showFeedbackPopup(tvInstruction, "Hint!", "🎂 = 100 🍬, 🍭 = 10 🍬, 🍬 = 1 🍬.");
            }
            error = true;
        }
    }
    private void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    private void fadeOut(View view) {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }

    private void fadeOutAndNext() {
        fadeOut(btnCard1);
        fadeOut(btnCard2);
        fadeOut(visualizer1);
        fadeOut(visualizer2);
        fadeOut(findViewById(R.id.feedbacktv));
        if(error){
            error = false;
        }
        tvInstruction.postDelayed(this::generateNewQuestion, 500);
    }
    public void showFeedbackPopup(View anchorView, String title, String body) {
        // Inflate the layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.feedbackpopup, null);

        // Set title and body dynamically
        TextView titleText = dialogView.findViewById(R.id.popupTitle);
        TextView bodyText = dialogView.findViewById(R.id.popupBody);

        titleText.setText(title);
        bodyText.setText(body);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(true);  // Allows closing the dialog when clicked outside

        // Create and show the dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        // Auto-dismiss after 2 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if the activity is still valid before dismissing the dialog
            if (!isFinishing() && dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 1000);
    }

}
