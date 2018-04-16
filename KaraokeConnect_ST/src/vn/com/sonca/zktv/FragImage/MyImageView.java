package vn.com.sonca.zktv.FragImage;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.utils.PinyinHelper;
import vn.com.sonca.zktv.FragSwitch.ViewSwitch;
import vn.com.sonca.zzzzz.MyApplication;

public class MyImageView extends View {
	
	private String TAB = "MyImageView";
	private String sdcard = Environment.getExternalStorageDirectory() + "/Android/data/";
//	private String sdcard = "";
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Context context;
	
    private Drawable drawable = null;
    private String textName = "";
    
    private OnImageViewListener listener;
	public interface OnImageViewListener {
		public void OnClick(int position, Object object);
	}
	
	public void setOnImageViewListener(OnImageViewListener listener){
		this.listener = listener;
	}
	
	public MyImageView(Context context) {
		super(context);
		initView(context);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		anime.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(listener != null && object != null){
					listener.OnClick(position, object);
				}
			}
		});
		textPaint.setStyle(Style.FILL);
		this.context = context;
	}
	
	private int position;
	private String pathImage;
	private String search = "";
	private int color;
	public void setData(int position, int color, String name , String search, int cover){
		pathImage = sdcard + context.getPackageName() + "/PICTURE/" + cover;
		if (search == null) {
			this.search = "";
		} else {
			this.search = search;
		}
		this.position = position;
		this.color = color;
		textName = name;
		clearData();
		invalidate();
		
	}
	
	private float transX;
	private float transY;
	private int widthLayout, heighLayout;
	private Rect rectBG = new Rect();
	private Rect rectImage = new Rect();
	private Rect rectBottom = new Rect();
	private int cirX, cirY, radius;
	private int poX, poY, poS;
	private int nameS, nameX, nameY;
	private int padX, padd;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);

		widthLayout = w;
    	heighLayout = h;
    	
    	cirX = (int) (0.055*w);
		cirY = (int) (0.055*h);
		radius = (int) (0.055*h);
		
		poS = (int) (0.07*h);
		poX = (int) (0.054*w);
		poY = (int) (0.053*h + 0.4*poS);

    	padX = (int) (0.147*w);
    	
		padd = 10 * h / 326;
    	
    	transX = padX + (float) (0.03*w) - 5 * h / 326;
		transY = (float) (0.79*h);
		rectImage.set(padX, padd,  w - (int) (0.09*w), (int)(0.76*h));	
		rectBottom.set(padX, (int)(0.76*h), w - (int) (0.09*w), h - padd);
		
		rectBG.set(rectImage.left - padd, rectImage.top - padd, rectBottom.right + padd, rectBottom.bottom + padd);		
		
		nameS = (int) (0.11*h);
		nameX = padX + (int) (0.03*w) - 5 * h / 326;
		nameY = (int) (0.79*h);
		
    }
    
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int mColor = getResources().getColor(R.color.ktv_color_image_1);
		switch (position) {
		case 1:
			mColor = getResources().getColor(R.color.ktv_color_image_1);
			break;
		case 2:
			mColor = getResources().getColor(R.color.ktv_color_image_2);
			break;
		case 3:
			mColor = getResources().getColor(R.color.ktv_color_image_3);
			break;
		case 4:
			mColor = getResources().getColor(R.color.ktv_color_image_4);
			break;
		case 5:
			mColor = getResources().getColor(R.color.ktv_color_image_5);
			break;
		case 6:
			mColor = getResources().getColor(R.color.ktv_color_image_6);
			break;
		case 7:
			mColor = getResources().getColor(R.color.ktv_color_image_7);
			break;
		case 8:
			mColor = getResources().getColor(R.color.ktv_color_image_8);
			break;
		default:
			break;
		}
		
		if(position == 2 || position == 3 || position == 4 || position == 6 || position == 8){
			poY = (int) (0.050*heighLayout + 0.4*poS);	
		}		

 		if (drawable == null) {
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(Color.WHITE);
			canvas.drawRect(rectBG, paintMain);		
			
			if (loadImage == null) {
				loadImage = new LoadImage(pathImage, textName, search);
				loadImage.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
			}	
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(color);
			canvas.drawRect(rectBottom, paintMain);
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(mColor);
			paintMain.setStrokeWidth(4);
			canvas.drawCircle(cirX, cirY, radius, paintMain);
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(poS);
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
			canvas.drawText(""+ position, poX, poY, textPaint);
			
//			textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//			textPaint.setTextSize(nameS);
//			String stringname = "";
//			int widthSong = (int) (0.75 * widthLayout);
//			int sizeSong = (int) textPaint.measureText(textName + "...");
//			if (sizeSong > widthSong) {
//				float f = textPaint.measureText("...");
//				int i = textPaint.breakText(textName, true, widthSong - f, null);
//				stringname = textName.substring(0, i) + "...";
//			} else {
//				stringname = textName;
//			}
//			textPaint.setTextAlign(Align.LEFT);
//			canvas.drawText(stringname, nameX, nameY, textPaint);
			
		} else {
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(Color.WHITE);
			canvas.drawRect(rectBG, paintMain);
			
			if(!textName.equals("")){
				drawable.setBounds(rectImage);
				drawable.draw(canvas);
			}

			paintMain.setStyle(Style.FILL);
			paintMain.setColor(color);
			canvas.drawRect(rectBottom, paintMain);
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(mColor);
			paintMain.setStrokeWidth(4);
			canvas.drawCircle(cirX, cirY, radius, paintMain);
			textPaint.setColor(Color.WHITE);
			textPaint.setTextSize(poS);
			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
			canvas.drawText(""+ position, poX, poY, textPaint);
			textPaint.setTextAlign(Align.LEFT);
			
			textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
			textPaint.setTextSize(nameS);
			builder.clear();
			int widthSong = (int) (0.75 * widthLayout);
			int sizeSong = (int) textPaint.measureText(wordtoSpan.toString() + "...");
			if (sizeSong > widthSong) {
				float f = textPaint.measureText("...");
				int i = textPaint.breakText(wordtoSpan.toString(), true, widthSong - f, null);
				builder.append(wordtoSpan.subSequence(0, i));
				builder.append("...");
			} else {
				builder.append(wordtoSpan);
			}
			StaticLayout layout = new StaticLayout(builder, 
					textPaint, widthLayout, Alignment.ALIGN_NORMAL, 1, 0, true);
			canvas.translate(transX, transY);
			layout.draw(canvas);
			
			canvas.translate(0, 0);
			
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    int myHeight = (int) (parentWidth);
	    super.onMeasure(widthMeasureSpec, 
	    		MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.EXACTLY));
	}
	
    private void resetPaint() {
    	paintMain.reset();
    	paintMain.setAntiAlias(true);
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
	private Spannable wordtoSpan = new SpannableString("");
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
			if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE) {
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(textName.equals("")){
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(object == null){
				return true;
			}
			this.startAnimation(anime);
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		default:
			break;
		}
		return true;
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	private Object object;


}
