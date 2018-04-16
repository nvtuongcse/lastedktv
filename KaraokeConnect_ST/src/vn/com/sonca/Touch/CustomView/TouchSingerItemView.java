package vn.com.sonca.Touch.CustomView;

import java.io.File;
import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.utils.PinyinHelper;
import vn.com.sonca.zzzzz.MyApplication;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.Layout.Alignment;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchSingerItemView extends View {
	private String sdcard = Environment.getExternalStorageDirectory() + "/Android/data/";
//	private String sdcard = "";
	
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Path path = new Path();
	private Context context;
	
	private int widthLayout;
    private int heightLayout;
    private int xPadding;
    private int yPadding;
    
    private int RECTANGLE_STROKE_WIDTH;
    private String RECTANGLE_STROKE_COLOR = "#00485b";    
    private String RECTANGLE_FILL_COLOR = "#1A000000";
    private String LINE_COLOR = "#00fdfd";
    private String RECTANGLE_TEXT_FILL_COLOR = "#80000000";
    private String TEXT_COLOR = "#00fdfd";
    private String TEXT_SHADOW_COLOR = "#59000000";
    private int rectangleHeight;
    private int LINE_WIDTH;
    private int LINE_HEIGHT;
    private Drawable drawable = null;
    private int drawablePadding;
    private String textName = "";
    private int textScale;
    private int textPadding;
    /*
    private OnSingerItemListener listener;
    
	public interface OnSingerItemListener {
		public void OnClick(String singerName);
	}
	
	public void setOnSingerItemListener(OnSingerItemListener listener){
		this.listener = listener;
	}
	*/
	public TouchSingerItemView(Context context) {
		super(context);
		initView(context);
	}

	public TouchSingerItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSingerItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
	}
	
	private String pathImage;
	private String search;
	public void setData(String name , String search, int cover){
		pathImage = sdcard + context.getPackageName() + "/PICTURE/" + cover;
		this.search = search;
		textName = name;
		clearData();
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			RECTANGLE_STROKE_COLOR = "#00485b";    
		    RECTANGLE_FILL_COLOR = "#1A000000";
		    LINE_COLOR = "#00fdfd";
		    RECTANGLE_TEXT_FILL_COLOR = "#80000000";
		    TEXT_COLOR = "#00fdfd";
		    TEXT_SHADOW_COLOR = "#59000000";
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			RECTANGLE_STROKE_COLOR = "#005249";    
		    RECTANGLE_FILL_COLOR = "#1A000000";
		    LINE_COLOR = "#005249";
		    RECTANGLE_TEXT_FILL_COLOR = "#80000000";
		    TEXT_COLOR = "#ffffff";
		    TEXT_SHADOW_COLOR = "#59000000";
		}
			
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			// DRAW 0
			canvas.translate(0, 0);

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
			/*
			 * mPaint.setStyle(Style.FILL_AND_STROKE);
			 * mPaint.setColor(Color.parseColor(RECTANGLE_FILL_COLOR));
			 * canvas.drawRect(xPadding, yPadding, widthLayout + xPadding,
			 * heightLayout + yPadding, mPaint);
			 */
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
			// mPaint.setMaskFilter(new BlurMaskFilter(RECTANGLE_STROKE_WIDTH *
			// 1.5f, Blur.SOLID));
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

			/*
			 * mPaint.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
			 * mPaint.setStyle(Style.FILL); mPaint.setARGB(255, 0, 0, 0);
			 * canvas.drawRect(xPadding + drawablePadding, yPadding +
			 * drawablePadding, getWidth() - xPadding - drawablePadding,
			 * getHeight() - yPadding - drawablePadding , mPaint);
			 */
			// drawable =
			// getResources().getDrawable(R.drawable.nguyen_van_chung);
			if (drawable != null) {
				int widthS = (getWidth() - 2 * (xPadding + drawablePadding));
				int heightS = (getHeight() - 2 * (yPadding + drawablePadding));
				/*
				 * int widthS = 287; int heightS = 287;
				 */

				if (drawable.getMinimumWidth() > widthS || drawable.getMinimumHeight() > heightS) {
					if (drawable.getMinimumWidth() == drawable.getIntrinsicHeight()) {
						drawable.setBounds(xPadding + drawablePadding, yPadding + drawablePadding,
								getWidth() - xPadding - drawablePadding, getHeight() - yPadding - drawablePadding);
					} else if (drawable.getMinimumWidth() > drawable.getIntrinsicHeight()) {
						int he = (int) (widthS * drawable.getMinimumHeight() / drawable.getMinimumWidth());
						drawable.setBounds(xPadding + drawablePadding, getHeight() / 2 - he / 2,
								getWidth() - xPadding - drawablePadding, getHeight() / 2 + he / 2);
					} else if (drawable.getMinimumWidth() < drawable.getIntrinsicHeight()) {
						int he = (int) (heightS * drawable.getMinimumWidth() / drawable.getMinimumHeight());
						drawable.setBounds(getWidth() / 2 - he / 2, yPadding + drawablePadding, getWidth() / 2 + he / 2,
								getHeight() - yPadding - drawablePadding);
					}
				} else {
					int w = drawable.getMinimumWidth();
					int h = drawable.getMinimumHeight();
					drawable.setBounds(getWidth() / 2 - w / 2, getHeight() / 2 - h / 2, getWidth() / 2 + w / 2,
							getHeight() / 2 + h / 2);
				}
				drawable.draw(canvas);
			}

			resetPaint();
			mPaint.setStyle(Style.FILL_AND_STROKE);
			mPaint.setColor(Color.parseColor(RECTANGLE_TEXT_FILL_COLOR));
			canvas.drawRect(xPadding + drawablePadding, getHeight() - yPadding - drawablePadding - rectangleHeight,
					getWidth() - xPadding - drawablePadding, getHeight() - yPadding - drawablePadding, mPaint);

			resetPaint();

			textPaint.setTextSize(textScale);
			textPaint.setColor(Color.parseColor(TEXT_COLOR));
			textPaint.setShadowLayer(2.0f, 2.0f, 2.0f, Color.parseColor(TEXT_SHADOW_COLOR));
			// textName = cutText(textScale, (float)(0.90*getWidth()) ,
			// textName);
			if (drawable == null) {
				if (loadImage == null) {
					loadImage = new LoadImage(pathImage, textName, search);
					// loadImage.execute();
					loadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				String stringname = "";
				int widthSong = (int) (0.9 * widthLayout);
				int sizeSong = (int) textPaint.measureText(textName + "...");
				if (sizeSong > widthSong) {
					float f = textPaint.measureText("...");
					int i = textPaint.breakText(textName, true, widthSong - f, null);
					stringname = textName.substring(0, i) + "...";
				} else {
					stringname = textName;
				}
				canvas.drawText(stringname, xPadding + drawablePadding + textPadding,
						getHeight() - yPadding - drawablePadding - rectangleHeight / 3, textPaint);
			} else {
				builder.clear();
				int widthSong = (int) (0.9 * widthLayout);
				int sizeSong = (int) textPaint.measureText(wordtoSpan.toString() + "...");
				if (sizeSong > widthSong) {
					float f = textPaint.measureText("...");
					int i = textPaint.breakText(wordtoSpan.toString(), true, widthSong - f, null);
					builder.append(wordtoSpan.subSequence(0, i));
					builder.append("...");
				} else {
					builder.append(wordtoSpan);
				}
				StaticLayout layout = new StaticLayout(builder, textPaint, widthLayout, Alignment.ALIGN_NORMAL, 1, 0,
						true);
				canvas.translate(transX, transY);
				layout.draw(canvas);
			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			// DRAW 0
			canvas.translate(0, 0);

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
			canvas.drawRect(xPadding, yPadding, widthLayout + xPadding,heightLayout + yPadding, mPaint);
			
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
			// mPaint.setMaskFilter(new BlurMaskFilter(RECTANGLE_STROKE_WIDTH *
			// 1.5f, Blur.SOLID));
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

			/*
			 * mPaint.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
			 * mPaint.setStyle(Style.FILL); mPaint.setARGB(255, 0, 0, 0);
			 * canvas.drawRect(xPadding + drawablePadding, yPadding +
			 * drawablePadding, getWidth() - xPadding - drawablePadding,
			 * getHeight() - yPadding - drawablePadding , mPaint);
			 */
			// drawable =
			// getResources().getDrawable(R.drawable.nguyen_van_chung);
			if (drawable != null) {
				int widthS = (getWidth() - 2 * (xPadding + drawablePadding));
				int heightS = (getHeight() - 2 * (yPadding + drawablePadding));
				/*
				 * int widthS = 287; int heightS = 287;
				 */

				if (drawable.getMinimumWidth() > widthS || drawable.getMinimumHeight() > heightS) {
					if (drawable.getMinimumWidth() == drawable.getIntrinsicHeight()) {
						drawable.setBounds(xPadding + drawablePadding, yPadding + drawablePadding,
								getWidth() - xPadding - drawablePadding, getHeight() - yPadding - drawablePadding);
					} else if (drawable.getMinimumWidth() > drawable.getIntrinsicHeight()) {
						int he = (int) (widthS * drawable.getMinimumHeight() / drawable.getMinimumWidth());
						drawable.setBounds(xPadding + drawablePadding, getHeight() / 2 - he / 2,
								getWidth() - xPadding - drawablePadding, getHeight() / 2 + he / 2);
					} else if (drawable.getMinimumWidth() < drawable.getIntrinsicHeight()) {
						int he = (int) (heightS * drawable.getMinimumWidth() / drawable.getMinimumHeight());
						drawable.setBounds(getWidth() / 2 - he / 2, yPadding + drawablePadding, getWidth() / 2 + he / 2,
								getHeight() - yPadding - drawablePadding);
					}
				} else {
					int w = drawable.getMinimumWidth();
					int h = drawable.getMinimumHeight();
					drawable.setBounds(getWidth() / 2 - w / 2, getHeight() / 2 - h / 2, getWidth() / 2 + w / 2,
							getHeight() / 2 + h / 2);
				}
				drawable.draw(canvas);
			}

			resetPaint();
			mPaint.setStyle(Style.FILL_AND_STROKE);
			mPaint.setColor(Color.parseColor(RECTANGLE_TEXT_FILL_COLOR));
			canvas.drawRect(xPadding + drawablePadding, getHeight() - yPadding - drawablePadding - rectangleHeight,
					getWidth() - xPadding - drawablePadding, getHeight() - yPadding - drawablePadding, mPaint);

			resetPaint();

			textPaint.setTextSize(textScale);
			textPaint.setColor(Color.parseColor(TEXT_COLOR));
			textPaint.setShadowLayer(2.0f, 2.0f, 2.0f, Color.parseColor(TEXT_SHADOW_COLOR));
			// textName = cutText(textScale, (float)(0.90*getWidth()) ,
			// textName);
			if (drawable == null) {
				if (loadImage == null) {
					loadImage = new LoadImage(pathImage, textName, search);
					// loadImage.execute();
					loadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				String stringname = "";
				int widthSong = (int) (0.9 * widthLayout);
				int sizeSong = (int) textPaint.measureText(textName + "...");
				if (sizeSong > widthSong) {
					float f = textPaint.measureText("...");
					int i = textPaint.breakText(textName, true, widthSong - f, null);
					stringname = textName.substring(0, i) + "...";
				} else {
					stringname = textName;
				}
				canvas.drawText(stringname, xPadding + drawablePadding + textPadding,
						getHeight() - yPadding - drawablePadding - rectangleHeight / 3, textPaint);
			} else {
				builder.clear();
				int widthSong = (int) (0.9 * widthLayout);
				int sizeSong = (int) textPaint.measureText(wordtoSpan.toString() + "...");
				if (sizeSong > widthSong) {
					float f = textPaint.measureText("...");
					int i = textPaint.breakText(wordtoSpan.toString(), true, widthSong - f, null);
					builder.append(wordtoSpan.subSequence(0, i));
					builder.append("...");
				} else {
					builder.append(wordtoSpan);
				}
				StaticLayout layout = new StaticLayout(builder, textPaint, widthLayout, Alignment.ALIGN_NORMAL, 1, 0,
						true);
				canvas.translate(transX, transY);
				layout.draw(canvas);
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
    /*
    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	switch (event.getAction())
    	{
    	case MotionEvent.ACTION_UP:
    		if (event.getX() >= 0 && event.getX() <= getWidth() && event.getY() >= 0 && event.getY() <= getHeight())
    		{
    			if (listener != null) {
    				listener.OnClick(text);
    			}
    		}
    		return true;
    	}
    	return false;
    }
    */
    
	private float transX;
	private float transY;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	transX = (float) (0.05*w);
		transY = (float) (0.75*h);
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
	
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
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
	}

	private LoadImage loadImage;
	private Spannable wordtoSpan = null;
	private SpannableStringBuilder builder = new SpannableStringBuilder();
	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private String path;
		private String name = "";
		private String seach = "";
		private int width, height;
		public LoadImage(String path, String name , String search) {
			this.path = path;
			this.name = name;
			this.seach = search;
		}
		
		private int getIndex(String string , int index){
			switch (string.charAt(index)) {
			case '(': 	return index + 1; 
			case '`': 	return index + 1; 
			case '[': 	return index + 1; 
			default:	return index;
			}
		}
		
		private Spannable createSpannableChinese(String name , String where, int color){
			Spannable wordtoSpan = new SpannableString(name);
			int countIdx = 0;
			for (int i = 0; i < name.length(); i++) {
				if (countIdx >= where.length()) {
					break;
				}
				String strChar = name.substring(i, i + 1);
				String strSearchChar = where.substring(countIdx, countIdx + 1);
				if (strChar.equals(strSearchChar)) {
					wordtoSpan.setSpan(new ForegroundColorSpan(color), 
							i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					countIdx++;
				} else {
					if (PinyinHelper.checkChinese(strChar)) {
						wordtoSpan.setSpan(new ForegroundColorSpan(color), 
								i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						countIdx++;
					} else {
						continue;
					}
				}
			}
			return wordtoSpan;
		}
		
		private Spannable createSpannable(String textSong , String nameraw , String textSearch, int color){
			textSearch.trim();
			ArrayList<Integer> listOffset = new ArrayList<Integer>();
			Spannable wordtoSpan;
			if(textSong.equals("")){
				wordtoSpan = new SpannableString("");
			}else{
				if(textSearch.equals("")){
					return new SpannableString(textSong);
				}
				textSearch.trim();
				String newString = textSong.replaceAll("[ &+=_,-/]", "*");
				StringBuffer buffer = new StringBuffer(newString);
					//-------------//
				String[] strings = buffer.toString().split("[*]");
				if(strings.length < textSearch.length()){
					int offset = nameraw.indexOf(textSearch);
					wordtoSpan = new SpannableString(textSong);
					if(offset != -1){
						wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, 
								offset + textSearch.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					return wordtoSpan;
				}
				int count = 0;
	 			for (int i = 0; i < strings.length; i++) {
	 				int size = strings[i].length();
	 				if(size <= 0){
	 					count += 1;
	 				}else{
	 					listOffset.add(count);
	 					count += size + 1;
	 				}
	 				if(listOffset.size() >= textSearch.length()){
	 					break;
	 				}
				}
	 				//-------------//
				wordtoSpan = new SpannableString(textSong);
				for (int i = 0; i < listOffset.size(); i++) {
					int offset = getIndex(textSong, listOffset.get(i));
					if(nameraw.charAt(offset) != textSearch.charAt(i)){
						int of = nameraw.indexOf(textSearch);
						SpannableString word = new SpannableString(textSong);
						if(of != -1){
							word.setSpan(new ForegroundColorSpan(color), of, 
									of + textSearch.length(),
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						return word;
					}else{
						wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, offset + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}	
			return wordtoSpan;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			int color = Color.GREEN;
			if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
				color = Color.GREEN;
			} else if (MyApplication.intColorScreen == MyApplication.SCREEN_GREEN){
				color = Color.argb(255, 250, 145, 0);
			}
			String nameraw = PinyinHelper.replaceAll(name);
			if (seach.equals("")) {
				wordtoSpan = createSpannable(name, nameraw, seach, color);
			} else {
				CharSequence wh1 = name.subSequence(0, 1);
				CharSequence wh2 = name.subSequence(name.length() - 1, name.length());
				if (PinyinHelper.checkChinese(wh1.toString()) || 
						PinyinHelper.checkChinese(wh2.toString())) {
					if(name.contains(seach)){
						wordtoSpan = createSpannable(name, nameraw, seach, color);
					}else{
						wordtoSpan = createSpannableChinese(name, seach, color);
					}
				} else {
					wordtoSpan = createSpannable(name, nameraw, seach, color);
				}
			}
			try {
				if(new File(path).exists()){
					drawable = Drawable.createFromPath(path);
				}else{
					drawable = getResources().getDrawable(R.raw.a1);
				}
			} catch (Exception ex) {
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