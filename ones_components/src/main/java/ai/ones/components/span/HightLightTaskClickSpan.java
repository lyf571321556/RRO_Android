package ai.ones.components.span;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class HightLightTaskClickSpan extends ClickableSpan {
    private Context mContext;
	String text;
    HightLightTaskClickListener listener;
    int color;
    boolean hasUnderline = true;

    float textSize = -1;
    boolean fakeBoldText = false;

    public HightLightTaskClickSpan(Context context,String text, int color, HightLightTaskClickListener listener) {
        super();
        this.mContext=context;
        this.text = text;
        this.color = color;
        this.listener = listener;
    }

    public HightLightTaskClickSpan(Context context,String text, int color, HightLightTaskClickListener listener, boolean hasUnderline) {
        super();
        this.mContext=context;
        this.text = text;
        this.color = color;
        this.listener = listener;
        this.hasUnderline = hasUnderline;
    }

    public HightLightTaskClickSpan(Context context,String text, int color, HightLightTaskClickListener listener, boolean hasUnderline, float textSize, boolean fakeBoldText) {
        super();
        this.mContext=context;
        this.text = text;
        this.color = color;
        this.listener = listener;
        this.hasUnderline = hasUnderline;
        this.textSize = textSize;
        this.fakeBoldText = fakeBoldText;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if( color != 0 ){
            ds.setColor(color);
        }
        ds.setUnderlineText(hasUnderline);//去掉下划线</span>

        if(textSize != -1) {
            ds.setTextSize(textSize);
        }
        ds.setFakeBoldText(fakeBoldText);//仿“粗体”设置
    }

    @Override
    public void onClick(View widget) { 
    	if(listener != null) {
    		listener.onClick(text);
    	}
        if(widget instanceof TextView){
            ((TextView)widget).setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
        }
    }
    
    public interface HightLightTaskClickListener {
    	public void onClick(String text);
    }

}


