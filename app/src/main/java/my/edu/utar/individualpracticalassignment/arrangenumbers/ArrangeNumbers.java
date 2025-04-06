package my.edu.utar.individualpracticalassignment.arrangenumbers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import my.edu.utar.individualpracticalassignment.R;

public class ArrangeNumbers extends AppCompatActivity {
    private LinearLayout numberContainer, slotContainer;
    private TextView instructionTv;
    private LevelManager levelManager;
    private Button btn_back_home;
    private int TOTAL_QUESTIONS = 6;
    private TextView levelDisplay;
    private int[] answer;
    private int[] question;
    private int questionCount = 0, slotsFilled = 0;


    private TextView feedbackTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrangenumbers);

        setupUI();
        initializeGame();
        setupEventListeners();

    }
    private void displayLevel(){
        levelDisplay.setText("Level : "+levelManager.getLevel()
                +"\nQuestions Completed: "+this.questionCount
        );
    }
    private Button createButton() {
        Button labelBt = new Button(ArrangeNumbers.this);  // 'this' refers to the Context (Activity)

        int totalSlots = levelManager.getTotalSlots();
        int buttonSize = 180; // Default size for buttons when slots are less than 7
        int textSize = 18;
        // Scale button size if there are 7 or more slots
        if (totalSlots >= 7) {
            buttonSize = 150; // Scale down button size for 7 or more slots
            textSize = 15;
        }

        // Set the button's layout parameters (Width & Height)
        labelBt.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize)); // Set width & height in pixels

        // Set button text properties
        labelBt.setTextColor(Color.WHITE);
        labelBt.setTextSize(textSize);
        labelBt.setTypeface(null, Typeface.BOLD); // Set text style to bold
        labelBt.setBackgroundTintList(ContextCompat.getColorStateList(ArrangeNumbers.this, R.color.orange));
        labelBt.setOnLongClickListener(longClickListener);

        return labelBt;
    }

    /** Initialize UI components */
    private void setupUI() {
        levelManager = new LevelManager();
        numberContainer = findViewById(R.id.numberContainer);
        slotContainer = findViewById(R.id.slotContainer);
        feedbackTv= findViewById(R.id.feedback);
        levelDisplay = findViewById(R.id.levelDisplay);
        instructionTv= findViewById(R.id.instructionText);
        btn_back_home = findViewById(R.id.btn_back_home);
        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /** Setup event listeners for drag and drop */
    private void setupEventListeners() {
        for (int i = 0; i < slotContainer.getChildCount(); i++) {
            View slot = slotContainer.getChildAt(i);
            slot.setOnDragListener(dragDropListenerSortArea);
            slot.setOnLongClickListener(longClickListener);
        }

        numberContainer.setOnDragListener(dragDropListenerNumberContainer);
    }

    /** Start or reset the game */
    private void initializeGame() {
        resetGame();
    }

    /** Reset game for the next question */
    private void resetGame() {
        slotsFilled = 0;
        updateSlots();
        getQuestion();
        setupEventListeners();
        displayLevel();
        instructionTv.setText("Drag the numbers to the blue boxes in "+levelManager.getSortMode()+" order");
    }

    /** Dynamically update slots based on the level */
    private void updateSlots() {
        slotContainer.removeAllViews(); // Clear previous slots

        int totalSlots = levelManager.getTotalSlots();
        for (int i = 0; i < totalSlots; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
            layoutParams.setMargins(8, 8, 8, 8);


            LinearLayout slot = new LinearLayout(this);
            slot.setLayoutParams(layoutParams);
            slot.setBackgroundResource(R.drawable.bordershape_blue); // Add a drawable background if needed
            slot.setOnDragListener(dragDropListenerSortArea); // Set drag event
            slot.setGravity(Gravity.CENTER);
            slotContainer.addView(slot);
        }
        for(int i = 0; i< totalSlots; i++){

            Button labelBt = createButton();
            numberContainer.addView(labelBt);
        }
    }
    private void restartQuestion(){
        slotsFilled = 0;
        for(int i =0;i < slotContainer.getChildCount(); i++){
            LinearLayout slot = (LinearLayout)slotContainer.getChildAt(i);
            slot.removeAllViews();
        }
        for(int i = 0; i < levelManager.getTotalSlots() ; i++){
           Button labelBt = createButton();
           labelBt.setText(String.valueOf(question[i]));
           numberContainer.addView(labelBt);
        }
    }



    /** Get a new question and update the number slots */
    private void getQuestion() {
        question = levelManager.getNumArray();
        int count = Math.min(numberContainer.getChildCount(), question.length);

        for (int i = 0; i < count; i++) {
            Button numberBt = (Button) numberContainer.getChildAt(i);
            numberBt.setText(String.valueOf(question[i]));
        }
    }

    /** Get the current answer from slots */
    private void getAnswer() {
        answer = new int[slotContainer.getChildCount()];
        for (int i = 0; i < slotContainer.getChildCount(); i++) {
            LinearLayout slot = (LinearLayout) slotContainer.getChildAt(i);
            View  v = slot.getChildAt(0);
            if(v instanceof TextView){
                TextView tv = (TextView) v;
                answer[i] = Integer.parseInt(tv.getText().toString());
                Log.d("Answer" + i, String.valueOf(answer[i]));
            }
        }
    }

    /** Check progress after every question */
    private void checkProgress() {
        getAnswer();
        if (levelManager.questionCompleted(answer)) {
            questionCount++;
            if (questionCount >= TOTAL_QUESTIONS) {
                finish();
            } else {
                showFeedbackPopup( "Correct!", "Great job! 🎉",-1);
                levelManager.increaseLevel();
                resetGame();
            }
        }
        else{
            if(Objects.equals(levelManager.getSortMode(), "Ascending")) {
                showFeedbackPopup("Oops!", "Its okay. Hint: Ascending means smallest from the left biggest to the right",2000);
            }
            else{
                showFeedbackPopup("Oops!","Its okay. Hint: Descending means biggest from the left smallest to the right",2000);
            }
          restartQuestion();
        }
    }

    /** Drag listeners */
    private final View.OnDragListener dragDropListenerSortArea = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();
                    if (draggedView instanceof Button) {

                        //Checks if the slot is occupied. Rejects drop if it is already
                        //occupied
                        if((((LinearLayout)v).getChildCount()>0)){
                            return false;
                        }

                        // Remove from its original parent
                        ViewGroup parent = (ViewGroup) draggedView.getParent();
                        if (parent != null) {
                            parent.removeView(draggedView);
                        }



                        // Add the button to the slotContainer
                        TextView tv = new TextView(ArrangeNumbers.this);
                        Button draggedItem = (Button) draggedView;

                        tv.setText(String.valueOf(draggedItem.getText()));
                        tv.setTextSize(16);

                        ((LinearLayout) v).addView(tv);
                        draggedView.setVisibility(View.VISIBLE);
                        slotsFilled++;
                        Log.d("Slots filled",String.valueOf(slotsFilled));
                        if (slotsFilled == levelManager.getTotalSlots()) {
                            Log.d("Question Attempted","Yes");
                            checkProgress();
                        }
                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    draggedView = (View) event.getLocalState();
                    draggedView.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    };


    private final View.OnDragListener dragDropListenerNumberContainer = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DROP:
                    View draggedView = (View) event.getLocalState();

                    Log.d("Slots filled",String.valueOf(slotsFilled));
                    if (draggedView instanceof LinearLayout) {
                        // Remove from its current parent
                        LinearLayout ll = (LinearLayout) draggedView;
                        if(ll.getChildCount() == 0){
                            return false;
                        }

                        slotsFilled--;




                        TextView label = (TextView)ll.getChildAt(0);

                        Button labelBt = createButton();

                        Log.d("Value of inner text view of linear layout",String.valueOf(label.getText()));
                        labelBt.setText(String.valueOf(label.getText()));
                        ((LinearLayout)v).addView(labelBt);
                        ll.removeAllViews();
                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    draggedView = (View) event.getLocalState();
                    draggedView.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    };

    private final View.OnLongClickListener longClickListener = view -> {

        if(view instanceof LinearLayout){
            if(((LinearLayout)view).getChildCount() == 0){
                return false;
            }
        }

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDragAndDrop(null, shadowBuilder, view, 0);
        return true;
    };

    public void showFeedbackPopup( String title, String body, int dismissDuration) {
        int popupDuration = 0;
        if(dismissDuration == -1){ popupDuration = 1500;}
        else{popupDuration = dismissDuration;}
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
        }, popupDuration);
    }
}
