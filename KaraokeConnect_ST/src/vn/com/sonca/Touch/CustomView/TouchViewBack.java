package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.MailTo;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchViewBack extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchViewBack(Context context) {
		super(context);
		initView(context);
	}

	public TouchViewBack(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchViewBack(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnBackListener listener;
	public interface OnBackListener{
		public void OnBack();
	}
	
	public void setOnBackListener(OnBackListener listener){
		this.listener = listener;
	}
	
	private Drawable drawUser;
	
	private Drawable drawSinger;
	private Drawable drawMusician;
	private Drawable drawType;
	private Drawable drawLang;
	
	private Drawable zlightdrawUser;
	private Drawable zlightdrawSinger;
	private Drawable zlightdrawMusician;
	private Drawable zlightdrawType;
	private Drawable zlightdrawLang;
	
	private void initView(Context context) {
		paintMain.setStyle(Style.FILL);
		drawSinger = getResources().getDrawable(R.drawable.touch_image_back_singer_99x50);
		drawMusician = getResources().getDrawable(R.drawable.touch_image_back_tacgia_99x50);
		drawType = getResources().getDrawable(R.drawable.touch_image_back_theloai_99x50);
		drawLang = getResources().getDrawable(R.drawable.touch_image_back_ngonngu_99x50);
		drawUser = getResources().getDrawable(R.drawable.touch_icon_user_search);
		
		zlightdrawSinger = getResources().getDrawable(R.drawable.zlight_image_back_singer_99x50);
		zlightdrawMusician = getResources().getDrawable(R.drawable.zlight_image_back_theloai_99x50);
		zlightdrawType = getResources().getDrawable(R.drawable.zlight_image_back_theloai_99x50);
		zlightdrawLang = getResources().getDrawable(R.drawable.touch_image_back_ngonngu_99x50);
		zlightdrawUser = getResources().getDrawable(R.drawable.touch_icon_user_search);
		
		this.state = GONE;
	}
	
	public static int state = GONE;
	public void setVisiable(int state){
		this.state = state; 
		requestLayout();
	}
	
	public int getVisiable(){
		return this.state;
	}
	
	private String layout = "";
	public void setLayout(String layout , boolean bool){
		this.layout = layout;
		if(bool){
			invalidate();
		}
	}
	
	private int widthView = 0;
	public int getWidthView(){
		return widthView;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(state == GONE){
			setMeasuredDimension(0, 0);
		}else{
			int height = MeasureSpec.getSize(heightMeasureSpec);
			int width = 75*height/50;
			setMeasuredDimension(width, height);
		}
	}
	
	private int heightView = 0;
	private Rect rectDraw = new Rect();
	private Rect rectbackgroud = new Rect();
	private Rect rectUser = new Rect();
	private int lineX , startY , stopY;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		heightView = h;
		lineX = w - 5;
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.45*h);
		int hr = (int) (0.30*h);
		int wr = 99*hr/50;
		rectbackgroud = new Rect(0, 0, w, h);
		rectDraw = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
		stopY = (int) (h/2 - 0.43*h);
		startY = (int) (h/2 + 0.38*h);
		
		hr = (int) (0.18*h);
		wr = hr;
		tamX = (int) (1.5*hr);
		tamY = (int) (h - 1.5*hr);
		rectUser.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
	}
	
	private int color_01;
	private int color_02;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 1, 55, 102);
			color_02 = Color.argb(255, 255, 253, 253);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.parseColor("#10B008");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {

			paintMain.setStyle(Style.FILL);
			paintMain.setColor(color_01);
			//canvas.drawRect(rectbackgroud, paintMain);
			// --------------//
			if (layout.equals(TouchMainActivity.SINGER)) {
				drawSinger.setBounds(rectDraw);
				drawSinger.draw(canvas);
			}
			if (layout.equals(TouchMainActivity.MUSICIAN)) {
				drawMusician.setBounds(rectDraw);
				drawMusician.draw(canvas);
			}
			if (layout.equals(TouchMainActivity.SONGTYPE)) {
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(1);
				paintMain.setColor(color_02);
				canvas.drawLine(lineX, startY, lineX, stopY, paintMain);
				drawType.setBounds(rectDraw);
				drawType.draw(canvas);
			}
			if (layout.equals(TouchMainActivity.LANGUAGE)) {
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(1);
				paintMain.setColor(color_02);
				canvas.drawLine(lineX, startY, lineX, stopY, paintMain);
				drawLang.setBounds(rectDraw);
				drawLang.draw(canvas);
			}
			/*
			 * if(MyApplication.intWifiRemote == MyApplication.SONCA &&
			 * MyApplication.bOffUserList == true){
			 * drawUser.setBounds(rectUser); drawUser.draw(canvas); }
			 */
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			paintMain.setStyle(Style.FILL);
			paintMain.setColor(color_01);
			canvas.drawRect(rectbackgroud, paintMain);
			// --------------//
			if (layout.equals(TouchMainActivity.SINGER)) {
				zlightdrawSinger.setBounds(rectDraw);
				zlightdrawSinger.draw(canvas);
			}
			if (layout.equals(TouchMainActivity.MUSICIAN)) {
				zlightdrawMusician.setBounds(rectDraw);
				zlightdrawMusician.draw(canvas);
			}
			if (layout.equals(TouchMainActivity.SONGTYPE)) {
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(1);
				paintMain.setColor(color_02);
				canvas.drawLine(lineX, startY, lineX, stopY, paintMain);
				zlightdrawType.setBounds(rectDraw);
				zlightdrawType.draw(canvas);
			}
			if (layout.equals(TouchMainActivity.LANGUAGE)) {
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(1);
				paintMain.setColor(color_02);
				canvas.drawLine(lineX, startY, lineX, stopY, paintMain);
				zlightdrawLang.setBounds(rectDraw);
				zlightdrawLang.draw(canvas);
			}
			/*
			 * if(MyApplication.intWifiRemote == MyApplication.SONCA &&
			 * MyApplication.bOffUserList == true){
			 * drawUser.setBounds(rectUser); drawUser.draw(canvas); }
			 */
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if(listener != null){
				listener.OnBack();
			}
		} 
		return true;
	}

}
