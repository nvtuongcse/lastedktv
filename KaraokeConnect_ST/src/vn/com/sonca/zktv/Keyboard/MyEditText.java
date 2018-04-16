package vn.com.sonca.zktv.Keyboard;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MyApplication;

public class MyEditText extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public MyEditText(Context context) {
		super(context);
		initView(context);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String title = "";
	private Drawable drawImage, drawImageBlink;
	private void initView(Context context) {
		drawImage = context.getResources().getDrawable(R.drawable.ktv_song_boder_search);
		drawImageBlink = context.getResources().getDrawable(R.drawable.ktv_song_boder_search_blink);
		title = context.getResources().getString(R.string.ktv_title_search);
		textPaint.setStyle(Style.FILL);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rectImage = new Rect();
	private int textS, textX1, textX2, textY;
	private int padImage;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		textS = (int) (0.5*h);
		textY = (int) (0.5*h + 0.4*textS);
		textX1 = (int) (0.05*w);
		
		textPaint.setTextSize(textS);
		float s = textPaint.measureText(title);
		textX2 = (int) (textX1 + s);
		
		padImage = 4 * w / 450;
		rectImage.set(0 + padImage, 0 + padImage, w - padImage * 4, h - padImage);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(flagBlink){
			if(countBlink < 2){
				if(countBlink%2==0){
					drawImage.setBounds(rectImage);
					drawImage.draw(canvas);
				} else {
					drawImageBlink.setBounds(rectImage);
					drawImageBlink.draw(canvas);
				}
				countBlink++;
			} else {
				stopTimerBlink();
			}
		} else {
			drawImage.setBounds(rectImage);
			drawImage.draw(canvas);	
		}		
		
		textPaint.setARGB(255, 255, 255, 255);
		canvas.drawText(title, textX1, textY, textPaint);
		textPaint.setColor(Color.GREEN);
		canvas.drawText(" " + nameKey.toString(), textX2, textY, textPaint);
		
	}
	
	private StringBuffer nameKey = new StringBuffer("");
	public void setTextSearch(String key){
		if(key != null){
			if(key.equals("@")){
				if(nameKey.length() > 0){
					nameKey.deleteCharAt(nameKey.length() - 1);
				}
			}else if(nameKey.length() <= 6){
				nameKey.append(key);
			}else{}
			startTimerBlink();
			invalidate();
		}
	}
	
	public void setAllTextSearch(String key){
		if(key == null){
			key = "";
		}
		nameKey = null;
		nameKey = new StringBuffer(key);
		startTimerBlink();
		invalidate();
	}
	
	public void clearTextSearch(){
		if(nameKey.length() > 0){
			nameKey.delete(0, nameKey.length() - 1);
		}
	}
	
	public String getTextSearch(){
		return nameKey.toString();
	}
	 
	private Timer timerBlink;
	private boolean flagBlink = false;
	private int countBlink = 0;
	private void startTimerBlink(){
		stopTimerBlink();
		flagBlink = true;
		
		if(nameKey.toString().isEmpty()){
			return;
		}
		
		timerBlink = new Timer();
		timerBlink.schedule(new TimerTask() {
			
			@Override
			public void run() {				
				handlerInvalidate.sendEmptyMessage(0);
			}
		}, 200, 1000);
	}
	
	private void stopTimerBlink(){
		if(timerBlink != null){
			timerBlink.cancel();
			timerBlink = null;
		}
		flagBlink = false;
		countBlink = 0;
		invalidate();
	}
	
	private Handler handlerInvalidate = new Handler(){
		public void handleMessage(Message msg) {
			invalidate();
		};
	};
}
