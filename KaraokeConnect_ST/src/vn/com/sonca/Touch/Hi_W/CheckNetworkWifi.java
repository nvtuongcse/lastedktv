package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class CheckNetworkWifi extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public CheckNetworkWifi(Context context) {
		super(context);
		initView(context);
	}

	public CheckNetworkWifi(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public CheckNetworkWifi(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String line1 = "Cho phep mo rong mang Wi-Fi";
	private String line2 = "(ket noi dau karaoke voi mang Wi-Fi";
	private String line3 = "co san)";
	private Drawable drawCheckNO;
	private Drawable drawCheckOK;
	
	private Drawable zlightdrawCheckNO;
	private Drawable zlightdrawCheckOK;
	
	private void initView(Context context) {
		drawCheckNO = getResources().getDrawable(R.drawable.touch_icon_check_off);
		drawCheckOK = getResources().getDrawable(R.drawable.touch_icon_check_fw_on);
		
		zlightdrawCheckNO = getResources().getDrawable(R.drawable.zlight_icon_check_off);
		zlightdrawCheckOK = getResources().getDrawable(R.drawable.zlight_icon_check_fw_on);		
		
		line1 = getResources().getString(R.string.hiw_text_right_1);
		line2 = getResources().getString(R.string.hiw_text_right_2);
		line3 = getResources().getString(R.string.hiw_text_right_3);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (0.22*width);
		setMeasuredDimension(width, height);
	}
	
	private int line1X, line1Y, line1S;
	private int line2X, line2Y, line2S;
	private int line3X, line3Y, line3S;
	private Rect rectCheck = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tx = (int) (0.07*w);
		int ty = (int) (0.15*h);
		int hw = (int) (0.08*w); 
		rectCheck.set(tx, ty, tx + hw, ty + hw);
		
		line1S = (int) (0.27*h);
		line1X = (int) (tx + hw + 0.01*w);
		line1Y = (int) (0.2*h + 0.5*line1S);
		
		line2S = (int) (0.2*h);
		line2X = (int) (tx + hw + 0.01*w);
		line2Y = (int) (0.5*h + 0.5*line1S);
		
		line3S = line2S;
		line3X = line2X;
		line3Y = (int) (0.75*h + 0.5*line1S);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (isCheck) {
				drawCheckOK.setBounds(rectCheck);
				drawCheckOK.draw(canvas);
			} else {
				drawCheckNO.setBounds(rectCheck);
				drawCheckNO.draw(canvas);
			}
			mainText.setStyle(Style.FILL);
			mainText.setARGB(255, 180, 253, 253);
			mainText.setTextSize(line1S);
			canvas.drawText(line1, line1X, line1Y, mainText);
			
			mainText.setTextSize(line2S);
			canvas.drawText(line2, line2X, line2Y, mainText);
			
			mainText.setTextSize(line3S);
			canvas.drawText(line3, line3X, line3Y, mainText);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			if (isCheck) {
				zlightdrawCheckOK.setBounds(rectCheck);
				zlightdrawCheckOK.draw(canvas);
			} else {
				zlightdrawCheckNO.setBounds(rectCheck);
				zlightdrawCheckNO.draw(canvas);
			}
			mainText.setStyle(Style.FILL);
			mainText.setColor(Color.parseColor("#005249"));
			mainText.setTextSize(line1S);
			canvas.drawText(line1, line1X, line1Y, mainText);
			
			mainText.setTextSize(line2S);
			canvas.drawText(line2, line2X, line2Y, mainText);
			
			mainText.setTextSize(line3S);
			canvas.drawText(line3, line3X, line3Y, mainText);
		
		}
		
	}
	
	private OnCheckChangeListener listenerCheck;
	public interface OnCheckChangeListener{
		public void OnCheckChange(View view, boolean isCheck);
	}
	
	public void setOnCheckChangeListener(OnCheckChangeListener listenerCheck){
		this.listenerCheck = listenerCheck;
	}
	
	private boolean isCheck = true;
	public boolean isCheckView() {
		return isCheck;
	}
	public void setCheckView(boolean isCheck){
		this.isCheck = isCheck;
		invalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_UP:
			float x = event.getX();
			float y = event.getY();
			if(rectCheck != null){
				if(x >= rectCheck.left && x <= rectCheck.right &&
					y >= rectCheck.top && y <= rectCheck.bottom){
					isCheck = !isCheck;
					invalidate();
					if(listenerCheck != null){
						listenerCheck.OnCheckChange(this, isCheck);
					}
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	

}
