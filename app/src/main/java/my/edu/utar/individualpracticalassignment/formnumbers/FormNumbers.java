package my.edu.utar.individualpracticalassignment.formnumbers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.utar.individualpracticalassignment.MainActivity;
import my.edu.utar.individualpracticalassignment.R;

public class FormNumbers extends AppCompatActivity implements ActivityFinishedDialog.OnDialogListener{

    private TextView targetNumber, totalValue;
    private LinearLayout dropArea, candyContainer;
    private Button resetButton;
    private Button backToHome;
    private Button restartButton;
    private TextView levelDisplay;
    private int[] priceList;
    private LevelManager levelManager;

    private int questionCount;
    private int TOTAL_QUESTIONS;
    private int requiredResult;
    private int currentSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_numbers);
        TOTAL_QUESTIONS = 8;

        questionCount = 0 ;
        levelManager = new LevelManager();
        targetNumber = findViewById(R.id.targetNumber);
        totalValue = findViewById(R.id.totalValue);
        dropArea = findViewById(R.id.dropArea);
        candyContainer = findViewById(R.id.candyContainer);
        resetButton = findViewById(R.id.resetButton);
        restartButton = findViewById(R.id.restartButton);
        backToHome = findViewById(R.id.btn_back_home);
        levelDisplay = findViewById(R.id.levelDisplay);

        candyContainer.getChildAt(0).setTag(getString(R.string.sweet));
        candyContainer.getChildAt(1).setTag(getString(R.string.lollipop));
        candyContainer.getChildAt(2).setTag(getString(R.string.cupcake));

        for (int i = 0; i < candyContainer.getChildCount(); i++) {
            View candy = candyContainer.getChildAt(i);
            candy.setOnLongClickListener(longClickListener);
        }

        dropArea.setOnDragListener(dragDropListener);
        resetButton.setOnClickListener(v -> resetActivity());
        restartButton.setOnClickListener(v -> restartActivity());

        backToHome.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        generateNewTarget();
        displayPriceList();
        displayLevel();
    }

    private void generateNewTarget() {
        requiredResult = levelManager.getTarget();
        targetNumber.setText("Target: $" + requiredResult);
        currentSum = 0;
        updateTotalValue();
    }

    private final View.OnLongClickListener longClickListener = view -> {
        String candyType = (String) view.getTag();
        int price = levelManager.getPriceOf(candyType);

        ClipData clipData = ClipData.newPlainText("priceValue", String.valueOf(price));
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDragAndDrop(clipData, shadowBuilder, null, 0);

        return true;
    };

    private final View.OnDragListener dragDropListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData clipData = event.getClipData();
                    if (clipData != null && clipData.getItemCount() > 0) {
                        String valueString = clipData.getItemAt(0).getText().toString();
                        int value = Integer.parseInt(valueString);
                        if(currentSum + value > requiredResult){
                            return false;
                        }

                        currentSum += value;
                        updateTotalValue();
                        checkResult();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        break;
                    }
                    break;
            }
            return true;
        }
    };

    private void updateTotalValue() {
        totalValue.setText("Total Value: " + currentSum);
    }

    private void displayPriceList() {
        TextView sweetPriceTv = findViewById(R.id.priceOfSweet);
        TextView lollipopPriceTv = findViewById(R.id.priceOfLollipop);
        TextView cupcakePriceTv = findViewById(R.id.priceOfCupcake);

        priceList = new int[3];
        priceList[0] = levelManager.getPriceOf("🍬");
        priceList[1] = levelManager.getPriceOf("🍭");
        priceList[2] = levelManager.getPriceOf("🎂");

        sweetPriceTv.setText("Price of \uD83C\uDF6C \n $" + priceList[0]);
        lollipopPriceTv.setText("Price of \uD83C\uDF6D \n $" + priceList[1]);
        cupcakePriceTv.setText("Price of \uD83C\uDF82 \n $" + priceList[2]);
    }
    private void displayLevel(){
        levelDisplay.setText("Level : "+levelManager.getLevel()
                +"\nQuestions Completed: "+this.questionCount
        );
    }
    private void checkResult() {
        if (currentSum == requiredResult) {
            questionCount++;
            if(questionCount == this.TOTAL_QUESTIONS){
                new ActivityFinishedDialog().show(getSupportFragmentManager(),
                        "FINISH_ACTIVITY");
            }
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            resetActivity();
            if(questionCount % 2 == 0 ) {
                levelManager.increaseLevel();
            }
            displayPriceList();
            generateNewTarget();
            displayLevel();
        }

    }
    private void restartActivity(){
        levelManager.reset();
        resetActivity();
        displayPriceList();
        this.questionCount = 0;
        displayLevel();
    }
    private void resetActivity() {
        this.currentSum = 0;
        generateNewTarget(); 
        displayPriceList();
        updateTotalValue();
    }

    @Override
    public void onDialogAccept() {
         restartActivity();
    }

    @Override
    public void onDialogReject() {
        this.finish();
    }

}
