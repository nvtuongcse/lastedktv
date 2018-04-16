package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.SetttingApp.ButonOnOff;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class TouchBaseTypeView extends View {
	
	private String TAB = "BaseTypeView";
    private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context context;
    
    private int widthLayout = 100;
    private int heightLayout = 100;
    
    public static final int ACTIVE = 0;
    public static final int INACTIVE = 1;
    public static final int HOVER = 2;
    private int StateSave = INACTIVE;
    private int StateCurrent = INACTIVE;
    
    private Drawable background;
    private Drawable icon;
	
	public TouchBaseTypeView(Context context) {
		super(context);
		initView(context);
	}

	public TouchBaseTypeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchBaseTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable zlight_boder_active, zlight_boder;
	
	private void initView(Context context) {
		this.context = context;
		StateCurrent = StateSave = INACTIVE;
		
		zlight_boder = getResources().getDrawable(
				R.drawable.zlight_touch_image_boder_tab_header_inactive_196x170);
		zlight_boder_active = getResources().getDrawable(
				R.drawable.zlight_touch_image_boder_tab_header_active_196x170);		
	}
	
	protected abstract int getActiveIcon();
	protected abstract int getInActiveIcon();
	protected abstract int getHoverIcon();
	protected abstract String getTextView();
	protected abstract String getTypeView();
	
	private OnBaseTypeViewListener listener;
	public interface OnBaseTypeViewListener {
		public void onBaseTypeView(boolean isActive, String type , TouchBaseTypeView view);
	}
	
	public void setOnBaseTypeViewListener(OnBaseTypeViewListener listener){
		this.listener = listener;
	}
	
	private int colorTextActive;
	private int colorText;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {

		} else {
			colorText = Color.argb(255, 61, 195, 177);
			colorTextActive = Color.WHITE;
		}
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			setK(getWidth(), getHeight());
			paintMain.setStyle(Style.FILL);
			paintMain.setTextSize(K7);
			if (StateSave == ACTIVE) {
				paintMain.setColor(Color.parseColor("#ffe002"));
				background = getResources().getDrawable(
						R.drawable.touch_image_boder_tab_header_active_196x170);
				background.setBounds(0 - K4, K8, widthLayout + K4, heightLayout);
				background.draw(canvas);
			} else {
				if(getTypeView().equals(TouchMainActivity.YOUTUBE)){
					int valueYouTube = MyApplication.getCommandMediumYouTube();
					if(valueYouTube != 2){
						paintMain.setARGB(255, 0, 253, 253);
						background = getResources().getDrawable(
								R.drawable.touch_image_boder_tab_header_196x170);
						background.setBounds(0 - K4, K8, widthLayout + K4, heightLayout);
						background.draw(canvas);
					} else {
						paintMain.setColor(Color.GRAY);
						background = getResources().getDrawable(
								R.drawable.touch_image_boder_tab_header_196x170_xam);
						background.setBounds(0 - K4, K8, widthLayout + K4, heightLayout);
						background.draw(canvas);
					}
				} else {
					paintMain.setARGB(255, 0, 253, 253);
					background = getResources().getDrawable(
							R.drawable.touch_image_boder_tab_header_196x170);
					background.setBounds(0 - K4, K8, widthLayout + K4, heightLayout);
					background.draw(canvas);
				}		
			}
			paintMain.setTypeface(Typeface.DEFAULT_BOLD);
			float textWidth = paintMain.measureText(String
					.valueOf(getTextView()));
			canvas.drawText(String.valueOf(getTextView()), (widthLayout / 2)
					- (textWidth / 2), (heightLayout - K6), paintMain);

			switch (StateCurrent) {
			case ACTIVE:
				icon = getResources().getDrawable(getActiveIcon());
				break;
			case INACTIVE:
				icon = getResources().getDrawable(getInActiveIcon());
				break;
			case HOVER:
				icon = getResources().getDrawable(getHoverIcon());
				break;
			default:
				break;
			}
			if(MyApplication.flagOnAdminYouTube == false && getTypeView().equals(TouchMainActivity.YOUTUBE)){
				icon = getResources().getDrawable(
						R.drawable.tab_youtube_inactive_144x144_xam);
			}
			icon.setBounds(K1 - K4, K9, widthLayout - K1 + K4, heightLayout - K2);
			icon.draw(canvas);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
				
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = (int) (0.094 * getResources().getDisplayMetrics().widthPixels);
		if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {
			myWidth = (int) (0.0825 * getResources().getDisplayMetrics().widthPixels);
		}
		setMeasuredDimension(
				MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.UNSPECIFIED),
				heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
		widthLayout = w;
		heightLayout = h - K1;
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	int valueYouTube = MyApplication.getCommandMediumYouTube();
		if(getTypeView().equals(TouchMainActivity.YOUTUBE) && valueYouTube == ButonOnOff.KHOAHET){
			return true;
		}
    	
    	switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN :
			StateCurrent = HOVER;
			invalidate();
			break;
		case MotionEvent.ACTION_UP :
			if (StateSave == INACTIVE) {
				StateSave = ACTIVE;
				if (listener != null) {
					listener.onBaseTypeView(true, getTypeView(), this);
				}
			} else {
				StateSave = INACTIVE;
				if (listener != null) {
					listener.onBaseTypeView(false, getTypeView(), this);
				}
			}
			StateCurrent = StateSave;
			invalidate();
			break;
		default : break;
		}
    	
    	return true;
    }
    
    public void setInActive(){
		StateCurrent = StateSave = INACTIVE;
		invalidate();
    }
    
    public void setActive(){
    	StateCurrent = StateSave = ACTIVE;
		invalidate();
    }
    
    public int isActive(){
    	return StateCurrent;
    }
    
    private float KL;
    private int K1,K2,K3,K4,K5,K6,K7,K8 , K9;
    private void setK(int width , int height){
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			K1 = 14*height/184;
	    	K2 = 22*height/144;
	    	    	
	    	K3 = 9*height/144;
	    	K4 = 10*width/193;
	    	K5 = 100*width/1920;
	    	K6 = 23*height/144;
	    	K7 = 27*width/193;
	    	K8 = 14*height/184;
	    	
	    	K9 = 14*height/144;
			KL = (float) (172*height/184);		
		} else {
	    	K1 = 14*height/184;
	    	K2 = 22*height/144;
	    	    	
	    	K3 = 9*height/144;
	    	K4 = 0*height/144;
	    	K5 = 100*width/1920;
	    	K6 = 23*height/144;
	    	K7 = 27*width/193;
	    	K8 = 10*height/184;
	    	
	    	K9 = 10*height/144;
			KL = (float) (172*height/184);			
		}
		
    }

}
