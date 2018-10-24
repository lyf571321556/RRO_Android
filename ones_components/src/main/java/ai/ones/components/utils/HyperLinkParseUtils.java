package ai.ones.components.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.ones.components.drawable.TextDrawable;
import ai.ones.components.span.HightLightTaskClickSpan;

/**
 * 超链解析工具类
 * Created by zgj on 2017/8/10.
 */

public class HyperLinkParseUtils {

    public final static String REGEX = "\\[h.*?text=\"([^\"`~!#$%\\^&\\*\\(\\)\\+\\=|{}':;',\\[\\]\\.<>/\\?~!#￥%……&\\*（）——+|{}【】‘；：”“’。，、？]*?)\" url=\"(.*?)\".*?\\]";

    public static final Pattern NEW_PATTERN
            = Pattern.compile(REGEX);

    public static boolean replaceToText(Context context, SpannableStringBuilder style, String str) {
        return replaceToText(context, style, new SpannableString(str), 0, null);
    }

    public static boolean replaceToText(Context context, SpannableStringBuilder style, SpannableString spannableString, int textColor, HightLightTaskClickSpan.HightLightTaskClickListener listener) {
        boolean isFind = false;
        Matcher matcherNew = NEW_PATTERN.matcher(spannableString);
//        int size = context.getResources().getDimensionPixelSize(R.dimen.common_font_fs2);
        int diff = 0;
        while (matcherNew.find()) {
//            String text = matcherNew.group(1);
//            if (text == null) {
//                return;
//            }
//            Drawable drawable = MentionUtils.textToBitmap(text,textView.getContext().getResources().getColor(textColor));
//            if (drawable == null) {
//                return;
//            }
//            drawable.setBounds(0, 0, size * text.length(), size);
//            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
//
//            style.setSpan(imageSpan, matcherNew.start(), matcherNew.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (matcherNew.group() == null ||
                    matcherNew.group(1) == null ||
                    matcherNew.group(2) == null) {
                continue;
            }
            isFind = true;
            if (listener != null) {
                style.setSpan(new HightLightTaskClickSpan
                                (context,matcherNew.group(2), context.getResources().getColor(textColor), listener, false),
                        matcherNew.start() - diff, matcherNew.end() - diff,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            style.replace(matcherNew.start() - diff, matcherNew.end() - diff, matcherNew.group(1));
            diff = diff + matcherNew.group().length() - matcherNew.group(1).length();
        }
        return isFind;
    }

    public static String filterHyperLink(String content){
        if( TextUtils.isEmpty(content) ){
            return null;
        }
        int diff = 0;
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        Matcher matcherNew = NEW_PATTERN.matcher(content);
        while (matcherNew.find()) {
            if (matcherNew.group() == null ||
                    matcherNew.group(1) == null ||
                    matcherNew.group(2) == null) {
                continue;
            }
            style.replace(matcherNew.start() - diff, matcherNew.end() - diff, matcherNew.group(1));
            diff = diff + matcherNew.group().length() - matcherNew.group(1).length();
        }
        return style.toString();

    }

    public static Drawable textToBitmap(Context context,String text, int color) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        return new TextDrawable(context,text, color);
    }

}
