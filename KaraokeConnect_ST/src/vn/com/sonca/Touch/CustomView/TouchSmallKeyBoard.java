package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchSmallKeyBoard extends View {
	
	public static final String NULL = "null";
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ArrayList<Circle> listCircles;
    private int widthLayout = 400;
    private int heightLayout = 400;
    private StringBuffer stringOut = new StringBuffer();
	
	public TouchSmallKeyBoard(Context context) {
		super(context);
		initView(context);
	}

	public TouchSmallKeyBoard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSmallKeyBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		listCircles = new ArrayList<Circle>();
	}

	private OnSmallKeyboardViewListener listener;
	public interface OnSmallKeyboardViewListener {
		public void onSmallKeyboardView(String key);
	}
	
	public void setOnKeyboardViewListener(OnSmallKeyboardViewListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth() , getHeight());
		
		RadialGradient gradient;

		for (int i = 0; i < listCircles.size(); i++) {
			Circle circle = listCircles.get(i);
			float tamX = widthLayout/2 + K*circle.getCenterX();
			float tamY = heightLayout/2 + K*circle.getCenterY();
			if (circle.isActive()) {
				mainPaint.setStyle(Style.FILL);
				mainPaint.setStrokeWidth(5);
				mainPaint.setColor(Color.CYAN);
				gradient = new RadialGradient(tamX, tamY, 2*K, Color.CYAN, Color.TRANSPARENT, Shader.TileMode.CLAMP);
				mainPaint.setShader(gradient);
				canvas.drawCircle(tamX, tamY, K, mainPaint);
				mainPaint.setShader(null);
				
				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth(KS);
				mainPaint.setColor(Color.RED);
				canvas.drawCircle(tamX, tamY, K, mainPaint);
			} 
		}
		
		mainPaint.setStyle(Style.FILL);
		for (int i = 0; i < listCircles.size() ; i++) {
			Circle circle = listCircles.get(i);
			float tamX = widthLayout/2 + K*circle.getCenterX();
			float tamY = heightLayout/2 + K*circle.getCenterY();
			mainPaint.setColor(Color.RED);
			mainPaint.setTextSize(KT);
			float textWidth = mainPaint.measureText(String.valueOf(circle.getCharacter()));
			canvas.drawText(String.valueOf(circle.getCharacter()) , tamX - (textWidth/2), (float) (tamY + K/2.1), mainPaint);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec , heightMeasureSpec);
		int myWidth = 12*heightMeasureSpec/22;
	    setMeasuredDimension(myWidth, heightMeasureSpec);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
		widthLayout = w;
		heightLayout = h;
	
		listCircles.add(new Circle('7', -2 , 1));
		listCircles.add(new Circle('4', -2 , 3));
		listCircles.add(new Circle('1', -2 , 5));
		listCircles.add(new Circle('0', -2 , 7));

		listCircles.add(new Circle('8', 0 , 1));
		listCircles.add(new Circle('5', 0 , 3));
		listCircles.add(new Circle('2', 0 , 5));
		listCircles.add(new Circle('.', 0 , 7));
		
		listCircles.add(new Circle('9', 2 , 1));
		listCircles.add(new Circle('6', 2 , 3));
		listCircles.add(new Circle('3', 2 , 5));
		listCircles.add(new Circle('X', 2 , 7));

	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			float x = event.getX();
			float y = event.getY();
			int minItem = -1;
			float minR = 50;
			for (int i = 0; i < listCircles.size(); i++) {
				Circle circle = listCircles.get(i);
				circle.setActive(false);
				float centerX = widthLayout/2 + K*circle.getCenterX();
				float centerY = heightLayout/2 + K*circle.getCenterY();
				float R = (float) Math.sqrt((x - centerX)*(x - centerX) + (y - centerY)*(y - centerY));
				if (minR > R) {
					minR = R;
					minItem = i;
				} 
			}
			if(minItem != -1){
				listCircles.get(minItem).setActive(true);
				if (listener != null) {
					if(minItem == (listCircles.size() - 2)){
						if (stringOut.length() > 0) {
							stringOut.deleteCharAt(stringOut.length() - 1);
						} 
					}else if(minItem == (listCircles.size() - 1)){
						stringOut.delete(0, stringOut.length());
					}else {
						stringOut.append(listCircles.get(minItem).getCharacter());
					}
						//-----------------//
					if (stringOut.length() > 0) {
//						Log.i("view", "length > 0");
						listener.onSmallKeyboardView(stringOut.toString());
					} else {
//						Log.i("view", "length = 0");
						listener.onSmallKeyboardView(NULL);
					}
				} 
			}
			invalidate();
		}break;
		case MotionEvent.ACTION_UP:
			for (int i = 0; i < listCircles.size(); i++) {
				listCircles.get(i).setActive(false);
			}invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			if(delay500 != null){
				delay500.cancel(true);
				delay500 = null;
			}
			delay500 = new Delay500();
			delay500.execute();
			break;
		default : break;
		}
    	return true;
    }
 
	
    private int KS;
    private float K , KT;
	private void setK(int w , int h){
		K = h/22;
		KT = (float) (62*h/1080);
		KS = (int) (6*h/1080);
	}
	
//////////////////////////////////////////////////////////////
	
	class Circle {
		private float centerX;
		private float centerY;
		private char character;
		private boolean isActive;

		public Circle(char c, float x, float y) {
			centerX = x;
			centerY = y;
			character = c;
			isActive = false;
		}

		public float getCenterX() {
			return centerX;
		}

		public float getCenterY() {
			return centerY;
		}

		public char getCharacter() {
			return character;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}
	
//////////////////////////////////////////////////////////////

	private Delay500 delay500 = null;

	class Delay500 extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			long m = System.currentTimeMillis();
			while ((System.currentTimeMillis() - m) < 500) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			for (int i = 0; i < listCircles.size(); i++) {
				listCircles.get(i).setActive(false);
			}
			invalidate();
		}

	}
	
}
