package app.sonca.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import vn.com.hanhphuc.karremote.R;

public class ViewFlowerSong extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewFlowerSong(Context context) {
		super(context);
		initView(context);
	}

	public ViewFlowerSong(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewFlowerSong(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawPause;
	private Drawable drawPlay;
	private void initView(Context context) {
		drawPause = getResources().getDrawable(R.drawable.touch_pbstatus_pause_48x48);
		drawPlay = getResources().getDrawable(R.drawable.touch_pbstatus_play_48x48);
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.parseColor("#00fdfd"));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int nameS, nameX, nameY;
	private Rect rectImage = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		nameS = (int) (0.55*h);
		nameY = (int) (0.45*h + 0.5*nameS);
		nameX = (int) (h);
		textPaint.setTextSize(nameS);
		
		rectImage.set(0, 0, h, h);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(nameSong == null){
			return;
		}
		if(nameSong.equals("")){
			return;
		}
		if(isPausePlay){
			drawPlay.setBounds(rectImage);
			drawPlay.draw(canvas);
		}else{
			drawPause.setBounds(rectImage);
			drawPause.draw(canvas);
		}
		canvas.drawText(nameSong, nameX, nameY, textPaint);
	}
	
	private String nameSong = "";
	public void setNameSong(String nameSong){
		this.nameSong = nameSong;
		invalidate();
	}
	
	public boolean isPausePlay = false;
	public void setPausePlay(boolean value){
		isPausePlay = value;
		invalidate();
	}
	
	public boolean isPausePlay(){
		return this.isPausePlay;
	}

}
