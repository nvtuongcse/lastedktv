package vn.com.sonca.Touch.SongType;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class TouchSongTypeBackGround extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public TouchSongTypeBackGround(Context context) {
		super(context);
		initView(context);
	}

	public TouchSongTypeBackGround(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSongTypeBackGround(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable shapeDrawable;
	private void initView(Context context) {
		shapeDrawable = getResources().getDrawable(R.drawable.touch_shape_songtype);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (width/6);
		setMeasuredDimension(width, height);

	}
	
	private Rect rectBackground = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectBackground.set(0, 0, w, h);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			shapeDrawable.setBounds(rectBackground);
			shapeDrawable.draw(canvas);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(Color.parseColor("#000000"));
			paintMain.setAlpha(30);
			canvas.drawRect(rectBackground, paintMain);
		}
	}

}
