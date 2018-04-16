package app.sonca.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;

public class ViewAvatar extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewAvatar(Context context) {
		super(context);
		initView(context);
	}

	public ViewAvatar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewAvatar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawNu;
	private Drawable drawNam1;
	private Drawable drawNam2;
	private Drawable drawNam3;
	private void initView(Context context) {
		drawNam1 = getResources().getDrawable(R.drawable.kc_pk_nam);
		drawNam2 = getResources().getDrawable(R.drawable.kc_pk_nam1);
		drawNam3 = getResources().getDrawable(R.drawable.kc_pk_nam2);
		drawNu = getResources().getDrawable(R.drawable.kc_pk_nu);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(height, height);
	}
	
	private float Line;
	private int widthLayout;
	private int heightLayout;
	private Rect rectKhung = new Rect();
	private Path pathGoc = new Path();
	private Rect pathImage = new Rect();	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		
		int padding = (int) (0.15*h);
		pathImage.set(padding, padding, w - padding, h - padding);
		
		padding = 1;
		Line = 150*h/1080;
		rectKhung.set(padding, padding, w - padding, h - padding);
		
		pathGoc.reset();
		pathGoc.moveTo(padding , padding + Line);
		pathGoc.lineTo(padding, padding);
		pathGoc.lineTo(padding + Line , padding);
		
		pathGoc.moveTo(widthLayout - padding , padding + Line);
		pathGoc.lineTo(widthLayout - padding , padding);
		pathGoc.lineTo(widthLayout - Line - padding , padding);
		
		pathGoc.moveTo(widthLayout - padding , heightLayout - Line - padding);
		pathGoc.lineTo(widthLayout - padding, heightLayout - padding);
		pathGoc.lineTo(widthLayout - Line - padding , heightLayout - padding);
		
		pathGoc.moveTo(padding , heightLayout - Line - padding);
		pathGoc.lineTo(padding , heightLayout - padding);
		pathGoc.lineTo(padding + Line , heightLayout - padding);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paintMain.setStyle(Style.STROKE);
		paintMain.setStrokeWidth(1);
		paintMain.setARGB(255, 0, 78, 144);
		canvas.drawRect(rectKhung, paintMain);
		if(isActive == true){
			paintMain.setARGB(255, 0, 253, 253);
			canvas.drawPath(pathGoc, paintMain);
		}
		switch (avatar) {
		case 0:
			drawNu.setBounds(pathImage);
			drawNu.draw(canvas);
			break;
		case 1:
			drawNam1.setBounds(pathImage);
			drawNam1.draw(canvas);
			break;
		case 2:
			drawNam2.setBounds(pathImage);
			drawNam2.draw(canvas);
			break;
		case 3:
			drawNam3.setBounds(pathImage);
			drawNam3.draw(canvas);
			break;
		default:
			break;
		}
		
	}
	
	private boolean isActive = false;
	public void setActiveView(boolean isActive){
		this.isActive = isActive;
		invalidate();
	}
	
	private int avatar = 0;
	public void setAvatar(int avatar){
		this.avatar = avatar;
		invalidate();
	}
	
	private int position = -1;
	public void setPosition(int position){
		this.position = position;
	}
	
	public int getPosition(){
		return position;
	}
	

}
