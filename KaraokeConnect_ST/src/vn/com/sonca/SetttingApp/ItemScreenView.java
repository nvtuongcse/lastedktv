package vn.com.sonca.SetttingApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.SleepDevice.Sleep;
import vn.com.sonca.zzzzz.MyApplication;

public class ItemScreenView extends View {
	
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private int ENABLE = -1;
	private int INACTIVE = 0;
	private int ACTIVE = 1;
	private int HOVER = 2;
	private int state = INACTIVE;
	
	public ItemScreenView(Context context) {
		super(context);
		initView(context);
	}

	public ItemScreenView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ItemScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private String name = "Giao dien mau xanh sang(mac dinh)";
	private Drawable drawCheck;
	private Drawable drawFill;
	private Drawable drawAC;
	private void initView(Context context) {
		drawCheck = getResources().getDrawable(R.drawable.check_icon);
		drawAC = getResources().getDrawable(R.drawable.image_check_47x46_111);
		drawFill = getResources().getDrawable(R.drawable.image_boder_song);
	}
	
	private OnItemScreenListener listener;
	public interface OnItemScreenListener {
		public void OnScreenClick(int id);
	}
	
	public void setOnItemScreenListener(OnItemScreenListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int hangso = 7;
		int h = MeasureSpec.getSize(heightMeasureSpec);
		int w = MeasureSpec.getSize(widthMeasureSpec);
		if(w > hangso*h){
			int height = (int) (h);
			int width = (int) (w);
			setMeasuredDimension(width, height);
			return;
		}else{
			int width = w;
			int height = w/hangso;
			setMeasuredDimension(width, height);
		}
		
	}
	
	private int Line = 0;
	private int widthLayout = 0;
	private int heightLayout = 0;
	private float nameS, nameX, nameY;
	private float listS, listX, listY;
	private LinearGradient gradient;
	private Path pathGoc = new Path();
	private Path pathKhung = new Path();
	private Rect rectDevice = new Rect();
	private Rect rectFill = new Rect();
	private Rect rectCheck = new Rect();
	private Rect rectAC = new Rect();
	
	private int x0;
	private int widthbreak;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		x0 = 1;
		
		rectFill.set(x0, x0, widthLayout - x0, heightLayout - x0);

		
		int hr = (int) (0.35*h);
		int wr = 47*hr/46;
		rectAC.set(0, 0, wr, hr);
		
		int tamX = (int) (0.92*w);
		int tamY = (int) (0.5*h);
		hr = (int) (0.28*h);
		wr = 80*hr/70;
		rectCheck.set(tamX - wr + x0, tamY - hr + x0, 
				tamX + wr + x0, tamY + hr + x0);
		tamY = (int) (0.25*h);
		hr = (int) (0.4*h);
		wr = hr;
		tamX = (int) (1.75*wr);
		rectDevice.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
		nameS = (float) (0.29*h);
		nameX = (float) (0.05*w);
		nameY = (float) (0.37*h + 0.7*nameS);
		
		listS = (float) (0.3*h);
		listX = nameX;
		listY = (float) (0.75*h + 0.3*listS);
		
		gradient = new LinearGradient(0, 0, w, 0, 
				Color.argb(255, 0, 42, 80), 
				Color.argb(255, 1, 76, 141), 
				TileMode.CLAMP);
		
		Line = (int) (0.15*h);
		pathGoc.reset();
		
		pathGoc.moveTo(x0 , x0 + Line);
		pathGoc.lineTo(x0, x0);
		pathGoc.lineTo(x0 + Line , x0);
		
		pathGoc.moveTo(widthLayout - x0, x0 + Line);
		pathGoc.lineTo(widthLayout - x0, x0);
		pathGoc.lineTo(widthLayout - Line - x0 , x0);
		
		pathGoc.moveTo(widthLayout - x0 , heightLayout - Line - x0);
		pathGoc.lineTo(widthLayout - x0, heightLayout - x0);
		pathGoc.lineTo(widthLayout - Line - x0 , heightLayout - x0);
		
		pathGoc.moveTo(x0 , heightLayout - Line - x0);
		pathGoc.lineTo(x0 , heightLayout - x0);
		pathGoc.lineTo(x0 + Line , heightLayout - x0);
		
		
		pathKhung.reset();
		pathKhung.moveTo(x0, x0);
		pathKhung.lineTo(x0, heightLayout - x0);
		pathKhung.lineTo(widthLayout - x0 , heightLayout - x0);
		pathKhung.lineTo(widthLayout - x0, x0);
		pathKhung.lineTo(x0, x0);
		
		paintText.setTextSize(listS);
		float wt = paintText.measureText("...");
		widthbreak = (int) (w - (rectDevice.right - rectDevice.left) - 
				2*(rectCheck.right - rectCheck.left) - wt);
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		state = ACTIVE;
		MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;
		*/
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 0, 78, 144);
			color_02 = Color.argb(255, 0, 253, 253);
			color_03 = Color.argb(255, 180, 254, 255);
			color_04 = Color.GRAY;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#E6E7E8");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#21BAA9");
			color_04 = Color.GRAY;
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (state == INACTIVE) {

				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(1);
				paintMain.setColor(color_01);
				canvas.drawPath(pathKhung, paintMain);
				paintMain.setColor(color_02);
				canvas.drawPath(pathGoc, paintMain);

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_02);
				canvas.drawText(name, nameX, nameY, paintText);

			} else if (state == HOVER) {

				paintSimple.setShader(gradient);
				paintSimple.setStrokeWidth(getHeight());
				canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
				paintSimple.setShader(null);

				drawFill.setBounds(rectFill);
				drawFill.draw(canvas);

				drawAC.setBounds(rectAC);
				drawAC.draw(canvas);

				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(color_03);
				// canvas.drawPath(pathKhung, paintMain);
				paintMain.setColor(color_02);
				canvas.drawPath(pathGoc, paintMain);

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_02);
				canvas.drawText(name, nameX, nameY, paintText);

			} else if (state == ACTIVE) {

				paintSimple.setShader(gradient);
				paintSimple.setStrokeWidth(getHeight());
				canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
				paintSimple.setShader(null);

				drawFill.setBounds(rectFill);
				drawFill.draw(canvas);

				drawAC.setBounds(rectAC);
				drawAC.draw(canvas);

				drawCheck.setBounds(rectCheck);
				drawCheck.draw(canvas);

				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(color_03);
				// canvas.drawPath(pathKhung, paintMain);
				paintMain.setColor(color_02);
				canvas.drawPath(pathGoc, paintMain);

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_02);
				canvas.drawText(name, nameX, nameY, paintText);

			} else if (state == ENABLE) {

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_04);
				canvas.drawText(name, nameX, nameY, paintText);

			}
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			if (state == INACTIVE) {

				paintMain.setStyle(Style.FILL);
				paintMain.setColor(color_01);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(2);
				paintMain.setColor(Color.parseColor("#C1FFE8"));
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(Color.parseColor("#64949F"));
				canvas.drawText(name, nameX, nameY, paintText);

			} else if (state == HOVER) {
				
				paintMain.setStyle(Style.FILL);
				paintMain.setColor(color_02);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(2);
				paintMain.setColor(Color.parseColor("#C1FFE8"));
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_03);
				canvas.drawText(name, nameX, nameY, paintText);

			} else if (state == ACTIVE) {

				paintMain.setStyle(Style.FILL);
				paintMain.setColor(color_02);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(2);
				paintMain.setColor(Color.parseColor("#C1FFE8"));
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				
				drawCheck.setBounds(rectCheck);
				drawCheck.draw(canvas);

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_03);
				canvas.drawText(name, nameX, nameY, paintText);

			} else if (state == ENABLE) {

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_04);
				canvas.drawText(name, nameX, nameY, paintText);

			}
		}
		

	}
	
	/*
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if(state != ENABLE){
			if(pressed == true){
				state = HOVER;
				invalidate();
			}else{
				if(listener != null){
					listener.OnScreenClick(this.getId());
				}
			}
		}
	}
	*/
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			state = HOVER;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			if(listener != null){
				listener.OnScreenClick(this.getId());
			}
			break;
		default:
			break;
		}
		
		return true;
	}

	// ////////////////////////////////////
	
	public void setData(String data, boolean value) {
		name = data;
		if (value == true) {
			state = ACTIVE;
		} else {
			state = INACTIVE;
		}
		invalidate();
	}
	public void setData(boolean value) {
		if (value == true) {
			state = ACTIVE;
		} else {
			state = INACTIVE;
		}
		invalidate();
	}
	
}
