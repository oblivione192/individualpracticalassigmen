package my.edu.utar.individualpracticalassignment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class TopBarView extends View {
    private Bitmap topBarBitmap;
    private Paint paint;

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topBarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.topbar);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int newWidth = (int) (topBarBitmap.getWidth() * 0.75);
        int newHeight = (int) (topBarBitmap.getHeight() * 0.75);

        // Create a smaller scaled bitmap
        @SuppressLint("DrawAllocation") Bitmap scaledBitmap = Bitmap.createScaledBitmap(topBarBitmap, newWidth, newHeight, true);

        // Draw the scaled bitmap
        canvas.drawBitmap(scaledBitmap, 0, 0, paint);
    }
    }

