package com.oss_net.choloeksathe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.oss_net.choloeksathe.R;

/**
 * Created by mahbubhasan on 12/24/17.
 */

public class CircularImageView extends NetworkImageView {
    Context context;
    public CircularImageView(Context context) {
        super(context);
        this.context = context;
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if(bm == null) return;
        setImageDrawable(new BitmapDrawable(context.getResources(), getCircularBitmap(bm)));
    }

    /**
     * Creates a circular bitmap and uses whichever dimension is smaller to determine the width
     * <br/>Also constrains the circle to the leftmost part of the image
     *
     * @param bitmap
     * @return bitmap
     */
    public Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        if(bitmap.getWidth()>bitmap.getHeight())
            width = bitmap.getHeight();
        // final int color = 0x808080;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, width);
        final RectF rectF = new RectF(rect);
        final float roundPx = width / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6.0f);
        paint.setColor(getResources().getColor(R.color.image_border));


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        return output;
    }

}
