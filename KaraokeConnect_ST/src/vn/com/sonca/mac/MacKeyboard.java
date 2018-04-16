package vn.com.sonca.mac;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.sonca.MyLog.MyLog;
import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MacKeyboard extends View {

	public static final char CLEAR = '@';

	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private StringBuffer stringOut = new StringBuffer();

	private Drawable drawaActive;
	private Drawable drawaInActive;
	private Drawable drawClearAc;
	private Drawable drawClearIn;
	private Drawable drawSpaceAc;
	private Drawable drawSpaceIn;

	private ArrayList<Circle> listCircles;
	private Context context;
	private int widthLayout = 400;
	private int heightLayout = 400;
	private float DP;

	public MacKeyboard(Context context) {
		super(context);
		initView(context);
	}

	public MacKeyboard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MacKeyboard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		mainText.setStyle(Style.FILL);
		drawaActive = getResources().getDrawable(
				R.drawable.image_boder_active_118x102);
		drawaInActive = getResources().getDrawable(
				R.drawable.image_boder_inactive_118x102);
		drawClearAc = getResources().getDrawable(
				R.drawable.image_xoa_active_244x102);
		drawClearIn = getResources().getDrawable(
				R.drawable.image_xoa_inactive_244x102);
		drawSpaceAc = getResources().getDrawable(
				R.drawable.image_space_active_244x102);
		drawSpaceIn = getResources().getDrawable(
				R.drawable.image_space_inactive_244x102);
		DP = getResources().getDisplayMetrics().density;
		listCircles = new ArrayList<Circle>();
		this.context = context;

	}

	private OnMACKeyboardViewListener listener;

	public interface OnMACKeyboardViewListener {
		public void onKeyboardClick(char key);
	}

	public void setOnMACKeyboardViewListener(OnMACKeyboardViewListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = 12 * heightMeasureSpec / 25;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}

	private int KS;
	private int K, KT;
	private int draW, drawH;
	private Rect rectClear;
	private Rect rectSpace;

	private int minR;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		setClickable(false);

		minR = (int) (0.15 * w);

		drawH = (int) (h / 21.5);
		draW = 118 * drawH / 102;
		KT = (int) (48 * h / 1080);
		KS = (int) (1 * DP);
		widthLayout = w;
		heightLayout = h;

		mainText.setARGB(204, 180, 254, 255);
		mainText.setTextSize(KT);

		// TODO ---1234-------------------------------------

		listCircles.clear();

		int tamX = (int) (0.15 * widthLayout);
		int tamY = (int) (0.085 * heightLayout);
		int left = (int) (tamX - draW), right = (int) (tamX + draW);
		int top = (int) (tamY - drawH), bottom = (int) (tamY + drawH);
		float width = mainText.measureText("1");
		int textX = (int) (tamX - width / 2);
		int textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('1', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.085 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("2");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('2', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.085 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("3");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('3', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.085 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("4");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('4', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---5678-------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.175 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("5");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('5', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.175 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("6");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('6', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.175 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("7");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('7', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.175 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("8");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('8', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---90----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.265 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("9");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('9', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.265 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("0");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('0', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---ABCD----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.375 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("A");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('A', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.375 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("B");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('B', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.375 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("C");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('C', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.375 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("D");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('D', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---EFGH----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.465 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("E");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('E', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.465 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("F");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('F', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.465 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("G");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('G', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.465 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("H");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('H', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---IJKL----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.555 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("I");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('I', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.555 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("J");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('J', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.555 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("K");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('K', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.555 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("L");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('L', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---MNOP----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.645 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("M");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('M', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.645 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("N");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('N', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.645 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("O");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('O', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.645 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("P");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('P', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---QSRT----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.735 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("Q");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('Q', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.735 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("R");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('R', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.735 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("S");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('S', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.735 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("T");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('T', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---VUWX----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.825 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("U");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('U', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.825 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("V");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('V', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.825 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("W");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('W', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.825 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("X");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('X', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---YZ----------------------------------------

		tamX = (int) (0.15 * widthLayout);
		tamY = (int) (0.915 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("Y");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('Y', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.385 * widthLayout);
		tamY = (int) (0.915 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("Z");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('Z', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// TODO ---_Clear----------------------------------------

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.265 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText(" ");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle(' ', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.265 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText(" ");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle(' ', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.615 * widthLayout);
		tamY = (int) (0.915 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("@");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('@', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.85 * widthLayout);
		tamY = (int) (0.915 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = mainText.measureText("@");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('@', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		int WD = 244 * drawH / 102;
		tamX = (int) (0.733 * widthLayout);
		tamY = (int) (0.915 * heightLayout);
		left = (int) (tamX - WD);
		right = (int) (tamX + WD);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		rectClear = new Rect(left, top, right, bottom);

		tamX = (int) (0.733 * widthLayout);
		tamY = (int) (0.265 * heightLayout);
		left = (int) (tamX - WD);
		right = (int) (tamX + WD);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		rectSpace = new Rect(left, top, right, bottom);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int minItem = -1;

		for (int i = 0; i < listCircles.size() - 4; i++) {
			Circle circle = listCircles.get(i);
			if (circle.isActive()) {
				drawaActive.setBounds(circle.getRectF());
				drawaActive.draw(canvas);
				minItem = i;
			} else {
				drawaInActive.setBounds(circle.getRectF());
				drawaInActive.draw(canvas);
			}
		}

		int size = listCircles.size();
		if (listCircles.get(size - 1).isActive()
				|| listCircles.get(size - 2).isActive()) {
			drawClearAc.setBounds(rectClear);
			drawClearAc.draw(canvas);
		} else {
			drawClearIn.setBounds(rectClear);
			drawClearIn.draw(canvas);
		}

		if (listCircles.get(size - 3).isActive()
				|| listCircles.get(size - 4).isActive()) {
			drawSpaceAc.setBounds(rectSpace);
			drawSpaceAc.draw(canvas);
			minItem = size - 3;
		} else {
			drawSpaceIn.setBounds(rectSpace);
			drawSpaceIn.draw(canvas);
		}

		for (int i = 0; i < listCircles.size() - 4; i++) {
			Circle circle = listCircles.get(i);
			if (circle.isActive()) {
				mainText.setShadowLayer(10, 0, 0, Color.WHITE);
			} else {
				mainText.setShadowLayer(0, 0, 0, Color.WHITE);
			}
			canvas.drawText(String.valueOf(circle.getName()),
					circle.getTextX(), circle.getTextY(), mainText);
		}

	}

	private boolean isClicked;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			isClicked = true;
			float offsetX = event.getX();
			float offsetY = event.getY();
			int minItem = -1;
			float min = minR;
			for (int i = 0; i < listCircles.size(); i++) {
				Circle circle = listCircles.get(i);
				circle.setActive(false);
				float centerX = circle.getCenterX();
				float centerY = circle.getCenterY();
				float R = (float) Math.sqrt((offsetX - centerX)
						* (offsetX - centerX) + (offsetY - centerY)
						* (offsetY - centerY));
				if (min > R) {
					min = R;
					minItem = i;
				}
			}
			if (minItem != -1) {
				listCircles.get(minItem).setActive(true);
				if (listener != null) {
					if (minItem == (listCircles.size() - 1)
							|| minItem == (listCircles.size() - 2)) {
						sendClear();
					} else {
						if (listener != null) {
							listener.onKeyboardClick(listCircles.get(minItem)
									.getName());
						}
					}
				}
			}
			invalidate();
		}
			break;
		case MotionEvent.ACTION_UP: {
			stopClear();
			isClicked = false;
			for (int i = 0; i < listCircles.size(); i++) {
				listCircles.get(i).setActive(false);
			}
			if (delay500 != null) {
				delay500.cancel(true);
				delay500 = null;
			}
			invalidate();
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			if (delay500 != null) {
				delay500.cancel(true);
				delay500 = null;
			}
			delay500 = new Delay500();
			delay500.execute();
		}
			break;
		}
		return true;
	}

	private Paint createHorizontalLines(PointF pointFirst, PointF pointLast) {
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(KS);
		LinearGradient gradient = new LinearGradient(pointFirst.x,
				pointFirst.y / 2, pointLast.x, pointLast.y / 2,
				Color.TRANSPARENT, Color.argb(255, 2, 235, 252),
				Shader.TileMode.MIRROR);
		paint.setShader(gradient);
		return paint;
	}

	// ////////////////////////////////////////////////////////////

	class Circle {
		private boolean isActive = false;
		private char name;
		private Rect rectF;
		private int centerX;
		private int centerY;
		private int textX;
		private int textY;

		public Circle(char name, int centerX, int centerY, int textX,
				int textY, Rect rectF) {
			this.centerX = centerX;
			this.centerY = centerY;
			this.textX = textX;
			this.textY = textY;
			this.rectF = rectF;
			this.name = name;
		}

		public char getName() {
			return name;
		}

		public int getTextX() {
			return textX;
		}

		public int getTextY() {
			return textY;
		}

		public int getCenterX() {
			return centerX;
		}

		public int getCenterY() {
			return centerY;
		}

		public Rect getRectF() {
			return rectF;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}

	// ////////////////////////////////////////////////////////////

	private Timer timerClear;

	private void sendClear() {
		timerClear = new Timer();
		timerClear.schedule(new TimerTask() {
			@Override
			public void run() {
				handlerClear.sendEmptyMessage(0);
			}
		}, 5, 300);
	}

	Handler handlerClear = new Handler() {
		public void handleMessage(android.os.Message msg) {
			listener.onKeyboardClick(CLEAR);
		};
	};

	private void stopClear() {
		if (timerClear != null) {
			timerClear.cancel();
			timerClear = null;
		}
	}

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
