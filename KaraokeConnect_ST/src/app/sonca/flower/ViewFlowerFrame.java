package app.sonca.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class ViewFlowerFrame extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewFlowerFrame(Context context) {
		super(context);
		initView(context);
	}

	public ViewFlowerFrame(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewFlowerFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}
	
	private float Line;
	private int widthLayout;
	private int heightLayout;
	private Rect rectKhung = new Rect();
	private Path pathGoc = new Path();
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;

		int padding = 1;
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
		paintMain.setARGB(255, 0, 253, 253);
		canvas.drawPath(pathGoc, paintMain);
	}

}
