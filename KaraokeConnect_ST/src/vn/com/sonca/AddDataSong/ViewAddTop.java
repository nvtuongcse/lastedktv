package vn.com.sonca.AddDataSong;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ViewAddTop extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewAddTop(Context context) {
		super(context);
		initView(context);
	}

	public ViewAddTop(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewAddTop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String title = "AAAAAAAAAAAaa";
	private Drawable drawLine;
	private Drawable drawable;
	private void initView(Context context) {
		title = getResources().getString(R.string.dialog_add_song_3);
		drawable = getResources().getDrawable(R.drawable.touch_tab_exit_active_144x72);
		drawLine = getResources().getDrawable(R.drawable.shape_line_search_view);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int textS, textX, textY;
	private Rect rectLine = new Rect();
	private Rect rectBackgroud = new Rect();
	private Rect rectDrawable = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectBackgroud.set(0, 0, w, (int)(0.95*h));
		rectLine.set(0, (int)(0.95*h), w, h);
		
		textS = (int) (0.5*h);
		textX = (int) (0.5*w);
		textY = (int) (0.4*h + 0.5*textS);
		
		int tamX = (int) (50);
		int tamY = (int) (0.5*h);
		int height = (int) (0.5*h);
		int width = 72*height/144;
		rectDrawable.set(10 , tamY - height, 10 + 2*width, tamY + height);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.parseColor("#003c6e"));
		canvas.drawRect(rectBackgroud, paintMain);
		drawLine.setBounds(rectLine);
		drawLine.draw(canvas);
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textS);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(Color.parseColor("#B4FEFF"));
		canvas.drawText(title, textX, textY, textPaint);
		
		drawable.setBounds(rectDrawable);
		drawable.draw(canvas);
		
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		// super.setOnClickListener(l);
		listener = l;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if(listener != null){
				float x= event.getX();
				float y = event.getY();
				if(x >= rectDrawable.left && x <= rectDrawable.right && 
					y >= rectDrawable.top && y <= rectDrawable.bottom){
					listener.onClick(this);
				}
			}
			break;

		default: break;
		}
		return true;
	}

}
