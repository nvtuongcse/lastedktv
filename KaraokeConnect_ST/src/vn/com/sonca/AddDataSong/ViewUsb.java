package vn.com.sonca.AddDataSong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.ItemUSB;

public class ViewUsb extends View {
	
	private String TAB = "ViewUsb";
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public ViewUsb(Context context) {
		super(context);
		initView(context);
	}

	public ViewUsb(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewUsb(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawUSB;
	private Drawable drawNet;
	private void initView(Context context) {
		drawUSB = getResources().getDrawable(R.drawable.icon_usb);
		drawNet = getResources().getDrawable(R.drawable.icon_net);
		textPaint.setStyle(Style.FILL);
		textPaint.setTextAlign(Align.CENTER);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int w = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(w, w);
	}
	
	private int Line = 0;
	private int widthLayout = 0;
    private int heightLayout = 0;
	private Path pathGoc = new Path();
	private Path pathKhung = new Path();
	private Rect rectImage = new Rect();
	private Rect rectText = new Rect();
	private int textS, textX, textY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
    	heightLayout = h;
    	
    	Line = 50*h/1080;
		
		pathKhung = new Path();
		pathKhung.reset();
		pathKhung.moveTo(widthLayout , 0);
		pathKhung.lineTo(widthLayout, heightLayout);
		pathKhung.lineTo(0 , heightLayout);
		pathKhung.lineTo(0, 0);
		//if(position == 0){
			pathKhung.lineTo(widthLayout , 0);
		//}
		pathGoc = new Path();
		pathGoc.reset();
		pathGoc.moveTo(0 , 0 + Line);
		pathGoc.lineTo(0, 0);
		//if(position == 0){
			pathGoc.lineTo(0 + Line , 0);
		//}
		pathGoc.moveTo(widthLayout , 0 + Line);
		pathGoc.lineTo(widthLayout , 0);
		//if(position == 0){
			pathGoc.lineTo(widthLayout - Line , 0);
		//}
		pathGoc.moveTo(widthLayout , heightLayout - Line);
		pathGoc.lineTo(widthLayout, heightLayout);
		pathGoc.lineTo(widthLayout - Line , heightLayout);
		pathGoc.moveTo(0 , heightLayout - Line);
		pathGoc.lineTo(0 , heightLayout);
		pathGoc.lineTo(0 + Line , heightLayout);
		
		
		rectText.set(0, (int)(0.8*h), w, h);
		textS = (int) (0.1*h);
		textX = (int) (0.5*w);
		textY = (int) (0.89*h + 0.5*textS);
		textPaint.setTextSize(textS);
		textPaint.setColor(Color.parseColor("#00FDFD"));
		
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.4*h);
		int hh = (int) (0.3*h);
		int ww = hh;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
	}
	
	private int color_02;
    private int color_03;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setARGB(25, 0, 0, 0);
		canvas.drawPath(pathKhung, paintMain);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.parseColor("#000d19"));
		paintMain.setAlpha(128);
		canvas.drawRect(rectText, paintMain);
		
		color_02 = Color.argb(255, 0, 78, 144);
		color_03 = Color.argb(255, 0, 253, 253);
		paintMain.setStyle(Style.STROKE);
		paintMain.setStrokeWidth((float) 3.0);
		paintMain.setColor(color_02);
		canvas.drawPath(pathKhung, paintMain);
		paintMain.setColor(color_03);
		canvas.drawPath(pathGoc, paintMain);
		
		
		canvas.drawText(name, textX, textY, textPaint);
		
		if(name.equalsIgnoreCase(getResources().getString(R.string.update_net_2))){
			drawNet.setBounds(rectImage);
			drawNet.draw(canvas);
		} else {
			drawUSB.setBounds(rectImage);
			drawUSB.draw(canvas);	
		}	
		
	}
	
	private String name = "";
	private ItemUSB itemUSB;
	public void setData(ItemUSB itemUSB){
		this.name = itemUSB.getUsbName();
		this.itemUSB = itemUSB;
		invalidate();
	}

}
