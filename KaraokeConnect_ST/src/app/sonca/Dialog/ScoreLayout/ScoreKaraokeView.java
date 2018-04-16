package app.sonca.Dialog.ScoreLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import vn.com.sonca.MyLog.MyLog;

public class ScoreKaraokeView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textStroke = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ScoreKaraokeView(Context context) {
		super(context);
		initView(context);
	}

	public ScoreKaraokeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ScoreKaraokeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}
	
	private OnScoreKaraokeListener listener;
	public interface OnScoreKaraokeListener {
		public void OnScoreFinished(int intScore, List<Integer> arrayList);
	}
	
	public void setOnScoreKaraokeListener(OnScoreKaraokeListener listener){
		this.listener = listener;
	}
	
	private OnShowScoreListener listener2;
	public interface OnShowScoreListener {
		public void OnShow(int intScore);
	}
	
	public void setOnShowScoreListener(OnShowScoreListener listener){
		this.listener2 = listener;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if(width <= height){
			setMeasuredDimension((int) (2.5*width), width);
		}else{
			setMeasuredDimension((int) (2.5*height), height);
		}
	}
	
	private Rect bounds = new Rect();
	private int textX, textY, textS;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		 
		textS = (int) (1.0*h);
		textX = (int) (0.5*w);
		textY = (int) (0.5*h + 0.35*textS);

		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setStyle(Style.FILL);
		textPaint.setARGB(255, 255, 228, 0);
		textPaint.setTextSize(textS);

		textStroke.setTypeface(Typeface.DEFAULT_BOLD);
		textStroke.setStyle(Style.STROKE);
		textPaint.setARGB(255, 255, 228, 0);
		textStroke.setTextSize(textS);
		textStroke.setStrokeWidth((int) (0.03*textS));
		
	}
	
	private String textScore = "0";
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!textScore.equals("")){
			float w = (float) (textX - 0.5*textPaint.measureText(textScore));
			textPaint.getTextBounds(textScore, 0, textScore.length(), bounds);
			canvas.drawText(textScore, w, textY, textPaint);
			canvas.drawText(textScore, w, textY, textStroke);
		}
	}
	
	public void updateView(){
		post(new Runnable() {
			@Override public void run() {
				invalidate();
			}
		});
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(timerScore != null){
			timerScore.cancel(); 
			timerScore = null;
		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(timerScore != null){
			timerScore.cancel(); 
			timerScore = null;
		}
	}
	
///////////////////////////////////////////
	
	private int intScoreEnd = 0;
	private int intScoreShow = 0;
	private Timer timerScore;
	public void activatedScore(){
		if(timerScore != null){
			timerScore.cancel();
			timerScore = null;
		}
		intScoreShow = 0;
		intScoreEnd = (int) (60 + 40*Math.random());
		if(listener2 != null){
			listener2.OnShow(intScoreEnd);
		}
		timerScore = new Timer();
		timerScore.schedule(new TimerTask() {
			@Override public void run() {
				intScoreShow += 10;
				if(intScoreShow >= intScoreEnd){
					intScoreShow = intScoreEnd;
					textScore = intScoreShow + "";
					handlerScore.sendEmptyMessage(1);
					cancel();
				}else{
					textScore = intScoreShow + "";
					handlerScore.sendEmptyMessage(0);
				}
			}
		}, 30, 30);
	}
	
	private List<Integer> arrayList;
	public void activatedScore(int score, List<Integer> arrayList){
		if(timerScore != null){
			timerScore.cancel();
			timerScore = null;
		}
		intScoreShow = 0;
		intScoreEnd = score;
		if(listener2 != null){
			this.arrayList = arrayList;
			listener2.OnShow(intScoreEnd);
		}
		timerScore = new Timer();
		timerScore.schedule(new TimerTask() {
			@Override public void run() {
				intScoreShow += 2;
				if(intScoreShow >= intScoreEnd){
					intScoreShow = intScoreEnd;
					textScore = intScoreShow + "";
					handlerScore.sendEmptyMessage(1);
					cancel();
				}else{
					textScore = intScoreShow + "";
					handlerScore.sendEmptyMessage(0);
				}
			}
		}, 50, 50);
	}
	
	private Handler handlerScore = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				updateView();
			}else if(msg.what == 1){
				updateView();
				if(listener != null){
					listener.OnScoreFinished(intScoreEnd, arrayList);
				}
			}
		};
	};
	

}
