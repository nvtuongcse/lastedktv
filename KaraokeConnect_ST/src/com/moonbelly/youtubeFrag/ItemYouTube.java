package com.moonbelly.youtubeFrag;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;













import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class ItemYouTube extends View{

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private int widthLayout;
	private int heightLayout;
	
	private Path path = new Path();
	
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
	private Drawable drawable = null;
	private int drawablePadding;
	private int textScale;
	private int textPadding;
	
	private Context context;
	
	public ItemYouTube(Context context) {
		super(context);
		this.context = context;
	}
	
	public ItemYouTube(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		this.context = context;
		initView();
	}

	public ItemYouTube(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	
	private OnItemYouTubeListener listener;

	public interface OnItemYouTubeListener {		
		public void onClickYouTube(MyYouTubeInfo info, int type, int position, float x, float y);
		public void onClickXemTruoc(MyYouTubeInfo info);
		public void onCallPopup(int position, View v);
	}

	public void setOnItemYouTubeListener(OnItemYouTubeListener listener) {
		this.listener = listener;
	}
	
	private int position;
	private MyYouTubeInfo info;
	private String imgDirPath;
	
	private Drawable drawBG, drawBG_Active, draw3Cham, drawPopup, drawFirst, drawXemTruoc;
	
	private void initView(){
		String rootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						context.getPackageName()));
		
		imgDirPath = rootPath.concat("/YTP"); 
		
		drawBG = getResources().getDrawable(R.drawable.boder_tablet_yt_inactive);
		drawBG_Active = getResources().getDrawable(R.drawable.boder_tablet_yt_active);
		draw3Cham = getResources().getDrawable(R.drawable.icon_3cham_yt_tablet);
		drawPopup = getResources().getDrawable(R.drawable.boder_tablet_popup);
		drawFirst = getResources().getDrawable(R.drawable.first_60x60);
		drawXemTruoc = getResources().getDrawable(R.drawable.youtube_xemtruoc);
		
	}
	
	public void setContentView(int position, MyYouTubeInfo info){
		this.position = position;
		this.info = info;
		clearData();
		loadImage = new LoadImage();
		invalidate();
	}	
	
	public void setItemInfo(MyYouTubeInfo info){
		this.info = info;
		invalidate();
	}
	
	public MyYouTubeInfo getItemInfo(){
		return this.info;
	}
	
	public void clearImage(){
		MyLog.e("clearImage", "clearImage");
		clearData();
		loadImage = new LoadImage();
		invalidate();
	}
	
	public boolean isFlagOpenPopup() {
		return flagOpenPopup;
	}

	public void setFlagOpenPopup(boolean flagOpenPopup) {
		this.flagOpenPopup = flagOpenPopup;
		invalidate();
	}

	private int ordinarly = -1;
	
	public void setOrdinarly(int ordinarly){
		this.ordinarly = ordinarly;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int myWidth = MeasureSpec.getSize(widthMeasureSpec);
//		int myHeight = 270 * (myWidth) / 240;
//		super.onMeasure(
//				MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.EXACTLY),
//				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.EXACTLY));
		
		int myWidth = MeasureSpec.getSize(widthMeasureSpec);
	    int myHeight = 270 * (myWidth) / 240;
	    super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.EXACTLY));
	}
	
	private int myPadding;
	private Rect rectImage, rectDur, rect3Cham, rectPopup, rectFirst, rectXemTruoc, rectOrdinarly;
	private float maxTitleWidth;
	
	private float KTS1, KT1Y, KT2Y, KT2Y2;
	private float KTS2, KT3Y, KTS3, KT4Y, KTS4, KT5Y, KT5YB;
	private int durSpace;
	
	private float KTS5, KT6Y, KT7Y;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		heightLayout = h;
		
		xPadding = (int) 0.0f * getWidth();
		yPadding = (int) 0.0f * getHeight();

		widthLayout = getWidth() - xPadding * 2;
		heightLayout = getHeight() - yPadding * 2;

		RECTANGLE_STROKE_WIDTH = getHeight() / 70;
		LINE_WIDTH = LINE_HEIGHT = RECTANGLE_STROKE_WIDTH * 3;
	
		// Top-Left
		PointF point1_draw = new PointF(xPadding, yPadding);
		PointF point2_draw = new PointF(xPadding, yPadding + LINE_HEIGHT);
		PointF point3_draw = new PointF(xPadding + LINE_WIDTH, yPadding);
		// Top-Right
		PointF point4_draw = new PointF(xPadding + widthLayout, yPadding);
		PointF point5_draw = new PointF(xPadding + widthLayout - LINE_WIDTH,
				yPadding);
		PointF point6_draw = new PointF(xPadding + widthLayout, yPadding
				+ LINE_HEIGHT);
		// Bottom-Left
		PointF point7_draw = new PointF(xPadding, yPadding + heightLayout);
		PointF point8_draw = new PointF(xPadding, yPadding + heightLayout
				- LINE_HEIGHT);
		PointF point9_draw = new PointF(xPadding + LINE_WIDTH, yPadding
				+ heightLayout);
		// Bottom-Right
		PointF point10_draw = new PointF(xPadding + widthLayout, yPadding
				+ heightLayout);
		PointF point11_draw = new PointF(xPadding + widthLayout, yPadding
				+ heightLayout - LINE_HEIGHT);
		PointF point12_draw = new PointF(xPadding + widthLayout - LINE_WIDTH,
				yPadding + heightLayout);

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
		
		// width = 240; height = 280

		myPadding = 10 * getWidth() / 240;
			
		float offsetX = (float) (0.5*w);
		float vuongW = (w - 2 * myPadding) / 2;
		float vuongH = vuongW * 80 / 120;
		rectImage = new Rect(
				(int)(offsetX - vuongW), 
				(int)myPadding, 
				(int)(offsetX + vuongW), 
				(int)(myPadding + 2 * vuongH));
		
		maxTitleWidth = w - 2 * myPadding;
		
		KTS1 = 18 * h / 280;
		KT1Y = rectImage.bottom + KTS1 + 2 * h / 280;
		KT2Y = KT1Y + KTS1 + 4 * h / 280;
		KT2Y2 = KT2Y + KTS1 + 4 * h / 280;
		
		KTS2 = 17 * h / 280;
		KT3Y = KT2Y2 + KTS2 + 8 * h / 280;
		
		KTS3 = 17 * h / 280;
		KT4Y = KT3Y + KTS3 + 8 * h / 280;
	
		KTS4 = 20 * h / 280;
		KT5Y = rectImage.bottom - 15 * h / 280;
		KT5YB = rectImage.top + KTS4 + 10 * h / 280;
		
		rectDur = new Rect();
		rectOrdinarly = new Rect();
		durSpace = 10 * w / 280;
		
		vuongW = 5 * w / 240;
		vuongH = vuongW * 32 / 6;
		rect3Cham = new Rect(
				(int)(w - myPadding - vuongW), 
				(int)(h - myPadding - vuongH), 
				(int)(w - myPadding), 
				(int)(h - myPadding));	
		
		offsetX = (float) (150*w/240);
		float offsetY = (float) (220*h/280);
		vuongH = 45 * h / 280;
		vuongW = 2 * vuongH;
		rectPopup = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (100*w/240);
		offsetY = (float) (200*h/280);
		vuongW = 17 * w / 240;
		vuongH = vuongW;
		rectFirst = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (100*w/240);
		offsetY = (float) (245*h/280);
		vuongW = 17 * w / 240;
		vuongH = 43 * vuongW / 50;
		rectXemTruoc = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		KTS5 = 24 * h / 280;
		KT6Y = 207*h/280;
		KT7Y = 250*h/280;
		
	}
	
	private void resetPaint() {
		mPaint.reset();
		mPaint.setAntiAlias(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
				
		if(ordinarly >= 0){
			drawBG_Active.setBounds(0,0,widthLayout,heightLayout);
			drawBG_Active.draw(canvas);
		} else {
			drawBG.setBounds(0,0,widthLayout,heightLayout);
			drawBG.draw(canvas);
		}
		
		// BORDER
		resetPaint();
		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setColor(Color.parseColor(RECTANGLE_FILL_COLOR));
		canvas.drawRect(xPadding, yPadding, widthLayout + xPadding,
				heightLayout + yPadding, mPaint);

		mPaint.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(Color.parseColor(RECTANGLE_STROKE_COLOR));
		canvas.drawRect(xPadding, yPadding, widthLayout + xPadding,
				heightLayout + yPadding, mPaint);

		mPaint.setColor(Color.parseColor(LINE_COLOR));
		canvas.drawPath(path, mPaint);		

//		mainPaint.setStyle(Style.FILL);
//		mainPaint.setARGB(255, 1, 68, 127);
//		canvas.drawRect(0 + myPadding, 0 + myPadding, getWidth() - myPadding,
//				getHeight() - myPadding, mainPaint);

		resetPaint();

		if (drawImage != null) {
			drawImage.setBounds(rectImage);
			drawImage.draw(canvas);
		} else {
			if (!info.getImageLink().equals("")) {
				try {
					loadImage.execute();
				} catch (Exception e) {

				}

			}
		}
		
		// TITLE
		mainText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
		mainText.setStyle(Style.FILL);
		mainText.setTextSize(KTS1);
		mainText.setARGB(255, 255, 238, 0);
		String strTitle = info.getTitle();
		String line1 = cutTextNoDot(mainText, maxTitleWidth, strTitle);
		canvas.drawText(line1, rectImage.left, KT1Y, mainText);
		
		if(line1.length() < strTitle.length()){
			String line2 = strTitle.substring(line1.length());
			String lineM = cutTextNoDot(mainText, maxTitleWidth, line2);
			canvas.drawText(lineM, rectImage.left, KT2Y, mainText);
			
			if(lineM.length() < line2.length()){
				String line3 = line2.substring(lineM.length());
				line3 = cutText(mainText, maxTitleWidth, line3);
				canvas.drawText(line3, rectImage.left, KT2Y2, mainText);
			}
		}
		
		// CHANNEL TITLE
		mainText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		mainText.setStyle(Style.FILL);
		mainText.setTextSize(KTS2);
		mainText.setARGB(255, 244, 67, 54);
		String str = info.getChannelTitle();
		str =  cutText(mainText, maxTitleWidth, str);
		canvas.drawText(str, rectImage.left, KT3Y, mainText);
		
		// VIEW COUNT
		if(info.getViewerCount() != 0){
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(KTS3);
			mainText.setARGB(255, 0, 190, 252);
			str = info.getStrViewCount() + " " + getResources().getString(R.string.real_yt_1);
			str = cutText(mainText, maxTitleWidth, str);
			canvas.drawText(str, rectImage.left, KT4Y, mainText);	
		}		
		
		// DURATION
		if(info.getDuration() != 0){
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(KTS4);
			mainText.setARGB(255, 180, 254, 255);
			str = info.getStrDur();
			float mWidth = mainText.measureText(str);
			float mTextXLeft = rectImage.right - 15 * widthLayout / 240 - mWidth;		
			float mTextRight = mTextXLeft + mWidth;
			
			rectDur = new Rect((int) (mTextXLeft - durSpace),
					(int) (KT5Y - KTS4 - 2 * durSpace / 3), (int) (mTextRight + durSpace),
					(int) (KT5Y + durSpace));
			mainPaint.setStyle(Style.FILL);
			mainPaint.setARGB(128, 0, 0, 0);
			canvas.drawRect(rectDur, mainPaint);

			canvas.drawText(str, mTextXLeft, KT5Y, mainText);
		}
		
		if(ordinarly >= 0){
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(KTS4);
			mainText.setARGB(255, 0, 255, 0);
			str = (ordinarly + 1) + "";
			float mTextXLeft = rectImage.left + 13 * widthLayout / 240;
			float mWidth = mainText.measureText(str);
			float mTextRight = mTextXLeft + mWidth;
			
			str = getResources().getString(R.string.real_yt_4);
			mTextRight += 10 * widthLayout / 240 + mainText.measureText(str);
			
			rectOrdinarly = new Rect((int) (mTextXLeft - durSpace),
					(int) (KT5YB - KTS4 - 2 * durSpace / 3), (int) (mTextRight + durSpace),
					(int) (KT5YB + durSpace));
			mainPaint.setStyle(Style.FILL);
			mainPaint.setARGB(128, 0, 0, 0);
			canvas.drawRect(rectOrdinarly, mainPaint);
			
			str = (ordinarly + 1) + "";
			canvas.drawText(str, mTextXLeft, KT5YB, mainText);
			
			mainText.setARGB(255, 180, 254, 255);
			str = getResources().getString(R.string.real_yt_4);
			canvas.drawText(str, mTextXLeft + mWidth + 10 * widthLayout / 240, KT5YB, mainText);
			
		}
		
		// 3 CHAM
		draw3Cham.setBounds(rect3Cham);
		draw3Cham.draw(canvas);
		
		if(flagOpenPopup){
			drawPopup.setBounds(rectPopup);
			drawPopup.draw(canvas);
			
			drawFirst.setBounds(rectFirst);
			drawFirst.draw(canvas);
			
			drawXemTruoc.setBounds(rectXemTruoc);
			drawXemTruoc.draw(canvas);
			
			mainText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(KTS5);
			mainText.setARGB(255, 180, 254, 255);
			str = getResources().getString(R.string.real_yt_2);
			canvas.drawText(str, rectXemTruoc.right + 5 * widthLayout / 240, KT6Y, mainText);
			
			str = getResources().getString(R.string.real_yt_3);
			canvas.drawText(str, rectXemTruoc.right + 5 * widthLayout / 240, KT7Y, mainText);
			
		}
	}
	
	private float offsetTouchX = -1;
	private float offsetTouchY = -1;
	
	private boolean flagOpenPopup = false;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		offsetTouchX = event.getRawX();
		offsetTouchY = event.getRawY();
		float offsetX = event.getX();
		float offsetY = event.getY();
		
		if (event.getAction() == MotionEvent.ACTION_UP){
			
			if(flagOpenPopup){
				if (offsetX > widthLayout - myPadding - 3 * rect3Cham.width() && offsetY > rect3Cham.top) {
					flagOpenPopup = false;
					invalidate();
					MyApplication.flagPopupYouTube = false;
					return true;
				}
			} else {
				if (offsetX > 2 * widthLayout / 3 && offsetY > rect3Cham.top) {
					flagOpenPopup = true;
					invalidate();
					
					MyApplication.flagPopupYouTube = true;
					if(listener != null){						
						listener.onCallPopup(position, this);
					}					
					return true;
				}
			}
			
			if(flagOpenPopup){				
				if(offsetX > rectPopup.left && offsetX < rectPopup.right && offsetY > rectPopup.top && offsetY < rectPopup.bottom){
					if(offsetX > rectPopup.left && offsetX < rectPopup.right && offsetY > rectPopup.top && offsetY < rectFirst.bottom){
						if(listener != null){
							listener.onClickYouTube(info, 1, -1, offsetTouchX, offsetTouchY); // first
						}
						flagOpenPopup = false;
						MyApplication.flagPopupYouTube = false;
						invalidate();
						return true;
					}
					
					if(offsetX > rectPopup.left && offsetX < rectPopup.right && offsetY > rectXemTruoc.top && offsetY < rectPopup.bottom){
						if(listener != null){
							listener.onClickXemTruoc(info); // xem truoc
						}
						flagOpenPopup = false;
						MyApplication.flagPopupYouTube = false;
						invalidate();
						return true;
					}
					
				} else {
					flagOpenPopup = false;
					MyApplication.flagPopupYouTube = false;
					invalidate();
				}
			} else {
				if(listener != null){
					listener.onClickYouTube(info, 0, position, offsetTouchX, offsetTouchY); // add
				}
			}
			
		}
		
		return true;
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
		
		if(drawImage != null){
			drawImage = null;
		}
	}
	
	private Drawable drawImage;
	private LoadImage loadImage;
	private class LoadImage extends AsyncTask<Void, Void, Void> {

		public LoadImage() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {	
				String path = imgDirPath + "/" + position;
				
				Bitmap bitmap;
				if (new File(path).exists()) {
//					MyLog.d("file exist", "file exist");
					bitmap = BitmapFactory.decodeFile(path);
					if (isCancelled()) {
						flagFinishImage = false;
						if (bitmap != null)
							bitmap.recycle();
						return null;
					}
					flagFinishImage = true;
				} else {
//					MyLog.d("file not exist", "file not exist");
//					bitmap = BitmapFactory.decodeResource(getResources(),
//							R.raw.a1);
					flagFinishImage = false;
					return null;
				}
				
				drawImage = new BitmapDrawable(getResources(), bitmap);
				if (isCancelled()) {
					flagFinishImage = false;
					if (drawImage != null)
						drawImage = null;
					return null;
				}
				
			} catch (Exception ex) {
				flagFinishImage = false;
				if (drawImage != null)
					drawImage = null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			invalidate();
		}

	}

	private boolean flagFinishImage = false;
	public boolean isFlagFinishImage(){
		return this.flagFinishImage;
	}
	
/////////////////////////// - CUT TEXT - //////////////////////////////////
	
	private String cutTextNoDot(Paint paint, float maxLength, String content) {
		if (content == null || content.equals("")) {
			return "";
		}
		if (maxLength == 0)
			return content;
		float length = paint.measureText(content);
		if (length > maxLength) {
			String[] strings = content.split(" ");
			if (strings.length > 1) {
				return cutTextWordNoDot(paint.getTextSize(), maxLength,
						strings, paint);
			} else {
				return content;
			}
		} else {
			return content;
		}
	}

	private String cutTextWordNoDot(float textSize, float maxLength,
			String[] content, Paint paint) {
		float length = 0;
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < content.length; i++) {
			length = paint.measureText(buffer.toString() + " " + content[i]);
			if (length < maxLength) {
				buffer.append(content[i] + " ");
			} else {
				break;
			}
		}
		return buffer.toString();
	}
	
	private String cutText(Paint paint, float maxLength, String content) {
		if (content == null || content.equals("")) {
			return "";
		}
		if(maxLength==0)
			return content;
		float length = paint.measureText(content);
		if (length > maxLength) {
			String[] strings = content.split(" ");
			if (strings.length > 1) {
				return cutTextWord(paint.getTextSize(), maxLength, strings, paint);
			} else {
				return content;
			}
		} else {
			return content;
		}
	}

	private String cutTextWord(float textSize, float maxLength,
			String[] content, Paint paint) {
		float length = 0;
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < content.length; i++) {
			length = paint.measureText(buffer.toString() + " " + content[i] + "...");
			if (length < maxLength) {
				buffer.append(content[i] + " ");
			} else {
				break;
			}
		}
		buffer.append("...");
		return buffer.toString();
	}
	
}
