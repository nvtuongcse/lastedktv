package app.sonca.Dialog.ScoreLayout;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ScoreImageView extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ScoreImageView(Context context) {
		super(context);
		initView(context);
	}

	public ScoreImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ScoreImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawable;
	private void initView(Context context) {
		drawable = getResources().getDrawable(R.drawable.star5);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rectImage = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = (int) (0.5*w);
		int ww = (int) (0.5*130*h/28);
		rectImage.set(tamX-ww, 0, tamX+ww, h);
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.GRAY);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// canvas.drawRect(rectImage, paintMain);
		
		drawable.setBounds(rectImage);
		drawable.draw(canvas);;
		
	}
	

}
