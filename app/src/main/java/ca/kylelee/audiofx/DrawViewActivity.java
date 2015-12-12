package ca.kylelee.audiofx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Created by Kyle on 11/29/2015.
 */
public class DrawViewActivity extends View {

    Random rand = new Random();
    Paint paint = new Paint();


    // CONSTRUCTOR
    public DrawViewActivity(Context context) {
        super(context);
        setFocusable(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);




        for(int i =0; i<30; i++){

            int r =  rand.nextInt(256);
            int g =  rand.nextInt(256);
            int b =  rand.nextInt(256);

            paint.setAntiAlias(true);
            paint.setColor(Color.rgb(r, g, b));



            int posX = rand.nextInt(canvas.getWidth());
            int posY = rand.nextInt(canvas.getHeight());
            int radius = rand.nextInt(100) + 20;


            canvas.drawCircle(posX,posY,radius,paint);


            //Log.d("kyle",Integer.toString(posX));


        }
        //this.invalidate();
        //this.postInvalidate();
        this.postInvalidateDelayed(100);


    }

}
