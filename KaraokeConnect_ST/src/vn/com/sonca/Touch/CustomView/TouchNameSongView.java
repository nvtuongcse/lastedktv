package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;

public class TouchNameSongView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private String textSong = "Tam biet mua ha";
	private String textSearch = "TB";
	private StaticLayout layoutAC;
	private StaticLayout layoutIN;
	
	public TouchNameSongView(Context context) {
		super(context);
		initView(context);
	}

	public TouchNameSongView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchNameSongView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Context context;
	private void initView(Context context) {
		this.context = context;
	}
	
	private boolean isActive;
	private Spannable wordtoSpan = new SpannableString("");
	public void setSongName(String name , String search , boolean isActive){
		ArrayList<Integer> listOffset = new ArrayList<Integer>();
		this.textSong = cutText(30, name);
		this.textSearch = search;
		this.isActive = isActive;
		String[] strings = textSong.split("[ -]");
		int count = 0;
		for (int i = 0; i < textSearch.length(); i++) {
			listOffset.add(count);
			count += strings[i].length() + 1;
		}
		wordtoSpan = new SpannableString(textSong);
		for (int i = 0; i < listOffset.size(); i++) {
			int offset = listOffset.get(i);
			wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), offset, offset + 1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myHeight = (int) (0.6188 * getResources().getDisplayMetrics().heightPixels/8);
		int myWidth = (int) (1300*widthMeasureSpec/1920);
		setMeasuredDimension(myWidth,
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}
	
	private float textSize;
	private float transX;
	private float transY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		textSize = (float) (0.581*h);
		transX = (float) (0.0755*w);
		transY = (float) (0.25*h);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// setSongName(textSong, textSearch , true);
		textPaint.setTextSize(textSize);
		if (isActive) {
			textPaint.setARGB(255, 0, 254, 255);
		} else {
			textPaint.setARGB(255 , 182 , 253 , 255);
		}
		StaticLayout layout = new StaticLayout(wordtoSpan, textPaint, getWidth(),
				Alignment.ALIGN_NORMAL, 1, 0, true);
		canvas.translate(transX, transY);
		layout.draw(canvas);
	}

///////////////////////// - CUT TEXT - ////////////////////////////

	private String cutText(int maxLength, String content) {
		if (content.length() <= maxLength) {
			return content;
		}
		return content.substring(0, maxLength) + "...";
	}
	/*
	private char ConvertUnicode2(char input){
		char c = Character.toLowerCase(input);	
		switch (c) {
		case 'a': return 'a';
		case 'ă': return 'a';
		case 'â': return 'a'; 
		
		case 'á': return 'a';
		case 'à': return 'a';
		case 'ả': return 'a';
		case 'ã': return 'a';
		case 'ạ': return 'a';
						
		case 'ắ': return 'a';
		case 'ằ': return 'a';
		case 'ẳ': return 'a';
		case 'ẵ': return 'a';
		case 'ặ': return 'a';
						
		case 'ấ': return 'a';
		case 'ầ': return 'a';
		case 'ẩ': return 'a';
		case 'ẫ': return 'a';
		case 'ậ': return 'a';
		
		//-----------//
		
		case 'd': return 'd';
		case 'đ': return 'd';
		
		//-----------//
		
		case 'u': return 'u';
		case 'ú': return 'u';
		case 'ù': return 'u';
		case 'ủ': return 'u';
		case 'ũ': return 'u';
		case 'ụ': return 'u';
		
		//-----------//
		
		case 'e': return 'e';
		case 'ê': return 'e';
						
		case 'é': return 'e';
		case 'è': return 'e';
		case 'ẻ': return 'e';
		case 'ẽ': return 'e';
		case 'ẹ': return 'e';
				
		case 'ế': return 'e';
		case 'ề': return 'e';
		case 'ể': return 'e';
		case 'ễ': return 'e';
		case 'ệ': return 'e';
		
		//-----------//
		
		case 'i': return 'i';
		case 'í': return 'i';
		case 'ì': return 'i';
		case 'ỉ': return 'i';
		case 'ĩ': return 'i';
		case 'ị': return 'i';
		
		//-----------//
		
		case 'o': return 'o';
		case 'ô': return 'o';
		case 'ơ': return 'o';
				
		case 'ó': return 'o';
		case 'ò': return 'o';
		case 'ỏ': return 'o';
		case 'õ': return 'o';
		case 'ọ': return 'o';
						
		case 'ố': return 'o';
		case 'ồ': return 'o';
		case 'ổ': return 'o';
		case 'ỗ': return 'o';
		case 'ộ': return 'o';
						
		case 'ớ': return 'o';
		case 'ờ': return 'o';
		case 'ở': return 'o';
		case 'ỡ': return 'o';
		case 'ợ': return 'o';

		default:	return c;
		}
		
	}
	*/
}
