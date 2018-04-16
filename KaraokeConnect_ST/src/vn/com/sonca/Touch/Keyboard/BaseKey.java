package vn.com.sonca.Touch.Keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;

public class BaseKey extends View implements OnClickListener{
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public BaseKey(Context context) {
		super(context);
		initView(context);
	}

	public BaseKey(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public BaseKey(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	public void initView(Context context){
		setOnClickListener(this);
		// drawAC = getResources().getDrawable(R.drawable.image_boder_active_118x102);
		// drawIN = getResources().getDrawable(R.drawable.image_boder_inactive_118x102);
	}
	
	private OnClickBaseKeyListener listener;
	public interface OnClickBaseKeyListener {
		public void OnNameKey(String namekey, View view);
	}
	
	public void setOnClickBaseKeyListener(OnClickBaseKeyListener listener){
		this.listener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int magin;
	private int widthLayout, heightLayout;
	private RectF restKey = new RectF();
	private Rect rectImage = new Rect();
	private Rect zlightRectImage = new Rect();
	private int textS, textY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		magin = 10;
		restKey.set(magin, magin, widthLayout - magin, heightLayout - magin);
		rectImage.set(0, 0, w, h);
		
		int d = (int) (0.05*w);
		zlightRectImage.set(d, d, w - d, h - d);
		
		textS = (int) (0.45*h);
		textY = (int) (0.5*h + 0.35*textS);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (pressed) {
				if(drawAC != null){
					drawAC.setBounds(rectImage);
					drawAC.draw(canvas);
				}
			} else {
				if(drawIN != null){
					drawIN.setBounds(rectImage);
					drawIN.draw(canvas);
				}
			}
			if(namekey.equals(GroupKeyBoard.CLEAR) || namekey.equals(GroupKeyBoard.SPACE)){
				return;
			}
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textS);
			if (pressed) {
				textPaint.setARGB(204, 180, 254, 255);
				textPaint.setShadowLayer(10, 0, 0, Color.WHITE);
			} else {
				textPaint.setARGB(255, 255, 255, 255);
				textPaint.setShadowLayer(0, 0, 0, Color.WHITE);
			}
			
			
			if(namekey.equals("123") && layout == View.GONE){
				textPaint.setColor(Color.GREEN);
				textPaint.setShadowLayer(0, 0, 0, Color.WHITE);
			}
			float textX = (float) (0.5 * widthLayout - 0.5 * textPaint.measureText(namekey));
			canvas.drawText(namekey, textX, textY, textPaint);

			
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			if (pressed) {
				if(drawAC != null){
					drawAC.setBounds(zlightRectImage);
					drawAC.draw(canvas);
				}
			} else {
				if(drawIN != null){
					drawIN.setBounds(zlightRectImage);
					drawIN.draw(canvas);
				}
			}
			if(namekey.equals(GroupKeyBoard.CLEAR) || namekey.equals(GroupKeyBoard.SPACE)){
				return;
			}
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textS);
			if (pressed) {
				textPaint.setColor(Color.parseColor("#FFFFFF"));
			} else {
				textPaint.setColor(Color.parseColor("#21BAA9"));
			}
			if(namekey.equals("123") && layout == View.GONE){
				textPaint.setColor(Color.GREEN);
			}
			float textX = (float) (0.5 * widthLayout - 0.5 * textPaint.measureText(namekey));
			canvas.drawText(namekey, textX, textY, textPaint);
		}

		
	}
	
	private boolean pressed = false;
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		this.pressed = pressed;
		invalidate();
	}
	
	private String namekey = "";
	private Drawable drawAC = null;
	private Drawable drawIN = null;
	public void setImage(Drawable drawAC, Drawable drawIN, String namekey){
		this.drawAC = drawAC;
		this.drawIN = drawIN;
		this.namekey = namekey;
		invalidate();
	}

	@Override
	public void onClick(View v) {
		if(listener != null){
			listener.OnNameKey(namekey, v);
		}
	}
	
	private int layout;
	public void setLayoutView(int layout){
		this.layout = layout;
		invalidate();
	}
	
}
