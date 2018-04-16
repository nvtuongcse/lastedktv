package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Connect.Model;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class TouchModelView extends View {
	
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private int ENABLE = -1;
	private int INACTIVE = 0;
	private int ACTIVE = 1;
	private int HOVER = 2;
	private int state = INACTIVE;
	
	public TouchModelView(Context context) {
		super(context);
		initView(context);
	}

	public TouchModelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchModelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private String name = "";
	private String list = "";
	private Drawable drawCheck;
	private Drawable drawFill;
	private Drawable drawAC;
	private Drawable drawDevice;
	private void initView(Context context) {
		drawCheck = getResources().getDrawable(R.drawable.check_icon);
		drawAC = getResources().getDrawable(R.drawable.image_check_47x46_111);
		drawFill = getResources().getDrawable(R.drawable.image_boder_song);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.09*getResources().getDisplayMetrics().heightPixels);
		setMeasuredDimension(widthMeasureSpec, height);
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
		x0 = 0;
		
		rectFill.set(0, 0, w, h);

		
		int hr = (int) (0.35*h);
		int wr = 47*hr/46;
		rectAC.set(0, 0, wr, hr);
		
		int tamX = (int) (0.92*w);
		int tamY = (int) (0.5*h);
		hr = (int) (0.28*h);
		wr = 80*hr/70;
		rectCheck.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamY = (int) (0.25*h);
		hr = (int) (0.4*h);
		wr = hr;
		tamX = (int) (1.75*wr);
		rectDevice.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
		nameS = (float) (0.4*h);
		nameX = (float) (tamX + 1.8*wr);
		nameY = (float) (0.35*h + 0.3*nameS);
		
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
		
		pathGoc.moveTo(widthLayout , x0 + Line);
		pathGoc.lineTo(widthLayout , x0);
		pathGoc.lineTo(widthLayout - Line , x0);
		
		pathGoc.moveTo(widthLayout , heightLayout - Line);
		pathGoc.lineTo(widthLayout, heightLayout);
		pathGoc.lineTo(widthLayout - Line , heightLayout);
		
		pathGoc.moveTo(x0 , heightLayout - Line);
		pathGoc.lineTo(x0 , heightLayout);
		pathGoc.lineTo(x0 + Line , heightLayout);
		
		
		pathKhung.reset();
		pathKhung.moveTo(0, 0);
		pathKhung.lineTo(0, heightLayout);
		pathKhung.lineTo(widthLayout, heightLayout);
		pathKhung.lineTo(0, heightLayout);
		pathKhung.lineTo(0, 0);
		
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
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		*/
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 0, 78, 144);
			color_02 = Color.argb(255, 0, 253, 253);
			color_03 = Color.argb(255, 180, 254, 255);
			color_04 = Color.GRAY;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#E6E7E8");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#21BAA9");
			color_04 = Color.GRAY;
		}

		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (state == INACTIVE) {
	
				drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
	
				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(color_01);
				// canvas.drawPath(pathKhung, paintMain);
				paintMain.setColor(color_02);
				canvas.drawPath(pathGoc, paintMain);
	
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_03);
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
	
			} else if (state == HOVER) {
	
				paintSimple.setShader(gradient);
				paintSimple.setStrokeWidth(getHeight());
				canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
				paintSimple.setShader(null);
	
				drawFill.setBounds(rectFill);
				drawFill.draw(canvas);
	
				drawAC.setBounds(rectAC);
				drawAC.draw(canvas);
	
				drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65_x);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
	
				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(color_03);
				// canvas.drawPath(pathKhung, paintMain);
				paintMain.setColor(color_02);
				canvas.drawPath(pathGoc, paintMain);
	
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_02);
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
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
	
				drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65_x);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
	
				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(color_03);
				// canvas.drawPath(pathKhung, paintMain);
				paintMain.setColor(color_02);
				canvas.drawPath(pathGoc, paintMain);
	
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_02);
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
			} else if (state == ENABLE) {
	
				drawDevice = getResources().getDrawable(R.drawable.image_daumay_inactive_71x65_x);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
	
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_04);
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

			if (state == INACTIVE) {
	
				paintMain.setStyle(Style.FILL);
				paintMain.setColor(color_01);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(Color.parseColor("#C1FFE8"));
				paintMain.setStrokeWidth(2);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				
				drawDevice = getResources().getDrawable(R.drawable.image_daumay_daketnoi_71x65_x);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(Color.parseColor("#64949F"));
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
	
			} else if (state == HOVER) {
	
				paintMain.setStyle(Style.FILL);
				paintMain.setColor(color_02);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(Color.parseColor("#C1FFE8"));
				paintMain.setStrokeWidth(2);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);

				drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65_x);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_03);
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
			} else if (state == ACTIVE) {
	
				paintMain.setStyle(Style.FILL);
				paintMain.setColor(color_02);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
				paintMain.setStyle(Style.STROKE);
				paintMain.setColor(Color.parseColor("#C1FFE8"));
				paintMain.setStrokeWidth(2);
				canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);

				drawCheck.setBounds(rectCheck);
				drawCheck.draw(canvas);
	
				drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65_x);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);

				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_03);
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
			} else if (state == ENABLE) {
	
				drawDevice = getResources().getDrawable(R.drawable.image_daumay_inactive_71x65_x);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
	
				paintText.setStyle(Style.FILL);
				paintText.setTextSize(nameS);
				paintText.setColor(color_04);
				canvas.drawText(name, nameX, nameY, paintText);
				paintText.setTextSize(listS);
				int i = paintText.breakText(list.toString(), true, widthbreak, null);
				if (list.length() > i) {
					canvas.drawText(list.subSequence(0, i) + "...", listX, listY, paintText);
				} else {
					canvas.drawText(list, listX, listY, paintText);
				}
			}
		
		}
		
	}
	
	
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		MyLog.d("ModelView", "pressed : " + pressed);
		if(state != ENABLE){
			if(pressed == true){
				state = HOVER;
				invalidate();
			}
		}
	}

	// ////////////////////////////////////
	
	public void setData(Model model){
		name = model.getName();
		list = model.getDetails();
		if(model.getEn() == 0){
			state = ENABLE;
		}else{
			if (model.isActive() == true) {
				state = ACTIVE;
			} else {
				state = INACTIVE;
			}
		}
		invalidate();
	}
	
}
