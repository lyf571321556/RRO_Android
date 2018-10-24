package ai.ones.components.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import ai.ones.utils.dimens.DimensUtils;

/**
 * @author admin on 2018/10/24.
 */

public class TextDrawable extends Drawable {
    private Context mContext;
    private final String text;
    private final Paint paint;

    public TextDrawable(Context context,String text, int color) {
        this.mContext=context;
        this.text = text;
        this.paint = new Paint();
        paint.setColor(color);
//        paint.setTextSize(context.getResources().
//                getDimensionPixelSize(R.dimen.common_font_fs2));
        paint.setAntiAlias(true);
//        paint.setFakeBoldText(true);
//        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawText(text,
                0,
                (bounds.centerY() / 2) - ((paint.descent() + paint.ascent()) / 2) + DimensUtils.dip2px(mContext, 3),
                paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
