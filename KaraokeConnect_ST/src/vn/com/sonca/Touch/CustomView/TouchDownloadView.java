package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.net.NetworkInfo.State;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchDownloadView extends View {
	
	private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

	private float TX0 , TY0 , TX1 , TY1;
	private float TBX0 , TBX1 , TBY0 , TBY1;
	private float left, right, top, bottom;
	private Path pathLine = new Path();
	private Path pathBut = new Path();

	public TouchDownloadView(Context context) {
		super(context);
		initView(context);
	}

	public TouchDownloadView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchDownloadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnClickListener listener;
	public interface OnClickListener {
		public void OnExitClick();
		public void OnStopClick();
	}
	
	public void setOnClickListener(OnClickListener listener){
		this.listener = listener;
	}

	private String downloadPercent = "0 %";
	private String totalSize = "0 MB";
	private float percentWidth = 0;
	private Drawable drawbackground;
	private Drawable zlightdrawbackground;
	private String title;
		
	private void initView(Context context) {
		drawbackground = getResources().getDrawable(R.drawable.boder_note_1116x399);
		title = getResources().getString(R.string.popup_uppic1);
		
		zlightdrawbackground = getResources().getDrawable(R.drawable.zlight_boder_popup);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private float perS , perX , perY;
	private float titleS, titleX, titleY;
	private float totalS , totalX , totalY;
	private float startX, startY, stopX, stopY, stop2X;
	private Rect rectbackground = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = w/2;
		int tamY = h/2;
		int hr = 399*h/1080;
		int wr = 1116*hr/399;
		rectbackground = new Rect(tamX - wr/2, tamY - hr/2, tamX + wr/2, tamY + hr/2);
		
		int line = (int) (0.2*wr);
		startX = w/2 - (wr - line)/2;
		startY = (float) (0.5*h);
		stopX =  w/2 + (wr - line)/2;
		stopY =  startY;
		stop2X = startY;
		
		
		titleS = (float) (0.135*hr);
		titleX = (float) (0.25*w);
		titleY = (float) (0.43*h);
		
		totalS = (float) (0.11*hr);
		totalY = (float) (0.55*h);
		
		perS = (float) (0.11*hr);
		perX = (float) (0.7*w);
		perY = (float) (0.48*h);
		
	}
	
	public void setDownloadPercent(int downloadPercent , int contentLength) {
		this.downloadPercent = downloadPercent + " %";
		float full = stopX - startX;
		this.percentWidth = downloadPercent * full / 100;
		this.totalSize = String.format("%.02f", (float)contentLength / 1024 / 1024) + " MB";
		invalidate();
	}

	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.GRAY;
			color_02 = Color.argb(255, 0, 253, 255);
			color_03 = Color.WHITE;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.GRAY;
			color_02 = Color.parseColor("#005249");
			color_03 = Color.parseColor("#005249");
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			drawbackground.setBounds(rectbackground);
			drawbackground.draw(canvas);	
			
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(5);
			linePaint.setColor(color_01);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);
			canvas.drawLine(startX, startY, stopX, stopY, linePaint);
			
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(2.5f);
			linePaint.setColor(color_02);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);
			stop2X = startX + percentWidth;
			canvas.drawLine(startX, startY, stop2X, stopY, linePaint);
		
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_03);
			textPaint.setTextSize(titleS);
			canvas.drawText(title, titleX, titleY, textPaint);
			
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_03);
			textPaint.setTextSize(perS);
			canvas.drawText(downloadPercent, perX, perY, textPaint);
			
			
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_03);
			textPaint.setTextSize(totalS);
			totalX =  stopX - textPaint.measureText(totalSize);
			canvas.drawText(totalSize, totalX, totalY, textPaint);
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			zlightdrawbackground.setBounds(rectbackground);
			zlightdrawbackground.draw(canvas);	
			
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(5);
			linePaint.setColor(color_01);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);
			canvas.drawLine(startX, startY, stopX, stopY, linePaint);
			
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(2.5f);
			linePaint.setColor(color_02);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);
			stop2X = startX + percentWidth;
			canvas.drawLine(startX, startY, stop2X, stopY, linePaint);
		
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_03);
			textPaint.setTextSize(titleS);
			canvas.drawText(title, titleX, titleY, textPaint);
			
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_03);
			textPaint.setTextSize(perS);
			canvas.drawText(downloadPercent, perX, perY, textPaint);
			
			
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_03);
			textPaint.setTextSize(totalS);
			totalX =  stopX - textPaint.measureText(totalSize);
			canvas.drawText(totalSize, totalX, totalY, textPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return true;
	}
	
}
