package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchSingerLinkView extends View {
	
	private final String TAB = "TouchSingerLinkView";
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintRectBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintButton = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchSingerLinkView(Context context) {
		super(context);
		initView(context);
	}

	public TouchSingerLinkView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSingerLinkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable zlightBackgroup;
	private void initView(Context context) {
		zlightBackgroup = getResources().getDrawable(R.drawable.zlight_boder_popup);
		
		this.namesinger = ",".split(",");
		listItems = new ArrayList<Item>();
		
		intItemActive = -1;
		
		idsinger = new int[]{};
		namesinger = new String[]{};
		/*
		idsinger = new int[]{1,2,3};
		namesinger = new String[]{"a","b","c"};
		*/
	}
	
	private OnStopShowListener listener;
	public interface OnStopShowListener {
		public void OnStop(int idSinger);
	}
	
	public void setOnStopShowListener(OnStopShowListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private float TX0 , TY0;
	private float wr , hr;
	private float textS;
	private float space;
	private float left, right, top, bottom;
	private Path pathLine = new Path();
	private Path pathBut = new Path();
	private ArrayList<Item> listItems;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
	}
	
	private void setK(int w , int h){
		left = w/2 - w/5;
		right = w/2 + w/5;
		wr = 2*(right - left)/3;
		hr = (float) (0.3*wr);
		
		float hrect = (float) (hr*namesinger.length);
		space = (float) (0.02*h);
		top = h/2 - hrect/2;
		bottom = (h/2 + hrect/2);
		
		textS = (float) (0.45*hr);
		paintText.setTextSize(textS);
		
		float wt = (float) (0.65*wr);
		float ht = (float) (0.8*hr);
		listItems.clear();
		for (int i = 0; i < idsinger.length; i++) {
			Item item = new Item();
			item.setId(idsinger[i]);
			item.setName(namesinger[i]);
			float width = paintText.measureText(namesinger[i]);
			item.setOffsetX(w / 2 - width / 2);
			item.setOffsetY((float)(top + (i+1)*hr - 0.65*textS));
			item.setRect(new Rect((int)(w/2 - wt), (int)(top + (i+1)*hr - hr/2 - ht/2), 
					(int)(w/2 + wt), (int)(top + (i+1)*hr - hr/2 + ht/2)));
			Path path = new Path();
			path.moveTo(w/2 - wt, top + (i+1)*hr);
			path.lineTo(w/2 + wt, top + (i+1)*hr);
			item.setPath(path);
			listItems.add(item);
		}

		float line = (float) (0.1*wr);
		pathLine = new Path();
		pathLine.moveTo(left, top - space + line);
		pathLine.lineTo(left, top - space);
		pathLine.lineTo(left + line, top - space);
		pathLine.moveTo(right - line, top - space);
		pathLine.lineTo(right, top - space);
		pathLine.lineTo(right, top - space + line);
		pathLine.moveTo(right, bottom + space - line);
		pathLine.lineTo(right, bottom + space);
		pathLine.lineTo(right - line, bottom + space);
		pathLine.moveTo(left + line, bottom + space);
		pathLine.lineTo(left, bottom + space);
		pathLine.lineTo(left, bottom + space - line);
	}
	
	private int[] idsinger;
	private String[] namesinger;
	public void ShowSingerLink(String namesinger , int[] idsinger){
		intItemActive = -1;
		// MyLog.e(TAB, namesinger);
		// MyLog.e(TAB, " : " + idsinger.length);
		this.namesinger = namesinger.split(",");
		this.idsinger = idsinger;
		setK(getWidth(), getHeight());
		CreateTimerBack();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// MyLog.e(TAB, "onDraw");
		// MyApplication.intColorScreen = MyApplication.SCREEN_KTVUI;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE ||
			MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			paintText.setStyle(Style.FILL);
			paintText.setARGB(255, 182, 253, 255);
			paintRect.setStyle(Style.FILL);
			paintRect.setARGB(240, 27, 74, 116);
			paintLine.setStyle(Style.STROKE);
			paintLine.setARGB(255, 0, 253, 253);
			paintButton.setStyle(Style.FILL);
			paintButton.setARGB(0, 1, 55, 102);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			paintText.setStyle(Style.FILL);
			paintText.setARGB(255, 255, 255, 255);
			paintRect.setStyle(Style.FILL);
			paintRect.setARGB(240, 148, 196, 133);
			paintRectBorder.setStyle(Style.STROKE);
			paintRectBorder.setARGB(255, 255, 255, 255);
			paintLine.setStyle(Style.STROKE);
			paintLine.setColor(Color.parseColor("#21BAA9"));
			paintButton.setStyle(Style.FILL);
			paintButton.setColor(Color.parseColor("#21BAA9"));
		}

		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE ||
			MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if (namesinger != null) {
				if (namesinger.length > 0) {
					if (isShow) {
						int width = getWidth();
						int height = getHeight();
						paintMain.setStyle(Style.FILL);
						paintMain.setARGB(intAphaBlack, 0, 0, 0);
						canvas.drawRect(0, 0, width, height, paintMain);
					}
					canvas.drawRect(left, top - space, right, bottom + space,
							paintRect);
					if (MyApplication.intColorScreen == MyApplication.SCREEN_GREEN) {
						canvas.drawRect(left, top - space, right, bottom
								+ space, paintRectBorder);
					}
					paintLine.setStrokeWidth(2);
					canvas.drawPath(pathBut, paintLine);
					paintLine.setStrokeWidth((float) (0.02 * wr));
					canvas.drawPath(pathLine, paintLine);

					paintLine.setStrokeWidth(2);
					for (int i = 0; i < listItems.size(); i++) {
						Item item = listItems.get(i);
						if (item.isActive()) {
							paintButton.setAlpha(150);
						} else {
							paintButton.setAlpha(0);
						}
						canvas.drawRect(item.getRect(), paintButton);
						if (i != listItems.size() - 1) {
							canvas.drawPath(item.getPath(), paintLine);
						}
						canvas.drawText(item.getName(), item.getOffsetX(),
								item.getOffsetY(), paintText);
					}
				}
			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			if (namesinger != null) {
				if (namesinger.length > 0) {
					if (isShow) {
						int width = getWidth();
						int height = getHeight();
						paintMain.setStyle(Style.FILL);
						paintMain.setARGB(intAphaBlack, 0, 0, 0);
						canvas.drawRect(0, 0, width, height, paintMain);
					}

					zlightBackgroup.setBounds((int) left, (int) (top - space),
							(int) right, (int) (bottom + space));
					zlightBackgroup.draw(canvas);

					paintLine.setStrokeWidth(2);
					for (int i = 0; i < listItems.size(); i++) {
						Item item = listItems.get(i);
						if (item.isActive()) {
							paintButton.setAlpha(255);
							paintText.setColor(Color.parseColor("#FFFFFF"));
						} else {
							paintButton.setAlpha(0);
							paintText.setColor(Color.parseColor("#21BAA9"));
						}
						canvas.drawRect(item.getRect(), paintButton);
						if (i != listItems.size() - 1) {
							canvas.drawPath(item.getPath(), paintLine);
						}
						canvas.drawText(item.getName(), item.getOffsetX(),
								item.getOffsetY(), paintText);
					}
				}
			}

		}
	}
	
	private int intItemActive;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE: {
			intItemActive = -1;
			float x = event.getX();
			float y = event.getY();
			for (int i = 0; i < listItems.size(); i++) {
				Item item = listItems.get(i);
				Rect rect = item.getRect();
				if (x >= rect.left && x <= rect.right && y >= rect.top
						&& y <= rect.bottom) {
					item.setActive(true);
					intItemActive = i;
				} else {
					item.setActive(false);
				}
			}
		}
			invalidate();
			break;
		case MotionEvent.ACTION_DOWN: {
			intItemActive = -1;
			float x = event.getX();
			float y = event.getY();
			for (int i = 0; i < listItems.size(); i++) {
				Item item = listItems.get(i);
				Rect rect = item.getRect();
				if (x >= rect.left && x <= rect.right && y >= rect.top
						&& y <= rect.bottom) {
					item.setActive(true);
					intItemActive = i;
				} else {
					item.setActive(false);
				}
			}
		}
			invalidate();
			break;
		case MotionEvent.ACTION_UP: {
			intItemActive = -1;
			float x = event.getX();
			float y = event.getY();
			for (int i = 0; i < listItems.size(); i++) {
				Item item = listItems.get(i);
				Rect rect = item.getRect();
				if (x >= rect.left && x <= rect.right && y >= rect.top
						&& y <= rect.bottom) {
					item.setActive(true);
					intItemActive = i;
				} else {
					item.setActive(false);
				}
			}
			HideSingerLink();
		}
			invalidate();
			break;
		}

		return true;
	}
	
	private boolean isShow = false;
	private int intAphaBlack = 0;
	private Timer timerBack = null;
	private void CreateTimerBack(){
		StopTimerBack();
		isShow = true;
		intAphaBlack = 0;
		timerBack = new Timer();
		timerBack.schedule(new TimerTask() {
			@Override public void run() {
				intAphaBlack += 10;
				if(intAphaBlack > 150){
					intAphaBlack = 150;
					handlerTimerBack.sendEmptyMessage(1);
				}else{
					handlerTimerBack.sendEmptyMessage(0);
				}
			}
		}, 10, 10);
	}
	private void HideSingerLink(){
		StopTimerBack();
		isShow = true;
		timerBack = new Timer();
		timerBack.schedule(new TimerTask() {
			@Override public void run() {
				intAphaBlack -= 10;
				if(intAphaBlack < 0){
					intAphaBlack = 0;
					handlerTimerBack.sendEmptyMessage(2);
					cancel();
				}else{
					handlerTimerBack.sendEmptyMessage(0);
				}
			}
		}, 10, 10);
	}
	private void StopTimerBack(){
		if(timerBack != null){
			timerBack.cancel();
			timerBack = null;
		}
	}
	private Handler handlerTimerBack = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				invalidate();
			}
			if(msg.what == 1){
				StopTimerBack();
			}
			if(msg.what == 2){
				if(listener != null){
					StopTimerBack();
					if(intItemActive == -1){
						listener.OnStop(-1);
					}else{
						int id = listItems.get(intItemActive).getId();
						listener.OnStop(id);
					}
					namesinger = null;
					invalidate();
				}
			}
		};
	};
	
	class Item {
		private String name;
		private int id;
		private float offsetX;
		private float offsetY;
		private Rect rect;
		private Path path;
		private boolean isActive = false;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public float getOffsetX() {
			return offsetX;
		}
		public void setOffsetX(float offsetX) {
			this.offsetX = offsetX;
		}
		public float getOffsetY() {
			return offsetY;
		}
		public void setOffsetY(float offsetY) {
			this.offsetY = offsetY;
		}
		public Rect getRect() {
			return rect;
		}
		public void setRect(Rect rect) {
			this.rect = rect;
		}
		public Path getPath() {
			return path;
		}
		public void setPath(Path path) {
			this.path = path;
		}
		public boolean isActive() {
			return isActive;
		}
		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}

}
