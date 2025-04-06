package my.edu.utar.individualpracticalassignment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Visualizer extends View {
    private int number;
    private final List<String> emojiList;
    private final Paint textPaint;

    public Visualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        emojiList = new ArrayList<>();
        textPaint = new Paint();
        textPaint.setTextSize(80);  // Set emoji size
        textPaint.setAntiAlias(true);
    }

    public void setNumber(int number) {
        this.number = number;
        generateEmojiList();
        invalidate(); // Triggers a redraw
    }

    private void generateEmojiList() {
        emojiList.clear();

        int hundreds = number / 100;
        int tens = (number % 100) / 10;
        int ones = number % 10;

        // Add Cupcakes (Hundreds)
        for (int i = 0; i < hundreds; i++) {
            emojiList.add("🎂");
        }

        // Add Lollipops (Tens)
        for (int i = 0; i < tens; i++) {
            emojiList.add("🍭");
        }
        // Add Candies (Ones)
        for (int i = 0; i < ones; i++) {
            emojiList.add("🍬");
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int x = 50, y = 100; // Starting position
        int spacing = 100;   // Space between emojis

        for (String emoji : emojiList) {
            canvas.drawText(emoji, x, y, textPaint);
            x += spacing;   // Move right
            if (x > getWidth() - 50) { // If end of screen, move down
                x = 50;
                y += spacing;
            }
        }
    }
}
