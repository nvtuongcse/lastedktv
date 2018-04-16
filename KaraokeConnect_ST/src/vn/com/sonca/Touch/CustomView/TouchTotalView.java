package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;
import java.util.Locale;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.params.SongType;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchTotalView extends View {

	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path mainPath;
	private int widthLayout = 350;
	private int heightLayout = 350;

	private int stateFilterSong = 3;

	private long sum;
	private boolean isDisplay;
	private int totalType = TouchSearchView.SONG;
	private boolean hoverSync = false;
	private boolean hoverRight = false;
	private String strSearch = "";
	private Drawable drawable;

	public interface OnTotalViewListener {
		public void OnUpdatePic();
	}

	private OnTotalViewListener listener;

	public void setOnTotalViewListener(OnTotalViewListener listener) {
		this.listener = listener;
	}

	public TouchTotalView(Context context) {
		super(context);
		initView(context);
	}

	public TouchTotalView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchTotalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Context context;
	private void initView(Context context) {
		this.context = context;
		sum = 0;
		isDisplay = false;
	}
	
	public void setContext(Context context){
		this.context = context;
	}
	private String theloaiName = "";

	private boolean layoutTheLoai = false;

	public void setLayoutTheLoai(String idTheLoai) {
		layoutTheLoai = true;
		if(idTheLoai.equals("-1")){
			theloaiName = getResources().getString(R.string.type_songca);
		}else if(idTheLoai.equals(DbHelper.SongType_NewSongClub + "")){
			theloaiName = getResources().getString(R.string.clb_new_songs);
		}else if(idTheLoai.equals(DbHelper.SongType_NewVol + "")){
			theloaiName = getResources().getString(R.string.type_newvol_2);
		}else if(idTheLoai.equals(DbHelper.SongType_LiveSong+ "")){
			theloaiName = getResources().getString(R.string.type_livesong);
		} else if(idTheLoai.equals(DbHelper.SongType_UpdateSong+ "")){
			theloaiName = getResources().getString(R.string.type_updatesong);
		} else if(idTheLoai.equals(DbHelper.SongType_China+ "")){
			theloaiName = getResources().getString(R.string.newSongType_1);
		} else if(idTheLoai.equals(DbHelper.SongType_Online+ "")){
			theloaiName = getResources().getString(R.string.newSongType_2);
		} else{
			ArrayList<SongType> songTypeList = DBInterface.DBSearchSongType("",
					SearchMode.MODE_FULL, 0, 0, context);
			for (SongType temp : songTypeList) {
				if (temp.getID() == Integer.valueOf(idTheLoai)) {
					theloaiName = temp.getName();
//					if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English")){
//						theloaiName = changeEnglishName(theloaiName);
//					}
					break;
				}
			}	
		}
		
		requestLayout();
	}
	
	private String changeEnglishName(String defaultStr) {
		String resultStr = defaultStr;
		if(defaultStr.equalsIgnoreCase("Song Ca") || defaultStr.equalsIgnoreCase("Remix") || defaultStr.equalsIgnoreCase("Duet")){
			return resultStr;
		}
		
		String name = context.getResources().getString(R.string.change_type_1);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Modern Song";
		}
		
		name = context.getResources().getString(R.string.change_type_2);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Children Song";
		}
		
		name = context.getResources().getString(R.string.change_type_3);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Prewar Song";
		}
		
		name = context.getResources().getString(R.string.change_type_4);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Folk Song";
		}
		
		name = context.getResources().getString(R.string.change_type_5);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Love Song";
		}

		name = context.getResources().getString(R.string.change_type_6);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Sounding Folk";
		}
		
		name = context.getResources().getString(R.string.change_type_7);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Revolution Song";
		}
		
		name = context.getResources().getString(R.string.change_type_8);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Bolero";
		}
		
		name = context.getResources().getString(R.string.change_type_9);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Trinh Cong Son";
		}
		
		name = context.getResources().getString(R.string.change_type_10);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Vietnamese Lyric For Chinese Song";
		}
		
		name = context.getResources().getString(R.string.change_type_11);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Vietnamese Lyric For English Song";
		}
		
		name = context.getResources().getString(R.string.change_type_12);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Vietnamese Lyric For French Song";
		}
		
		name = context.getResources().getString(R.string.change_type_13);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Modern-Traditional Song";
		}
		return resultStr;
	}

	public void setTotalSum(long sum) {
		this.sum = sum;
		invalidate();
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
		invalidate();
	}
	
	public boolean getdisplay(){
		return isDisplay;
	}

	public void setStateFilterSong(int stateFilterSong) {
		this.stateFilterSong = stateFilterSong;
		invalidate();
	}

	public int getStateFilterSong() {
		return this.stateFilterSong;
	}

	public void setData(int sum, int totalType, String strSearch,
			int stateFilter) {
		this.sum = sum;
		this.totalType = totalType;
		this.strSearch = strSearch;
		this.stateFilterSong = stateFilter;
		invalidate();
	}	

	public long getSum(){
		return this.sum;
	}
	
	private boolean layoutYouTube = false;
	public void setLayoutYouTube(){
		this.layoutYouTube = true;
		this.layoutTheLoai = false;
		this.stateFilterSong = 1;
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myHeight = (int) (0.35*getResources().getDisplayMetrics().heightPixels/5);
		if (MyApplication.flagHong) {
			myHeight = (int) (0.3*getResources().getDisplayMetrics().heightPixels/5);
		}
		setMeasuredDimension(widthMeasureSpec,
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}

	private int color_01;
	private int color_02;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
	}

	private int countZeroSum = 0;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth(), getHeight());
		
		/*
		isDisplay = true;
		totalType = SearchView.SINGER;
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		*/
		if(sum <= 0 && countZeroSum < 1){
			countZeroSum++;
			return;
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(200, 1, 150, 234);
			color_02 = Color.argb(255, 182, 253, 255);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#C1FFE8");
			color_02 = Color.parseColor("#21BAA9");
		}
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if (isDisplay) {
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_01);
				canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);

				// ----------------------------------
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(RT1S);
				mainPaint.setColor(color_02);
				String text = "";
				if (totalType == TouchSearchView.SONG) {
					text = sum + " " + getResources().getString(R.string.total_1);
				} else if (totalType == TouchSearchView.SINGER) {
					text = sum + " " + getResources().getString(R.string.total_3);
				} else if (totalType == -1) { // MUSICIAN
					text = sum + " " + getResources().getString(R.string.total_4);
				}

				if (totalType != TouchSearchView.SONG) {
					float textWidth = mainPaint.measureText(text);
					canvas.drawText(text, widthLayout / 2 - textWidth / 2, RT1Y, mainPaint);
				} else {
					canvas.drawText(text, RT1X, RT1Y, mainPaint);
				}
				float textWidth = 0;

				if (layoutTheLoai) {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setTextSize(RT1S);
					mainPaint.setColor(color_02);
					textWidth = mainPaint.measureText(theloaiName);
					canvas.drawText(theloaiName, widthLayout - textWidth - RT1X, RT1Y, mainPaint);
				}

				if (totalType != TouchSearchView.SONG) { // them phan cap nhat hinh
													// anh

					// ---------------KHUNG HOVER-------------------
					if (hoverSync) {
						drawable = getResources().getDrawable(R.drawable.boder_lammoi_hover);
					} else {
						drawable = getResources().getDrawable(R.drawable.boder_lammoi);
					}
					drawable.setBounds((int) RT2L, (int) RT2T, (int) RT2R, (int) RT2B);
					drawable.draw(canvas);
					// ----------------------------------
					mainPaint.setStyle(Style.FILL);
					mainPaint.setTextSize(RT2S);
					mainPaint.setColor(color_02);
					text = getResources().getString(R.string.total_5);
					textWidth = mainPaint.measureText(text);
					canvas.drawText(text, (RT2L + RT2R - textWidth) / 2, RT2Y, mainPaint);
				}
			}
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

			if (isDisplay) {
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_01);
				canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);

				// ----------------------------------
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(RT1S);
				mainPaint.setColor(color_02);
				String text = "";
				if (totalType == TouchSearchView.SONG) {
					text = sum + " " + getResources().getString(R.string.total_1);
				} else if (totalType == TouchSearchView.SINGER) {
					text = sum + " " + getResources().getString(R.string.total_3);
				} else if (totalType == -1) { // MUSICIAN
					text = sum + " " + getResources().getString(R.string.total_4);
				}

				if (totalType != TouchSearchView.SONG) {
					float textWidth = mainPaint.measureText(text);
					canvas.drawText(text, widthLayout / 2 - textWidth / 2, RT1Y, mainPaint);
				} else {
					canvas.drawText(text, RT1X, RT1Y, mainPaint);
				}
				float textWidth = 0;

				if (layoutTheLoai) {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setTextSize(RT1S);
					mainPaint.setColor(color_02);
					textWidth = mainPaint.measureText(theloaiName);
					canvas.drawText(theloaiName, widthLayout - textWidth - RT1X, RT1Y, mainPaint);
				}

				if (totalType != TouchSearchView.SONG) { // them phan cap nhat hinh
													// anh

					// ---------------KHUNG HOVER-------------------
					if (hoverSync) {
						drawable = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);
					} else {
						drawable = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
					}
					drawable.setBounds((int) RT2L, (int) RT2T, (int) RT2R, (int) RT2B);
					drawable.draw(canvas);
					// ----------------------------------
					mainPaint.setStyle(Style.FILL);
					mainPaint.setTextSize(RT2S);
					mainPaint.setColor(Color.parseColor("#FFFFFF"));
					text = getResources().getString(R.string.total_5);
					textWidth = mainPaint.measureText(text);
					canvas.drawText(text, (RT2L + RT2R - textWidth) / 2, RT2Y, mainPaint);
				}
			}
		
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isDisplay) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				float x = event.getX();
				float y = event.getY();
				if (x >= RT2L && x <= RT2R && y >= RT2T && y <= RT2B
						&& totalType != TouchSearchView.SONG) {
					hoverSync = true;
				}
			}
				invalidate();
				break;
			case MotionEvent.ACTION_UP: {
				hoverSync = false;
				hoverRight = false;
				float x = event.getX();
				float y = event.getY();
				if (x >= RT2L && x <= RT2R && y >= RT2T && y <= RT2B
						&& totalType != TouchSearchView.SONG) {
					if (listener != null) {
						listener.OnUpdatePic();
					}
				}
			}
				invalidate();
				break;
			default:
				break;
			}
		}
		return true;
	}

	private float RT1S, RT1X, RT1Y;
	private float RT2S, RT2Y;
	private float RT1L, RT1R, RT1T, RT1B;
	private float RX1, RY1;
	private float RT2L, RT2R, RT2T, RT2B;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		RX1 = 1 * w / 480;
		RY1 = 1 * h / 100;

		RT1S = 55 * h / 100;
		RT1X = 12 * w / 480;
		RT1Y = 65 * h / 100;

		RT2S = 35 * h / 100;
		RT2Y = 60 * h / 100;

		RT1L = 350 * w / 480;
		RT1R = 450 * w / 480;
		RT1T = 25 * h / 100;
		RT1B = 75 * h / 100;

		RT2L = 20 * w / 480;
		RT2R = 80 * w / 480;
		RT2T = 10 * h / 100;
		RT2B = 90 * h / 100;
	}

}
