package vn.com.sonca.Dialog.BlockMelody;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class MelodyDragView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintCyan = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public MelodyDragView(Context context) {
		super(context);
		initView(context);
	}

	public MelodyDragView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MelodyDragView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnMelodyDragViewListener listener;
	public interface OnMelodyDragViewListener {
		public void OnMelodyDrag(int value);
	}
	
	public void setOnMelodyDragViewListener(OnMelodyDragViewListener listener){
		this.listener = listener;
	}

	private Drawable drawable;
	private void initView(Context context) {
		paintGray.setStyle(Style.FILL);
		paintGray.setColor(Color.parseColor("#989898"));
		
		paintCyan.setStyle(Style.FILL);
		paintCyan.setColor(Color.parseColor("#00fdfd"));
		
		paintCircle.setStyle(Style.FILL);
		paintCircle.setColor(Color.parseColor("#bafefe"));
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextAlign(Align.CENTER);
		
		drawable = getResources().getDrawable(R.drawable.icon_block_volumn_drag_kdk);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int widthView;
	private int heightView;
	private int cx, cy, ra;
	private int textS, textX, textY, textYY;
	private int[] offsetCenter = new int[11];
	private RectF rectRound = new RectF();
	private RectF rectRoundValue = new RectF();

	private Rect rectImage = new Rect();
	private int wImage, hImage, topImage, bottomImage, leftImage, rightImage;
	private int intround;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		heightView = h;
		
		intround = 2;
		int tamY = (int) (0.6*h);
		int paddind = (int) (0.03*w);
		int zzz = (w - 2*paddind)/10;
		for (int i = 0; i < offsetCenter.length; i++) {
			offsetCenter[i] = paddind + i*zzz;
		}
		cy = (int) (tamY);
		ra = intround;
		rectRound.set(paddind, tamY-intround, 
				offsetCenter[offsetCenter.length-1], tamY+intround);
		rectRoundValue.set(rectRound);
		
		textS = (int) (0.18*h);
		textY = (int) (0.75*h + 0.5*textS);
		textYY = (int) (0.8*h + 0.5*textS);
		
		hImage = (int) (0.4*h);
		wImage = 30*hImage/40;
		bottomImage = (int) (0.5*h);
		topImage = bottomImage - hImage;
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			drawable = getResources().getDrawable(R.drawable.icon_block_volumn_drag_kdk);
			paintGray.setColor(Color.parseColor("#989898"));
			paintCyan.setColor(Color.parseColor("#00fdfd"));
			paintCircle.setColor(Color.parseColor("#bafefe"));
			color_01 = Color.GREEN;
			color_02 = Color.argb(255, 0, 253, 253);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			drawable = getResources().getDrawable(R.drawable.zlight_icon_block_volumn_drag_kdk_light);
			paintGray.setColor(Color.WHITE);
			paintCyan.setColor(Color.argb(255, 0, 82, 73));
			paintCircle.setColor(Color.argb(255, 155, 159, 163));
			color_01 = Color.argb(255, 0, 82, 73);
			color_02 = Color.argb(255, 255, 255, 255);
		}
		
	}

	private int color_01 = 0;
	private int color_02 = 0;
	private int valueView = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRoundRect(rectRound, intround, intround, paintGray);
		rectRoundValue.right = offsetCenter[valueView];
		canvas.drawRoundRect(rectRoundValue, intround, intround, paintCyan);
		for (int i = 0; i < offsetCenter.length; i++) {
			canvas.drawCircle(offsetCenter[i], cy, ra, paintCircle);
		}
		textPaint.setTextSize(textS);
		textPaint.setColor(color_01);
		for (int i = 0; i < offsetCenter.length; i++) {
			if(valueView != i){
				canvas.drawText("" + i, offsetCenter[i], textY, textPaint);
			}
		}
		textPaint.setColor(color_02);
		textPaint.setTextSize((float) (1.5*textS));
		canvas.drawText("" + valueView, offsetCenter[valueView], textYY, textPaint);
		
		if(drawable != null){
			leftImage = (int) (offsetCenter[valueView] - 0.5*wImage);
			rightImage = (int) (offsetCenter[valueView] + 0.5*wImage);
			rectImage.set(leftImage, topImage, rightImage, bottomImage);
			drawable.setBounds(rectImage);
			drawable.draw(canvas);
		}
	}
	
	private int stateClickView = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:{
			float x = event.getX();
			for (int i = 0; i < offsetCenter.length; i++) {
				if(x <= offsetCenter[i]){
					valueView = i;
					break;
				}
			}invalidate();
			if(listener != null){
				listener.OnMelodyDrag(valueView);
			}
		}break;	
		default: break;
		}		
		return true;
	}
	
/////////////////////////////////////////
	
	public void setValueMelody(int value){
		valueView = value;
		invalidate();
	}

}
