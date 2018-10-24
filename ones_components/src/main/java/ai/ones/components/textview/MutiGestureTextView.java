package ai.ones.components.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import java.util.concurrent.atomic.AtomicBoolean;

//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
/**
 * 处理 textView 长按 单击 双击 高亮点击事件
 * Created by admin on 2018/10/24.
 */

public class MutiGestureTextView extends AppCompatTextView {


    private static final int DOUBLE_TAP_TIMEOUT = 200;

    private LocalLinkMovementMethod mMovmentMethod;

    public interface DoubleClickCallBack {
        void onDoubleClick(View v);
    }

    private DoubleClickCallBack doubleClickCallBack;

//    private Disposable mDisposable;

    public void setDoubleClickCallBack(DoubleClickCallBack doubleClickCallBack) {
        this.doubleClickCallBack = doubleClickCallBack;
    }

    public MutiGestureTextView(Context context) {
        super(context);
    }

    public MutiGestureTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MutiGestureTextView(
            Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private MotionEvent mPreviousUpEvent;

    private AtomicBoolean mBoolean = new AtomicBoolean(false);

    long lastClickTime = 0;
    long CLICK_DELAY = 500l;

    public void setYZJMovementMethod(LocalLinkMovementMethod movmentMethod) {
        this.mMovmentMethod = movmentMethod;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mBoolean.set(true);
        }
        if (getText() != null &&
                mMovmentMethod != null &&
                mMovmentMethod.onTouchEvent(this, new SpannableStringBuilder(getText()), event)) {
            //高亮点击事件消耗掉
            return true;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            if (doubleClickCallBack != null) {
                //监听双击
                if (mPreviousUpEvent != null
                        && isConsideredDoubleTap(mPreviousUpEvent, event)) {
                    //双击事件 消耗掉
                    mPreviousUpEvent = null;
                    cancelPerformClick();
                    mBoolean.set(false);
                    doubleClickCallBack.onDoubleClick(this);
                    lastClickTime = 0;
                    return true;
                }

                if (lastClickTime == 0) {
                    return true;
                }

                sendPerformClickDelay(DOUBLE_TAP_TIMEOUT);

                mPreviousUpEvent = MotionEvent.obtain(event);
            } else {
                if (lastClickTime == 0) {
                    return true;
                }
                //不监听双击
                if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {
                    realPerformClick();
                } else {
                    performLongClick();
                }
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            lastClickTime = System.currentTimeMillis();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (System.currentTimeMillis() - lastClickTime >= CLICK_DELAY) {
                performLongClick();
                mPreviousUpEvent = null;
                lastClickTime = 0;
            }

        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean performClick() {
        return false;
    }

    public boolean realPerformClick() {
        if (mBoolean.get()) {
            super.performClick();
            mBoolean.set(false);
        }
        return false;
    }

    @Override
    public boolean performLongClick() {
        if (mBoolean.get()) {
            super.performLongClick();
            mBoolean.set(false);
        }
        return false;
    }


    /**
     * 延迟 点击事件
     *
     * @param delay 延迟时间
     */
    private void sendPerformClickDelay(int delay) {
//        mDisposable = RxUtils.rxCreateOperationDelay(new ObservableOnSubscribe<Object>() {
//            @Override
//            public void subscribe(ObservableEmitter<Object> e) throws Exception {
//                e.onNext(new Object());
//                e.onComplete();
//            }
//        }, new Consumer<Object>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                realPerformClick();
//                mDisposable = null;
//            }
//        }, delay);
    }

    private void cancelPerformClick() {
//        if (mDisposable != null && !mDisposable.isDisposed()) {
//            mDisposable.dispose();
//            mDisposable = null;
//        }
    }

    private boolean isConsideredDoubleTap(MotionEvent firstUp, MotionEvent secondDown) {
        if (secondDown.getEventTime() - firstUp.getEventTime() > DOUBLE_TAP_TIMEOUT) {
            return false;
        }
        int deltaX = (int) firstUp.getX() - (int) secondDown.getX();
        int deltaY = (int) firstUp.getY() - (int) secondDown.getY();
        return deltaX * deltaX + deltaY * deltaY < 10000;
    }


    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;
        long lastClickTime = 0;
        long CLICK_DELAY = 500l;


        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LocalLinkMovementMethod();

            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget,
                                    Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if( action == MotionEvent.ACTION_DOWN ){
                lastClickTime = System.currentTimeMillis();
            }

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

                ClickableSpan[] link = buffer.getSpans(
                        off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {
                            if( widget instanceof MutiGestureTextView){
                                ((MutiGestureTextView)widget).realPerformClick();
                            }
                            link[0].onClick(widget);
                        } else {
                            widget.performLongClick();
                        }
                        return true;
                    } else {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }
                } else {
                    Selection.removeSelection(buffer);
                    return false;
                }
            }else if (action == MotionEvent.ACTION_MOVE) {
                if (System.currentTimeMillis() - lastClickTime >= CLICK_DELAY) {
                    widget.performLongClick();
                    lastClickTime = 0;
                    return true;
                }

            }

            return false;
        }
    }
}