package ai.ones.components.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.ones.components.R;
import ai.ones.components.span.HightLightTaskClickSpan;
import ai.ones.components.textview.MutiGestureTextView;
import ai.ones.utils.pattern.WebPattern;
/**
 * @author admin on 2018/10/24
 *
 */
public class SpannableStringUtil {

    //匹配@+名称
    public static final Pattern NAME_MATCHER = Pattern.compile(
            "@[^\\s\\$\\^\\[\\]\\?:!#%&=;@'\"<>\f$，；：“”。！、？]+", Pattern.CASE_INSENSITIVE);
    // 匹配@+人名中的人名 group(1)
    public static final Pattern ONLY_NAME_MATCHER = Pattern.compile(
            "@([^\\s\\$\\^\\[\\]\\?:!#%&=;@'\"<>\f$，；：“”。！、？]+)", Pattern.CASE_INSENSITIVE);


    public static void makeHigthLightTaskText(Activity act, TextView textView, SpannableString spannableString, String TASK_MATCHERS, HightLightTaskClickSpan.HightLightTaskClickListener listener) {
        makeHigthLightTaskText(act, textView, spannableString, TASK_MATCHERS, listener, R.color.high_text_color);
    }

    public static void makeHigthLightTaskText(Activity act, TextView textView, SpannableString spannableString, HightLightTaskClickSpan.HightLightTaskClickListener nameListner, String TASK_MATCHERS, HightLightTaskClickSpan.HightLightTaskClickListener listener, int textColorTask, int textcolorLink, int textcolorName) {
        makeHigthLightTaskText(act, textView, spannableString, nameListner, TASK_MATCHERS, listener, textColorTask, textcolorLink, textcolorName, true, false);
    }

    public static void makeHigthLightTaskText(Activity act, TextView textView, SpannableString spannableString, HightLightTaskClickSpan.HightLightTaskClickListener nameListner, String TASK_MATCHERS, HightLightTaskClickSpan.HightLightTaskClickListener listener, int textColor) {
        makeHigthLightTaskText(act, textView, spannableString, nameListner, TASK_MATCHERS, listener, textColor, textColor, textColor, true);
    }

    public static void makeHigthLightTaskText(Activity act, TextView textView, SpannableString spannableString, HightLightTaskClickSpan.HightLightTaskClickListener nameListner, String TASK_MATCHERS, HightLightTaskClickSpan.HightLightTaskClickListener listener, int textColorTask, int textcolorLink, int textcolorName, boolean isSafeMode) {
        makeHigthLightTaskText(act, textView, spannableString, nameListner, TASK_MATCHERS, listener, textColorTask, textcolorLink, textcolorName, true, isSafeMode);
    }

    public static void makeHigthLightTaskText(final Activity act, TextView textView, SpannableString spannableString, HightLightTaskClickSpan.HightLightTaskClickListener nameListner,
                                              String TASK_MATCHERS, HightLightTaskClickSpan.HightLightTaskClickListener listener,
                                              int textColorTask, int textcolorLink, int textcolorName,
                                              boolean isMatchName, final boolean isSafeMode) {
        if (textView == null || spannableString == null) {
            return;
        }
        final Context ctx = act;
        String matcherStr = "";
        SpannableStringBuilder style = new SpannableStringBuilder(spannableString);
        boolean isFind = false;
        if (!TextUtils.isEmpty(TASK_MATCHERS)) {
            Pattern p = Pattern.compile(TASK_MATCHERS);
            Matcher matcher = p.matcher(spannableString);
            while (matcher.find()) {
                isFind = true;
                style.setSpan(new HightLightTaskClickSpan(ctx,matcher.group(), textView.getContext().getResources().getColor(textColorTask), listener, false),
                        matcher.start(), matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (isMatchName) {
            Matcher matcherAtName = NAME_MATCHER.matcher(spannableString);
            while (matcherAtName.find()) {
                isFind = true;
                style.setSpan(new HightLightTaskClickSpan(ctx,matcherAtName.group(), textView.getContext().getResources().getColor(textcolorName), nameListner, false),
                        matcherAtName.start(), matcherAtName.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        Matcher matcherAtURL = WebPattern.WEB_URL.matcher(spannableString);
        while (matcherAtURL.find()) {
            matcherStr = matcherAtURL.group();
            if (matcherStr.toUpperCase().startsWith("HTTP://") || matcherStr.toUpperCase().startsWith("HTTPS://")) {
                isFind = true;
                style.setSpan(new HightLightTaskClickSpan(ctx,matcherAtURL.group(), textView.getContext().getResources().getColor(textcolorLink), new HightLightTaskClickSpan.HightLightTaskClickListener() {
                            @Override
                            public void onClick(String text) {
                                if (ctx == null) {
                                    return;
                                }
                                //do something
                            }
                        }, true),
                        matcherAtURL.start(), matcherAtURL.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

        Matcher matcherAtPhone = WebPattern.MOBILE_PHONE.matcher(spannableString);
        while (matcherAtPhone.find()) {
            final String myPhone = matcherAtPhone.group();
            isFind = true;
            style.setSpan(new HightLightTaskClickSpan(ctx,matcherAtPhone.group(), textView.getContext().getResources().getColor(textcolorLink), new HightLightTaskClickSpan.HightLightTaskClickListener() {
                        @Override
                        public void onClick(String text) {
                            if (act == null) {
                                return;
                            }
                            //phone for something
                        }
                    }, true),
                    matcherAtPhone.start(), matcherAtPhone.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        if (isFind) {
            textView.setMovementMethod(LinkMovementClickMethod.getInstance());
        }
        textView.setText(style);

    }

    public static void makeHigthLightTaskText(final Activity act,
                                              TextView textView,
                                              SpannableString spannableString,
                                              HightLightTaskClickSpan.HightLightTaskClickListener nameListner,
                                              String TASK_MATCHERS,
                                              HightLightTaskClickSpan.HightLightTaskClickListener listener,
                                              int textColor,
                                              boolean isMatchName,
                                              HightLightTaskClickSpan.HightLightTaskClickListener urlMatchListener,
                                              HightLightTaskClickSpan.HightLightTaskClickListener phoneMatchListener,
                                              HightLightTaskClickSpan.HightLightTaskClickListener newListener) {
        if (textView == null || spannableString == null) {
            return;
        }
        final Context ctx = act;
        String matcherStr = "";
        SpannableStringBuilder style = new SpannableStringBuilder(spannableString);
        boolean isFind = false;
        if (!TextUtils.isEmpty(TASK_MATCHERS)) {
            Pattern p = Pattern.compile(TASK_MATCHERS);
            Matcher matcher = p.matcher(spannableString);
            while (matcher.find()) {
                isFind = true;
                style.setSpan(new HightLightTaskClickSpan(ctx,matcher.group(),
                                textView.getContext().getResources().getColor(textColor),
                                listener,
                                false),
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        Matcher matcherAtURL = WebPattern.WEB_URL.matcher(spannableString);
        while (matcherAtURL.find()) {
            matcherStr = matcherAtURL.group();
            if (matcherStr.toUpperCase().startsWith("HTTP://") || matcherStr.toUpperCase().startsWith("HTTPS://")) {
                isFind = true;
                style.setSpan(new HightLightTaskClickSpan(ctx,matcherAtURL.group(), textView.getContext().getResources().getColor(textColor), urlMatchListener, true),
                        matcherAtURL.start(), matcherAtURL.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

        List<String> names = new ArrayList<>();
        if (isMatchName) {
            Matcher matcherAtName = NAME_MATCHER.matcher(spannableString);
            while (matcherAtName.find()) {
                names.add(matcherAtName.group());
                isFind = true;
                style.setSpan(new HightLightTaskClickSpan(ctx,matcherAtName.group(), textView.getContext().getResources().getColor(textColor), nameListner, false),
                        matcherAtName.start(), matcherAtName.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        Matcher matcherAtPhone = WebPattern.MOBILE_PHONE.matcher(spannableString);
        while (matcherAtPhone.find()) {
            final String myPhone = matcherAtPhone.group();

            if (names.size() > 0) {
                for (String name : names) {
                    if (name.contains(myPhone)) {
                        break;
                    }
                }
            }

            isFind = true;
            style.setSpan(new HightLightTaskClickSpan(ctx,matcherAtPhone.group(), textView.getContext().getResources().getColor(textColor), phoneMatchListener, true),
                    matcherAtPhone.start(), matcherAtPhone.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }


        //新的文本消息超链解析,由于使用了replace 因此要放到最后 不可提前
        if (HyperLinkParseUtils.replaceToText(act, style, spannableString, textColor, newListener)) {
            isFind = true;
        }

        if (isFind) {
            if (textView instanceof MutiGestureTextView) {
                ((MutiGestureTextView) textView).setYZJMovementMethod(MutiGestureTextView.LocalLinkMovementMethod.getInstance());
            } else {
                textView.setMovementMethod(LinkMovementClickMethod.getInstance());
            }
        } else {
            if (textView instanceof MutiGestureTextView) {
                ((MutiGestureTextView) textView).setYZJMovementMethod(null);
            } else {
                textView.setMovementMethod(null);
            }
        }
        textView.setText(style);
    }

    public static void makeHigthLightTaskText(Activity act, TextView textView, SpannableString spannableString, String TASK_MATCHERS, HightLightTaskClickSpan.HightLightTaskClickListener listener, int textColorTask, int textcolorLink, int textcolorName) {
        makeHigthLightTaskText(act, textView, spannableString, null, TASK_MATCHERS, listener, textColorTask, textcolorLink, textcolorName);
    }

    public static void makeHigthLightTaskText(Activity act, TextView textView, SpannableString spannableString, String TASK_MATCHERS, HightLightTaskClickSpan.HightLightTaskClickListener listener, int textColorTask) {
        makeHigthLightTaskText(act, textView, spannableString, TASK_MATCHERS, listener, textColorTask, R.color.high_text_color, R.color.high_text_color);
    }

    public static void makeHigthLightTaskText(TextView textView, String content, HightLightTaskClickSpan.HightLightTaskClickListener listener) {
        if (textView == null || content == null) {
            return;
        }
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new HightLightTaskClickSpan(textView.getContext(),content, textView.getContext().getResources().getColor(R.color.high_text_color), listener),
                0, content.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(style);
    }
}
