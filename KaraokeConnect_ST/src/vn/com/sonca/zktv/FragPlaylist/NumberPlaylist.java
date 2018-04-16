package vn.com.sonca.zktv.FragPlaylist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.CustomView.TouchMyGroupSong.OnMyGroupSongListener;
import vn.com.sonca.params.Song;

public class NumberPlaylist extends View {
	
	private int widthLayout, heightLayout;
	private TextPaint textNumber = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public NumberPlaylist(Context context) {
		super(context);
		initView(context);
	}

	public NumberPlaylist(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public NumberPlaylist(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnButFirstListener listener;
	public interface OnButFirstListener {
		public void OnFristRes(View view, int x, int y);
	}
	
	public void setOnButFirstListener(OnButFirstListener listener){
		this.listener = listener;
	}
	
	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		
		paintMain.setStyle(Style.FILL);
		textNumber.setStyle(Style.FILL);
		textNumber.setTextAlign(Align.CENTER);
		textNumber.setColor(Color.WHITE);
		textNumber.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int numberX, numberY, numberS;
	private int cirX, cirY, radius;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		heightLayout = h;
		
		cirY = (int) (0.5*h);
		cirX = (int) (0.5*h);
		radius = (int) (0.4*h);
		
		numberS = (int) (0.5*h);
		numberX = (int) (0.5*h);
		numberY = (int) (0.5*h + 0.38*numberS);
		textNumber.setTextSize(numberS);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(number <= 0){
			return;
		}
		
		paintMain.setColor(getResources().getColor(R.color.ktv_color_image_7));
		canvas.drawCircle(cirX, cirY, radius, paintMain);
		
		canvas.drawText("" + number, numberX, numberY, textNumber);
	}
	
	private int number = -1;
	public void setNumberPlaylist(int number){
		this.number = number;
		invalidate();
	}

}
