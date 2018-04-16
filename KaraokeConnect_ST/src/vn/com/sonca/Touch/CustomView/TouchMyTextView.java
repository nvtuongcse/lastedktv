package vn.com.sonca.Touch.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.DynamicLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

public class TouchMyTextView extends View {
	
	TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchMyTextView(Context context) {
		super(context);
		initView(context);
	}

	public TouchMyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchMyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Spannable wordtoSpan;
	private SpannableStringBuilder builder = new SpannableStringBuilder();
	private void initView(Context context) {

		String text = "01234567890123456789012345678901234567890123456789012345678901234567890123456789";
		wordtoSpan = new SpannableString(text);
		wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 5, 6,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 21,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 30, 31,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		textPaint.setTextSize(100);
		float f = textPaint.measureText("...(A)");
		int i = textPaint.breakText(wordtoSpan.toString(), true, getWidth() - f, null);
		builder.append(wordtoSpan.subSequence(0, i));
		builder.append("...(A)");
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.GRAY);
		StaticLayout layout = new StaticLayout(builder, textPaint, 
				getWidth(), Alignment.ALIGN_NORMAL, 1, 0, true);
		layout.draw(canvas);
		
		
	}
	
	
	
}
