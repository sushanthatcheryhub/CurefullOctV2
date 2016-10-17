package utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Sushant Hatcheryhub on 29-09-2016.
 */

public class RoundImageCircle extends ImageView {

    private int strokeColor = Color.WHITE;

    public RoundImageCircle(Context context) {
        super(context);
        inItUi(context, null);
    }

    /**
     * @param context Context
     * @param attrs   AttributeSet
     */
    public RoundImageCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        inItUi(context, attrs);
        // paintBorder = new Paint();
        // paintBorder.setAntiAlias(true);

    }

    /**
     * @param context  Context
     * @param attrs    AttributeSet
     * @param defStyle integer
     */
    public RoundImageCircle(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        inItUi(context, attrs);
        // paintBorder = new Paint();
        // paintBorder.setAntiAlias(true);
    }

    private void inItUi(Context context, AttributeSet attrs) {
        if (attrs != null) {
            // TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
            // R.styleable.RoundedImageView, 0, 0);

            try {
                // strokeColor = a
                // .getInteger(
                // R.styleable.RoundedImageView_RoundedImageView_strokecolor,
                // Color.WHITE);
                // clickable = a.getBoolean(
                // R.styleable.RoundedImageView_RoundedImageView_clickable,
                // true);
            } finally {
                // a.recycle();
            }
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paintH.setColor(strokeColor);
    }

    // this is the radius of the circle we are drawing
    int radius = 6;

    // this is the paint object which specifies the color and alpha level
    // of the circle we draw

    /**
     * @param color integer color
     */
    public void setStrokeColor(int color) {
        this.strokeColor = color;
        paintH.setColor(strokeColor);
        this.invalidate();
    }

    boolean drawGlow = false;
    private Paint paintS = new Paint();

    {
        paintS.setAntiAlias(true);
        // paint.setStyle(Style.STROKE);
        // paintS.setColor(0xFFBAB399);
        paintS.setColor(Color.RED);
    }

    ;
    private Paint paintH = new Paint();

    {
        paintH.setAntiAlias(true);
        // paint.setStyle(Style.STROKE);
        paintH.setColor(strokeColor);
        // paintH.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // paintH.setColor(Color.RED);
    }

    ;

    /*
     * @Override public void draw(Canvas canvas){ super.draw(canvas);
     * if(drawGlow){ canvas.drawCircle(glowX, glowY, radius, paint); } }
     */
    public void invalidateView() {
        this.invalidate();
    }

    private Paint paint;
    // private Paint paintBorder;
    private BitmapShader shader;

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap image = getCroppedBitmap(bitmap);
        Bitmap roundBitmap = Bitmap.createScaledBitmap(image, getWidth(),
                getHeight(), true);
        /*
		 * { canvas.drawCircle(roundBitmap.getWidth() / 2,
		 * roundBitmap.getWidth() / 2, roundBitmap.getWidth() / 2, paintH); }
		 * final Rect rect = canvas.getClipBounds(); final Rect rect1 = new
		 * Rect(radius - padding, radius - padding, roundBitmap.getWidth(),
		 * roundBitmap.getHeight()); canvas.drawBitmap(roundBitmap, rect, rect1,
		 * null);
		 */

        if (roundBitmap != null) {
            shader = new BitmapShader(Bitmap.createScaledBitmap(roundBitmap,
                    roundBitmap.getWidth(), roundBitmap.getHeight(), false),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            int circleCenter = roundBitmap.getWidth() / 2;

            // circleCenter is the x or y of the view's center
            // radius is the radius in pixels of the cirle to be drawn
            // paint contains the shader that will texture the shape
			/*
			 * canvas.drawCircle(circleCenter, circleCenter, circleCenter,
			 * paintBorder);
			 */
            canvas.drawCircle(circleCenter, circleCenter, circleCenter, paint);
        }
        // this.invalidate();
    }

    /**
     * @param bmp Bitmap
     * @author Wild Coder
     */
    public Bitmap getCroppedBitmap(Bitmap bmp) {
        Bitmap sbmp;
        // if(this.getHeight()<radiuss || this.getWidth()<radiuss){
        // sbmp = Bitmap.createScaledBitmap(bmp, this.getWidth() - radius,
        // this.getHeight()
        // - radius, true);
        // }else
        {
            sbmp = Bitmap.createScaledBitmap(bmp, this.getWidth() - radius,
                    this.getWidth() - radius, true);
        }

        sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f - radius / 2,
                sbmp.getHeight() / 2 + 0.7f - radius / 2, sbmp.getWidth() / 2
                        + 0.1f - radius / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }
}

