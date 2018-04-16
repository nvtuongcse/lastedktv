package vn.com.sonca.Touch.CustomView;

import java.io.File;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

public class TouchMusicianItemView extends View {
	
	private String sdcard = Environment.getExternalStorageDirectory() + "/Android/data/";
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path path = new Path();
	private Context context;
	
	private int widthLayout;
    private int heightLayout;
    private int xPadding;
    private int yPadding;
    
    private int RECTANGLE_STROKE_WIDTH;
    private static final String RECTANGLE_STROKE_COLOR = "#00485b";
    private static final String RECTANGLE_FILL_COLOR = "#1A000000";
    private static final String LINE_COLOR = "#00fdfd";
    private static final String RECTANGLE_TEXT_FILL_COLOR = "#80000000";
    private static final String TEXT_COLOR = "#00fdfd";
    private static final String TEXT_SHADOW_COLOR = "#59000000";
    private int rectangleHeight;
    private int LINE_WIDTH;
    private int LINE_HEIGHT;
    private Drawable drawable;
    private int drawablePadding;
    private String textName = "";
    private int textScale;
    private int textPadding;
    
	public TouchMusicianItemView(Context context) {
		super(context);
		initView(context);
	}

	public TouchMusicianItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchMusicianItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
	}

	private String pathImage;
	// private String textsaveTam = "";
	public void setData(String name , int cover){
		pathImage = sdcard + context.getPackageName() + "/PICTURE/" + cover;
		// textsaveTam = name;
		textName = name;
		clearData();
	}
	
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		xPadding = (int) 0.0f * getHeight();
		yPadding = (int) 0.0f * getHeight();
		
		widthLayout = getWidth() - xPadding * 2;
		heightLayout = getHeight() - yPadding * 2;
		
		RECTANGLE_STROKE_WIDTH = getHeight() / 70;
		LINE_WIDTH = LINE_HEIGHT = RECTANGLE_STROKE_WIDTH * 5;
		
		drawablePadding = widthLayout / 25;
		rectangleHeight = heightLayout / 4;
		
		textScale = widthLayout / 8;
		textPadding = widthLayout / 60;
		
		resetPaint();
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setColor(Color.parseColor(RECTANGLE_FILL_COLOR));
		canvas.drawRect(xPadding, yPadding, widthLayout + xPadding, heightLayout + yPadding, mPaint);
		mPaint.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.parseColor(RECTANGLE_STROKE_COLOR));
		canvas.drawRect(xPadding, yPadding, widthLayout + xPadding, heightLayout + yPadding, mPaint);
		// Top-Left
		PointF point1_draw = new PointF(xPadding, yPadding);
		PointF point2_draw = new PointF(xPadding, yPadding + LINE_HEIGHT);
		PointF point3_draw = new PointF(xPadding + LINE_WIDTH, yPadding);
		// Top-Right
		PointF point4_draw = new PointF(xPadding + widthLayout, yPadding);
		PointF point5_draw = new PointF(xPadding + widthLayout - LINE_WIDTH, yPadding);
		PointF point6_draw = new PointF(xPadding + widthLayout, yPadding + LINE_HEIGHT);
		// Bottom-Left
		PointF point7_draw = new PointF(xPadding, yPadding + heightLayout);
		PointF point8_draw = new PointF(xPadding, yPadding + heightLayout - LINE_HEIGHT);
		PointF point9_draw = new PointF(xPadding + LINE_WIDTH, yPadding + heightLayout);
		// Bottom-Right
		PointF point10_draw = new PointF(xPadding + widthLayout, yPadding + heightLayout);
		PointF point11_draw = new PointF(xPadding + widthLayout, yPadding + heightLayout - LINE_HEIGHT);
		PointF point12_draw = new PointF(xPadding + widthLayout - LINE_WIDTH, yPadding + heightLayout);
		
		mPaint.setColor(Color.parseColor(LINE_COLOR));
		// mPaint.setMaskFilter(new BlurMaskFilter(RECTANGLE_STROKE_WIDTH * 1.5f, Blur.SOLID));
		path.reset();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(point1_draw.x, point1_draw.y);
		path.lineTo(point2_draw.x, point2_draw.y);
		path.moveTo(point1_draw.x, point1_draw.y);
		path.lineTo(point3_draw.x, point3_draw.y);
		path.moveTo(point4_draw.x, point4_draw.y);
		path.lineTo(point5_draw.x, point5_draw.y);
		path.moveTo(point4_draw.x, point4_draw.y);
		path.lineTo(point6_draw.x, point6_draw.y);
		path.moveTo(point7_draw.x, point7_draw.y);
		path.lineTo(point8_draw.x, point8_draw.y);
		path.moveTo(point7_draw.x, point7_draw.y);
		path.lineTo(point9_draw.x, point9_draw.y);
		path.moveTo(point10_draw.x, point10_draw.y);
		path.lineTo(point11_draw.x, point11_draw.y);
		path.moveTo(point10_draw.x, point10_draw.y);
		path.lineTo(point12_draw.x, point12_draw.y);
		
		path.close();
		canvas.drawPath(path, mPaint);
		
		if (drawable != null)
		{
			int widthS = (getWidth() - 2*(xPadding + drawablePadding));
			int heightS = (getHeight() - 2*(yPadding + drawablePadding));
			/*
			int widthS = 287;
			int heightS = 287;
			*/			
			if(drawable.getMinimumWidth() > widthS  || drawable.getMinimumHeight() > heightS){
				if(drawable.getMinimumWidth() == drawable.getIntrinsicHeight()){
					drawable.setBounds(xPadding + drawablePadding, yPadding + drawablePadding, getWidth() - xPadding - drawablePadding, getHeight() - yPadding - drawablePadding);
				} else if (drawable.getMinimumWidth() > drawable.getIntrinsicHeight()) {
					int he = (int) (widthS*drawable.getMinimumHeight()/drawable.getMinimumWidth());
					drawable.setBounds(xPadding + drawablePadding, getHeight()/2 - he/2, getWidth() - xPadding - drawablePadding, getHeight()/2 + he/2);
				} else if (drawable.getMinimumWidth() < drawable.getIntrinsicHeight()){
					int he = (int) (heightS*drawable.getMinimumWidth()/drawable.getMinimumHeight());
					drawable.setBounds(getWidth()/2 - he/2, yPadding + drawablePadding, getWidth()/2 + he/2, getHeight() - yPadding - drawablePadding);
				}
			}else{
				int w = drawable.getMinimumWidth();
				int h = drawable.getMinimumHeight();
				drawable.setBounds(getWidth()/2 - w/2, getHeight()/2 - h/2, getWidth()/2 + w/2, getHeight()/2 + h/2);
			}
			drawable.draw(canvas);
		}
		
		resetPaint();
		
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setColor(Color.parseColor(RECTANGLE_TEXT_FILL_COLOR));
		canvas.drawRect(xPadding + drawablePadding, getHeight() - yPadding - drawablePadding - rectangleHeight, getWidth() - xPadding - drawablePadding, getHeight() - yPadding - drawablePadding, mPaint);
/*		
		resetPaint();	
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setColor(Color.RED);
		canvas.drawLine(getWidth() - xPadding - drawablePadding, 0, 
				getWidth() - xPadding - drawablePadding , getHeight(), mPaint);
*/		
		mPaint.setTextSize(textScale);
		mPaint.setColor(Color.parseColor(TEXT_COLOR));
		mPaint.setShadowLayer(2.0f, 2.0f, 2.0f, Color.parseColor(TEXT_SHADOW_COLOR));
		textName = cutText(textScale, (float)(0.90*getWidth()) , textName);
		canvas.drawText(textName, xPadding + drawablePadding + textPadding, getHeight() - yPadding - drawablePadding - rectangleHeight / 3, mPaint);
	
		if(drawable == null){
			if (new File(pathImage).exists()) {
				if (loadImage == null) {
					loadImage = new LoadImage(pathImage, textName);
					// loadImage.execute();
					loadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		}
	
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    int myHeight = (int) (parentWidth);
	    super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.EXACTLY));
	}
	
    private void resetPaint()
    {
    	mPaint.reset();
    	mPaint.setAntiAlias(true);
    	// mPaint.setMaskFilter(null);
    }
    
    
    
	private String cutText(float textSize , float maxLength , String content){	
		if(content == null){
			return "";
		}
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(textSize);
		float length = paint.measureText(content);
		if (length > maxLength) {	
			StringBuffer buffer = new StringBuffer("");		
			for (int i = 0; i < content.length() ; i++) {
				length = paint.measureText(buffer.toString() + content.charAt(i) + "...");
				if(length < maxLength){
					buffer.append(content.charAt(i));
				} else {
					break;
				}
			}
			buffer.append("...");
			return buffer.toString();
		} else {
			return content;
		}		
	}
	
/////////////////////////// - LOAD IMAGE - //////////////////////////////////
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clearData();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		clearData();
	}

	private void clearData() {
		if (loadImage != null) {
			loadImage.cancel(true);
			loadImage = null;
		}
		if (drawable != null) {
			drawable = null;
		}
		drawable = null;
	}

	private LoadImage loadImage;

	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private String path;
		private int width, height;
		public LoadImage(String path, String name) {
			// MyLog.i(VIEW_LOG_TAG, path);
			this.path = path;
			textName = name;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				drawable = Drawable.createFromPath(path);
			} catch (Exception ex) {
				ex.printStackTrace();
				if (drawable != null)
					drawable = null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			invalidate();
		}
	}		
	
}