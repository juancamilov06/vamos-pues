package co.vamospues.vamospues.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Manuela Duque M on 11/03/2017.
 */

public class GillSansLightTextView extends TextView {

    public GillSansLightTextView(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GillSans-Light.ttf");
        this.setTypeface(face);
    }

    public GillSansLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GillSans-Light.ttf");
        this.setTypeface(face);
    }

    public GillSansLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GillSans-Light.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
