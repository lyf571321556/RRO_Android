package ai.ones.components.utils;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;
/**
 * Created by admin on 2018/10/24.
 */
public class LinkMovementClickMethod extends LinkMovementMethod{

    private long lastClickTime;

    private static final long CLICK_DELAY = 500l;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    long temp = System.currentTimeMillis() - lastClickTime;
                    if(System.currentTimeMillis() - lastClickTime < CLICK_DELAY){
                        link[0].onClick(widget);
                        return true;
                    }
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                    lastClickTime = System.currentTimeMillis();
                }

            } else {
                Selection.removeSelection(buffer);
            }
        }
        return true;
    }

    public static LinkMovementClickMethod getInstance(){
        if(null == sInstance){
            sInstance = new LinkMovementClickMethod();
        }
        return sInstance;
    }

    private static LinkMovementClickMethod sInstance;

}
