package vn.com.sonca.zktv.FragSwitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;

public class ViewSwitch extends View {
	
	private String TAB = "ViewSwitch";
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewSwitch(Context context) {
		super(context);
		initView(context);
	}

	public ViewSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		anime.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				MyLog.e(TAB, "onAnimationEnd");
				if(listener != null){
					listener.onClick(ViewSwitch.this);
				}
			}
		});
		// name = context.getResources().getString(R.string.type_type);
		// drawImage = context.getResources().getDrawable(R.drawable.ktv_item_1);
		// drawIma = context.getResources().getDrawable(R.drawable.ktv_item_type);
		textPaint.setStyle(Style.FILL);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = (int) (1.85*height);
		setMeasuredDimension(width, height);
	}
	
	private int widthLayout;
	private Rect rextIma = new Rect();
	private Rect rectImage = new Rect();
	private int cirX, cirY, radius;
	private int poX, poY, poS;
	private int nameS, nameX, nameY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.47*h);
		int ww = (int) (1.85*hh);
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int) (0.5*w);
		tamY = (int) (0.38*h);
		hh = (int) (0.3*h);
		ww = 180*hh/128;
		rextIma.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		cirX = (int) (0.32*h);
		cirY = (int) (0.55*h);
		radius = (int) (0.08*h);
		
		poS = (int) (0.13*h);
		poX = (int) (0.172*w);
		poY = (int) (0.465*h + poS);
		
		nameS = (int) (0.14*h);
		nameX = (int) (0.12*w);
		nameY = (int) (0.68*h + nameS);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(drawImage != null){
			drawImage.setBounds(rectImage);
			drawImage.draw(canvas);
		}
		if(drawIma != null){
			drawIma.setAlpha(255);
			if(!isActive){
				drawIma.setAlpha(125);
			}
			drawIma.setBounds(rextIma);
			drawIma.draw(canvas);
		}
		
		paintMain.setStyle(Style.STROKE);
		paintMain.setStrokeWidth(4);
		paintMain.setColor(Color.WHITE);
		if(!isActive){
			paintMain.setARGB(125, 255, 255, 255);
		}
		canvas.drawCircle(cirX, cirY, radius, paintMain);

		textPaint.setColor(Color.WHITE);
		if(!isActive){
			textPaint.setARGB(125, 255, 255, 255);
		}
		textPaint.setTextSize(poS);
		textPaint.setTextAlign(Align.CENTER);
		canvas.drawText("" + position, poX, poY, textPaint);
		textPaint.setTextSize(nameS);
		textPaint.setTextAlign(Align.LEFT);
		canvas.drawText(name, nameX, nameY, textPaint);
		
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		//super.setOnClickListener(l);
		listener = l;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(isActive){
				this.startAnimation(anime);	
			}			
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return true;
	}
	
////////////////////////////////////////
	
	private String name = "";
	private int position = 1;
	private Drawable drawIma;
	private Drawable drawImage;
	private boolean isActive = true;
	public void setDataView(int position, String name, boolean isActive,
		Drawable drawImage, Drawable drawIma){
		this.isActive = isActive;
		this.position = position;
		this.drawImage = drawImage;
		this.drawIma = drawIma;
		this.name = name;
		invalidate();
	}

}
