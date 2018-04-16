package vn.com.sonca.zktv.FragImage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.zktv.FragData.MySongView;

public class MyGroupImage extends ViewGroup {
	
	private String TAB = "MyGroupImage";
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public MyGroupImage(Context context) {
		super(context);
		initView(context);
	}

	public MyGroupImage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyGroupImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		setWillNotDraw(false);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rectImage1 = new Rect();
	private Rect rectImage2 = new Rect();
	private Rect rectImage3 = new Rect();
	private Rect rectImage4 = new Rect();
	private Rect rectImage5 = new Rect();
	private Rect rectImage6 = new Rect();
	private Rect rectImage7 = new Rect();
	private Rect rectImage8 = new Rect();
	private Path pathLine = new Path();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int wp = w/4;
		int hp = (int) (0.5*h);
		int pad = 1;
		pathLine.reset();
		pathLine.moveTo(pad, pad);
		pathLine.lineTo(w-pad, pad);
		pathLine.lineTo(w-pad, h-pad);
		pathLine.lineTo(pad, h-pad);
		pathLine.lineTo(pad, pad);
		pathLine.moveTo(0, hp);
		pathLine.lineTo(w-pad, hp);
		pathLine.moveTo(wp, pad);
		pathLine.lineTo(wp, h-pad);
		pathLine.moveTo(2*wp, pad);
		pathLine.lineTo(2*wp, h-pad);
		pathLine.moveTo(3*wp, pad);
		pathLine.lineTo(3*wp, h-pad);
		
		int hI = h/4;
		int wI = w/8;
//		if(w/4 > h/2){
//			MyLog.e(TAB, "onSizeChanged : TOP");
//			hI = (int) (0.235*h);
//			wI = hI;
//		}else{
//			MyLog.e(TAB, "onSizeChanged : BOTTOM");
//			wI = (int) (0.11*w);
//			hI = wI;
//		}
		
		int tamX = 1*w/8, 	tamY = 1*h/4;
		rectImage1.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		tamX = 3*w/8; 		tamY = 1*h/4;
		rectImage2.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		tamX = 5*w/8; 		tamY = 1*h/4;
		rectImage3.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		tamX = 7*w/8; 		tamY = 1*h/4;
		rectImage4.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		tamX = 1*w/8; 		tamY = 3*h/4;
		rectImage5.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		tamX = 3*w/8; 		tamY = 3*h/4;
		rectImage6.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		tamX = 5*w/8; 		tamY = 3*h/4;
		rectImage7.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		tamX = 7*w/8; 		tamY = 3*h/4;
		rectImage8.set(tamX-wI, tamY-hI, tamX+wI, tamY+hI);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		paintMain.setStyle(Style.STROKE);
		paintMain.setColor(Color.parseColor("#016D7F"));
		paintMain.setStrokeWidth(1);
		canvas.drawPath(pathLine, paintMain);
//		paintMain.setStyle(Style.FILL);
//		paintMain.setColor(Color.GRAY);
//		
//		canvas.drawRect(rectImage1, paintMain);
//		canvas.drawRect(rectImage2, paintMain);
//		canvas.drawRect(rectImage3, paintMain);
//		canvas.drawRect(rectImage4, paintMain);
//		canvas.drawRect(rectImage5, paintMain);
//		canvas.drawRect(rectImage6, paintMain);
//		canvas.drawRect(rectImage7, paintMain);
//		canvas.drawRect(rectImage8, paintMain);
		
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		int padding = 5;
		
		View image1 = getChildAt(0);
		image1.layout(
				rectImage1.left+padding, rectImage1.top+padding, 
				rectImage1.right-padding, rectImage1.bottom-padding);
		View image2 = getChildAt(1);
		image2.layout(
				rectImage2.left+padding, rectImage2.top+padding, 
				rectImage2.right-padding, rectImage2.bottom-padding);
		View image3 = getChildAt(2);
		image3.layout(
				rectImage3.left+padding, rectImage3.top+padding, 
				rectImage3.right-padding, rectImage3.bottom-padding);
		View image4 = getChildAt(3);
		image4.layout(
				rectImage4.left+padding, rectImage4.top+padding, 
				rectImage4.right-padding, rectImage4.bottom-padding);
		View image5 = getChildAt(4);
		image5.layout(
				rectImage5.left+padding, rectImage5.top+padding, 
				rectImage5.right-padding, rectImage5.bottom-padding);
		View image6 = getChildAt(5);
		image6.layout(
				rectImage6.left+padding, rectImage6.top+padding, 
				rectImage6.right-padding, rectImage6.bottom-padding);
		View image7 = getChildAt(6);
		image7.layout(
				rectImage7.left+padding, rectImage7.top+padding, 
				rectImage7.right-padding, rectImage7.bottom-padding);
		View image8 = getChildAt(7);
		image8.layout(
				rectImage8.left+padding, rectImage8.top+padding, 
				rectImage8.right-padding, rectImage8.bottom-padding);
		
	}
	
}
