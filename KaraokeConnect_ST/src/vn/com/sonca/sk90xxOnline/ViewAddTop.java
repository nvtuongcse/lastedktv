package vn.com.sonca.sk90xxOnline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;

public class ViewAddTop extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewAddTop(Context context) {
		super(context);
		initView(context);
	}

	public ViewAddTop(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewAddTop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String titleTaiBai = "", titleAnBai = "";
	private Drawable drawLine;
	private Drawable drawable, drawDevice;
	
	private String QUANLY = "";
	private String TAIBAI = "";
	private String ANBAI = "";
	
	private Drawable drawableTemp;
	
	private void initView(Context context) {
		titleTaiBai = getResources().getString(R.string.dialog_90xx_online_1a);
		titleAnBai = getResources().getString(R.string.dialog_90xx_online_1b);
		drawable = getResources().getDrawable(R.drawable.touch_tab_exit_active_144x72);
		drawLine = getResources().getDrawable(R.drawable.shape_line_search_view);
		
		QUANLY = getResources().getString(R.string.dialog_90xx_ql_1);
		TAIBAI = getResources().getString(R.string.dialog_90xx_ql_2);
		ANBAI = getResources().getString(R.string.dialog_90xx_ql_3);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int textS, textX, textY;
	private Rect rectLine = new Rect();
	private Rect rectBackgroud = new Rect();
	private Rect rectDrawable = new Rect();
	private Rect rectDevice = new Rect();
	private float nameServerS, nameServerY;
	
	private Rect rectModeAnBai = new Rect();
	private Rect rectModeTaiBai = new Rect();
	private float but1aX, but1aY, but1aS;
	private float but1bX, but1bY, but1bS;
	private float but2aX, but2aY, but2aS;
	private float but2bX, but2bY, but2bS;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectBackgroud.set(0, 0, w, (int)(0.95*h));
		rectLine.set(0, (int)(0.95*h), w, h);
		
		textS = (int) (0.4*h);
		textX = (int) (0.36*w);
		textY = (int) (0.4*h + 0.6*textS);
		
		int tamX = (int) (50);
		int tamY = (int) (0.5*h);
		int height = (int) (0.5*h);
		int width = 72*height/144;
		rectDrawable.set(10 , tamY - height, 10 + 2*width, tamY + height);
		
		tamX = (int) (0.1*w);
		tamY = (int) (0.4*h);
		height = (int) (0.3*h);
		width = 71*height/65;
		rectDevice.set(tamX - width , tamY - height, tamX + width, tamY + height);
		
		nameServerS = (int) (0.2*h);
		nameServerY = (float) (0.9*h);
		
		//--------------------
		tamY = (int) (0.5*h);
		height = (int) (0.4*h);
		width = 174*height/74;
		rectModeAnBai.set(w - 2*width - 10 , tamY - height, w - 10, tamY + height);
		
		int wr = w - 2*width - 15;
		rectModeTaiBai.set(wr - 2*width - 10 , tamY - height, wr - 10, tamY + height);
	
		but1aS = but1bS = but2aS = but2bS = (float) (0.25*h);
		textPaint.setTextSize(but1aS);
		but1aY = but2aY = (float) (0.6*h - 0.6*but1aS);
		but1bY = but2bY = (float) (0.6*h + 0.6*but1aS);
		but1aX = (rectModeAnBai.left + rectModeAnBai.right)/2 - textPaint.measureText(QUANLY)/2;
		but2aX = (rectModeTaiBai.left + rectModeTaiBai.right)/2 - textPaint.measureText(QUANLY)/2;
		but1bX = (rectModeAnBai.left + rectModeAnBai.right)/2 - textPaint.measureText(ANBAI)/2;
		but2bX = (rectModeTaiBai.left + rectModeTaiBai.right)/2 - textPaint.measureText(TAIBAI)/2;
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.parseColor("#003c6e"));
		canvas.drawRect(rectBackgroud, paintMain);
		drawLine.setBounds(rectLine);
		drawLine.draw(canvas);
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textS);
		textPaint.setColor(Color.parseColor("#B4FEFF"));
		if(currentMode == 0){
			canvas.drawText(titleTaiBai, textX, textY, textPaint);
		} else {
			canvas.drawText(titleAnBai, textX, textY, textPaint);
		}		
		
		drawable.setBounds(rectDrawable);
		drawable.draw(canvas);
		
		drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65); 
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW){
			drawDevice = getResources().getDrawable(R.drawable.icon_ktvwwifi_connect);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL){
			drawDevice = getResources().getDrawable(R.drawable.touch_icon_model_active);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM1){
			drawDevice = getResources().getDrawable(R.drawable.icon_model_km1_connect);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM2){
			drawDevice = getResources().getDrawable(R.drawable.icon_model_km2_connect);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM){
			drawDevice = getResources().getDrawable(R.drawable.icon_kb_oem_active);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI){
			drawDevice = getResources().getDrawable(R.drawable.icon_km1wifi_active);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
			drawDevice = getResources().getDrawable(R.drawable.icon_kb39c_active);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
			drawDevice = getResources().getDrawable(R.drawable.daumay_kb_active);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			drawDevice = getResources().getDrawable(R.drawable.daumay_9108);
			if(MyApplication.flagSmartK_CB){
				drawDevice = getResources().getDrawable(R.drawable.cloudbox_active);
			}
			if(MyApplication.flagSmartK_801){
				drawDevice = getResources().getDrawable(R.drawable.sb801_active);
			}
			if(MyApplication.flagSmartK_KM4){
				drawDevice = getResources().getDrawable(R.drawable.km4_active);
			}
			drawDevice.setBounds(rectDevice);
			drawDevice.draw(canvas);
			
			drawDevice = getResources().getDrawable(R.drawable.wifi_9108_4);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			drawDevice = getResources().getDrawable(R.drawable.daumay_tbt);
			drawDevice.setBounds(rectDevice);
			drawDevice.draw(canvas);
			
			drawDevice = getResources().getDrawable(R.drawable.ktvwifi_4);
		}
		
		drawDevice.setBounds(rectDevice);
		drawDevice.draw(canvas);
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(nameServerS);
		textPaint.setColor(Color.GREEN);
		float nameX = (rectDevice.left + rectDevice.right)/2 - textPaint.measureText(serverName)/2;
		canvas.drawText(serverName, nameX, nameServerY, textPaint);
		
		drawableTemp = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_active);
		if(stateclick == intTaiBai){
			drawableTemp = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_hover);
		}
		if(currentMode != 1){
			drawableTemp = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_xam);
		}
		drawableTemp.setBounds(rectModeTaiBai);
		drawableTemp.draw(canvas);
		
		drawableTemp = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_active);
		if(stateclick == intAnBai){
			drawableTemp = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_hover);
		}
		if(currentMode != 0){
			drawableTemp = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_xam);
		}
		drawableTemp.setBounds(rectModeAnBai);
		drawableTemp.draw(canvas);		
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(but1aS);
		textPaint.setColor(Color.argb(255, 7, 40, 10));
		canvas.drawText(QUANLY, but1aX, but1aY, textPaint);
		canvas.drawText(ANBAI, but1bX, but1bY, textPaint);
		canvas.drawText(QUANLY, but2aX, but2aY, textPaint);
		canvas.drawText(TAIBAI, but2bX, but2bY, textPaint);
		
	}
	
	private OnViewAddTopListener listener;
	public interface OnViewAddTopListener {
		public void OnTopBack();
		public void OnTopTaiBai();
		public void OnTopAnBai();
	}
	public void setOnViewAddTopListener(OnViewAddTopListener listener){
		this.listener = listener;
	}
		
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(listener != null){
				float x= event.getX();
				float y = event.getY();

				if (x >= rectModeAnBai.left && x <= rectModeAnBai.right
						&& y >= rectModeAnBai.top && y <= rectModeAnBai.bottom) {
					stateclick = intAnBai;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			stateclick = 0;
			invalidate();
			if(listener != null){
				float x= event.getX();
				float y = event.getY();
				if(x >= rectDrawable.left && x <= rectDrawable.right && 
					y >= rectDrawable.top && y <= rectDrawable.bottom){
					listener.OnTopBack();
				}

				if (x >= rectModeAnBai.left && x <= rectModeAnBai.right
						&& y >= rectModeAnBai.top && y <= rectModeAnBai.bottom) {
					listener.OnTopAnBai();
					return true;
				}
			}
			break;

		default: break;
		}
		return true;
	}
	
	private String serverName = "";
	
	public void setServerName(String serverName){
		this.serverName = serverName;
		invalidate();
	}
	
	private int currentMode = 0;
	public void setCurrentMode(int currentMode){
		this.currentMode = currentMode;
		invalidate();
	}
	
	private int stateclick = 0;
	private final int intTaiBai = 1;
	private final int intAnBai = 2;
	
}
