package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.params.Language;
import vn.com.sonca.zzzzz.MyApplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class TouchSongTypeRemainItemView extends View {
	
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path path = new Path();
	//private Context context;
	
	private float widthLayout;
    private float heightLayout;
    private float xPadding;
    private float yPadding;
    
    private float RECTANGLE_STROKE_WIDTH;
    private String RECTANGLE_STROKE_COLOR = "#004e90";
    private String LINE_COLOR = "#00fdfd";
    private float LINE_WIDTH;
    private float LINE_HEIGHT;
    private String TEXT_COLOR = "#b4feff";
    private String text = "";
    private float textDX;
    private float textScale;
    private Drawable drawable; 
    private int padCheckXLeft, padCheckXRight, padCheckY;
    
	public TouchSongTypeRemainItemView(Context context) {
		super(context);
		initView(context);
	}

	public TouchSongTypeRemainItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSongTypeRemainItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		//this.context = context;
	}
	
	private Language lang;
	private boolean isLang = false;
	public void setLanguage(Language lang){
		isLang = true;
		this.lang = lang;
	}
	
	public void setText(String text)
	{
		this.text = text;
//		invalidate();
	}
	
	public String getText()
	{
		return text;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/5);
	    setMeasuredDimension(widthMeasureSpec, myHeight);
	}
	
	private LinearGradient gradient;
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			RECTANGLE_STROKE_COLOR = "#004e90";
			LINE_COLOR = "#00fdfd";
			TEXT_COLOR = "#b4feff";
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			RECTANGLE_STROKE_COLOR = "#004e90";
			LINE_COLOR = "#00fdfd";
			TEXT_COLOR = "#21BAA9";
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {

			xPadding = 0.0f * getHeight();
			yPadding = 0.0f * getHeight();

			widthLayout = getWidth() - xPadding * 2;
			heightLayout = getHeight() - yPadding * 2;

			RECTANGLE_STROKE_WIDTH = getHeight() / 35;
			LINE_WIDTH = LINE_HEIGHT = RECTANGLE_STROKE_WIDTH * 5;

			textDX = widthLayout / 20;
			textScale = heightLayout / 2.5f;

			if (isLang) {
				if (lang.isActive()) {
					gradient = new LinearGradient(0, 0, widthLayout, 0,
							Color.argb(255, 0, 42, 80), Color.argb(255, 1, 76,
									141), TileMode.CLAMP);
					paintSimple.setShader(gradient);
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(),
							getHeight() / 2, paintSimple);

					drawable = getResources().getDrawable(
							R.drawable.image_boder_song);
					drawable.setBounds(0, 0, (int) widthLayout,
							(int) heightLayout);
					drawable.draw(canvas);
				}
			}

			resetPaint();
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(1);
			mPaint.setColor(Color.parseColor(RECTANGLE_STROKE_COLOR));
			// mPaint.setColor(Color.RED);
			// canvas.drawRect(xPadding, yPadding, widthLayout + xPadding,
			// heightLayout + yPadding, mPaint);
			canvas.drawRect(0, 0, widthLayout, heightLayout, mPaint);
			// Top-Left
			PointF point1_draw = new PointF(xPadding, yPadding);
			PointF point2_draw = new PointF(xPadding, yPadding + LINE_HEIGHT);
			// Top-Right
			PointF point3_draw = new PointF(xPadding + widthLayout, yPadding);
			PointF point4_draw = new PointF(xPadding + widthLayout, yPadding
					+ LINE_HEIGHT);
			// Bottom-Left
			PointF point5_draw = new PointF(xPadding, yPadding + heightLayout);
			PointF point6_draw = new PointF(xPadding, yPadding + heightLayout
					- LINE_HEIGHT);
			PointF point7_draw = new PointF(xPadding + LINE_WIDTH, yPadding
					+ heightLayout);
			// Bottom-Right
			PointF point8_draw = new PointF(xPadding + widthLayout, yPadding
					+ heightLayout);
			PointF point9_draw = new PointF(xPadding + widthLayout, yPadding
					+ heightLayout - LINE_HEIGHT);
			PointF point10_draw = new PointF(xPadding + widthLayout
					- LINE_WIDTH, yPadding + heightLayout);

			path.reset();
			path.setFillType(Path.FillType.EVEN_ODD);
			// Rectangle
			path.moveTo(point1_draw.x, point1_draw.y);
			path.lineTo(point5_draw.x, point5_draw.y);
			path.moveTo(point3_draw.x, point3_draw.y);
			path.lineTo(point8_draw.x, point8_draw.y);
			path.moveTo(point5_draw.x, point5_draw.y);
			path.lineTo(point8_draw.x, point8_draw.y);
			path.close();
			canvas.drawPath(path, mPaint);

			path.reset();
			mPaint.setColor(Color.parseColor(LINE_COLOR));
			path.moveTo(point1_draw.x, point1_draw.y);
			path.lineTo(point2_draw.x, point2_draw.y);
			path.moveTo(point3_draw.x, point3_draw.y);
			path.lineTo(point4_draw.x, point4_draw.y);
			path.moveTo(point5_draw.x, point5_draw.y);
			path.lineTo(point6_draw.x, point6_draw.y);
			path.moveTo(point5_draw.x, point5_draw.y);
			path.lineTo(point7_draw.x, point7_draw.y);
			path.moveTo(point8_draw.x, point8_draw.y);
			path.lineTo(point9_draw.x, point9_draw.y);
			path.moveTo(point8_draw.x, point8_draw.y);
			path.lineTo(point10_draw.x, point10_draw.y);

			path.close();
			canvas.drawPath(path, mPaint);

			resetPaint();
			mPaint.setTextSize(textScale);
			mPaint.setColor(Color.parseColor(TEXT_COLOR));
			Rect boundRect = new Rect();
			mPaint.getTextBounds(text, 0, text.length(), boundRect);
			canvas.drawText(text, textDX, 2 * heightLayout / 3, mPaint);

			padCheckXLeft = (int) (150 * heightLayout / 135);
			padCheckXRight = (int) (60 * heightLayout / 135);
			padCheckY = (int) (30 * heightLayout / 135);

			if (isLang) {
				if (lang.isActive()) {
					drawable = getResources()
							.getDrawable(R.drawable.check_icon);
					drawable.setBounds((int) widthLayout - padCheckXLeft,
							padCheckY, (int) widthLayout - padCheckXRight,
							(int) heightLayout - padCheckY);
					drawable.draw(canvas);
				}
			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			xPadding = 0.0f * getHeight();
			yPadding = 0.0f * getHeight();

			widthLayout = getWidth() - xPadding * 2;
			heightLayout = getHeight() - yPadding * 2;

			RECTANGLE_STROKE_WIDTH = getHeight() / 35;
			LINE_WIDTH = LINE_HEIGHT = RECTANGLE_STROKE_WIDTH * 5;

			textDX = widthLayout / 20;
			textScale = heightLayout / 2.5f;

			mPaint.setStyle(Style.FILL);
			mPaint.setColor(Color.parseColor("#E6E7E8"));
			canvas.drawRect(0, 0, widthLayout, heightLayout, mPaint);

			mPaint.setStrokeWidth(2);
			mPaint.setStyle(Style.STROKE);
			mPaint.setColor(Color.parseColor("#C1FFE8"));
			canvas.drawRect(0, 0, widthLayout, heightLayout, mPaint);

			resetPaint();

			Rect boundRect = new Rect();
			mPaint.setAlpha(222);
			mPaint.setTextSize(textScale);
			mPaint.setColor(Color.parseColor("#000000"));
			mPaint.getTextBounds(text, 0, text.length(), boundRect);
			canvas.drawText(text, textDX, 2 * heightLayout / 3, mPaint);
			mPaint.setAlpha(255);

			padCheckXLeft = (int) (150 * heightLayout / 135);
			padCheckXRight = (int) (60 * heightLayout / 135);
			padCheckY = (int) (30 * heightLayout / 135);

			if (isLang) {
				if (lang.isActive()) {
					drawable = getResources()
							.getDrawable(R.drawable.check_icon);
					drawable.setBounds((int) widthLayout - padCheckXLeft,
							padCheckY, (int) widthLayout - padCheckXRight,
							(int) heightLayout - padCheckY);
					drawable.draw(canvas);
				}
			}

		}
	}
/*	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = 100;
	    int desiredHeight = 100;

	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    int width;
	    int height;

	    //Measure Width
	    if (widthMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        width = widthSize;
	    } else if (widthMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        width = Math.min(desiredWidth, widthSize);
	    } else {
	        //Be whatever you want
	        width = desiredWidth;
	    }

	    //Measure Height
	    if (heightMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        height = heightSize;
	    } else if (heightMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        height = Math.min(desiredHeight, heightSize);
	    } else {
	        //Be whatever you want
	        height = desiredHeight;
	    }

	    //MUST CALL THIS
	    setMeasuredDimension(width, height);
	}
*/
    private void resetPaint()
    {
    	mPaint.reset();
    	mPaint.setAntiAlias(true);
    	mPaint.setShader(null);
    }
}