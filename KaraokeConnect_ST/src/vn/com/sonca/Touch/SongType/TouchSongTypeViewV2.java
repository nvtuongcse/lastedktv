package vn.com.sonca.Touch.SongType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.params.SongType;
import vn.com.sonca.zzzzz.MyApplication;

public class TouchSongTypeViewV2  extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchSongTypeViewV2(Context context) {
		super(context);
		initView(context);
	}

	public TouchSongTypeViewV2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSongTypeViewV2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (width/6);
		setMeasuredDimension(width, height);
	}
	
	private float Line;
	private int soS, soX, soY;
	private int textS, textX, textY;
	private Rect rectKhung = new Rect();
	private Path pathGoc = new Path();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int widthLayout = w;
    	int heightLayout = h;
		Line = (float) (0.1*h);
		
		int tyleH = (int) (0.05*h);
		int tyleW = tyleH;
		int width = w - tyleW;
		int height = h - tyleH;
		rectKhung.set(tyleW, tyleH, width, height);
		
		soS = (int) (0.35*h);
		soX = (int) (0.95*w);
		soY = (int) (0.5*h + 0.36*soS);
		
		textS = (int) (0.45*h);
		textX = (int) (0.05*w);
		textY = (int) (0.5*h + 0.36*textS);
		
		pathGoc.reset();
		
		pathGoc.moveTo(tyleW , tyleH + Line);
		pathGoc.lineTo(tyleW, tyleH);
		pathGoc.lineTo(tyleW + Line , tyleH);
		
		pathGoc.moveTo(width , tyleH + Line);
		pathGoc.lineTo(width , tyleH);
		pathGoc.lineTo(width - Line , tyleH);
		
		pathGoc.moveTo(width , height - Line);
		pathGoc.lineTo(width, height);
		pathGoc.lineTo(width - Line , height);
		
		pathGoc.moveTo(tyleW , height - Line);
		pathGoc.lineTo(tyleW , height);
		pathGoc.lineTo(tyleW + Line , height);
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.parseColor("#000000");
			color_02 = Color.parseColor("#00FDFD");
			color_03 = Color.parseColor("#B4FEFF");
			color_04 = Color.parseColor("#989898");
			color_05 = Color.parseColor("#244D65");
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#FFFFFF");
			color_02 = Color.parseColor("#E6E7E8");
			color_03 = Color.parseColor("#404040");
			color_04 = Color.parseColor("#989898");
			color_05 = Color.parseColor("#244D65");
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(color_01);
			paintMain.setAlpha(50);
			canvas.drawRect(rectKhung, paintMain);
			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(1);
			paintMain.setColor(color_05);
			paintMain.setAlpha(255);
			canvas.drawRect(rectKhung, paintMain);
			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(2);
			paintMain.setColor(color_02);
			paintMain.setAlpha(255);
			canvas.drawPath(pathGoc, paintMain);
			
			textPaint.setStyle(Style.FILL);
			if(number != 0){
				textPaint.setColor(color_03);
			}else{
				textPaint.setColor(color_04);
			}
			if(name.equals(getResources().getString(R.string.newSongType_3))){
				int valueYouTube = MyApplication.getCommandMediumYouTube();
				if(valueYouTube == 2){
					textPaint.setColor(color_04);
				} else {
					textPaint.setColor(color_03);
				}				
			}
			textPaint.setTextSize(textS);
			canvas.drawText(name, textX, textY, textPaint);
			textPaint.setTextSize(soS);
			float x = (int) textPaint.measureText("" + number);
			if(!name.equals(getResources().getString(R.string.newSongType_3))){
				canvas.drawText(number + "", soX - x, soY, textPaint);	
			}
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

		}
		
		
	}
	
	private int number = 0;
	private String name = "";
	public void setData(SongType songType){
		if(songType == null){
			name = "";
			number = 0;
		}else{
			name = songType.getName();
			number = songType.getCountTotal();
		}
		invalidate();
	}
	

}
