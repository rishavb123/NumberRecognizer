package io.bhagat.numberrecognizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

public class DrawSurface extends SurfaceView implements Runnable {

    private SurfaceHolder holder;

    public static int screenWidth;
    public static int screenHeight;

    private Paint paint;
    private Paint font;
    private Paint boxPaint;
    private Paint bgPaint;

    private int guess;

    private ArrayList<Path> finishedPaths;
    private Path path;

    private Bitmap bitmap;

    public DrawSurface(Context context) {
        super(context);

        Display screenDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point sizeOfScreen = new Point();
        screenDisplay.getSize(sizeOfScreen);
        screenWidth=sizeOfScreen.x;
        screenHeight=sizeOfScreen.y;

        bitmap = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config.ARGB_8888);

        new Thread(this).start();

        holder = getHolder();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        font = new Paint();
        font.setTextSize(200);
        font.setColor(Color.RED);

        boxPaint = new Paint();
        boxPaint.setStrokeWidth(20);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setColor(Color.BLUE);

        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);
        bgPaint.setStyle(Paint.Style.FILL);

        finishedPaths = new ArrayList<>();
        path = new Path();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float touchX = event.getX();
                float touchY = event.getY();

                int action = event.getAction();

                if(path.isEmpty())
                    action = MotionEvent.ACTION_DOWN;

                if(touchY > screenWidth) {
                    if(action == MotionEvent.ACTION_DOWN) {
                        finishedPaths = new ArrayList<>();
                    }
                    action = MotionEvent.ACTION_UP;
                }

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        path.moveTo(touchX, touchY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        path.lineTo(touchX, touchY);
                        break;
                    case MotionEvent.ACTION_UP:
                        finishedPaths.add(new Path(path));
                        path.reset();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }

    public void makeGuess()
    {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, false);

        double[] input = new double[784];
        boolean send = false;

        for(int i = 0; i < 28; i++)
            for(int j = 0; j < 28; j++)
            {
                input[28*i + j] = 1 - Color.red(scaledBitmap.getPixel(j, i)) / 255.0;
                if(input[28 * i + j] != 0)
                    send = true;
            }

        if(send)
            ((MainActivity) getContext()).send(input);
    }

    @Override
    public void run() {
        while (true){
            if (!holder.getSurface().isValid())
                continue;
            Canvas canvas = holder.lockCanvas();
            Canvas bitmapCanvas = new Canvas(bitmap);

            canvas.drawRGB(255, 255,255);
            float outsideWidth = 0.2f;
            float outsideHeight = 0.15f;
            canvas.drawRect(screenWidth * outsideWidth, screenWidth * outsideHeight, screenWidth * (1 - outsideWidth), screenWidth * (1 - outsideHeight), boxPaint);
            canvas.drawPath(path, paint);

            bitmapCanvas.drawRGB(255, 255,255);
            bitmapCanvas.drawPath(path, paint);
            for (int i = 0; i < finishedPaths.size(); i++) {
                try {
                    canvas.drawPath(finishedPaths.get(i), paint);
                    bitmapCanvas.drawPath(finishedPaths.get(i), paint);
                } catch (IndexOutOfBoundsException ignored) { }
            }
            makeGuess();

            canvas.drawRect(0f, (float)screenWidth, (float)screenWidth, (float)screenHeight, bgPaint);
            canvas.drawText(Integer.toString(guess), (float) screenWidth / 2f - 100f, screenWidth + 300f, font);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

}
