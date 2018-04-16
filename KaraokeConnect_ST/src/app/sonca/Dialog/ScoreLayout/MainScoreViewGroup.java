package app.sonca.Dialog.ScoreLayout;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import app.sonca.Dialog.ScoreLayout.ScoreKaraokeView.OnShowScoreListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import android.view.View.MeasureSpec;

public class MainScoreViewGroup extends ViewGroup {
	
	public MainScoreViewGroup(Context context) {
		super(context);
		initView(context);
	}

	public MainScoreViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MainScoreViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int width = getWidth();
		int height = getHeight();
		int tamX = (int) (0.5*width);
		int tamY = (int) (0.4*height);
		int widthGroup = (int) (0.6*width);
		int heightGroup = (int) (0.5*widthGroup);
		
		int heightMyText = (int) (0.25*heightGroup);
		
		View myTextView = getChildAt(1);
		myTextView.layout(
				(int)(tamX - 0.5*widthGroup), 
				(int)(tamY - 0.5*heightGroup), 
				(int)(tamX + 0.5*widthGroup),
				(int)(tamY - 0.5*heightGroup + heightMyText));
		View backgroup = getChildAt(0);
		backgroup.layout(
				(int)(tamX - 0.5*widthGroup), 
				(int)(tamY - 0.55*heightGroup), 
				(int)(tamX + 0.5*widthGroup),
				(int)(tamY + 0.55*heightGroup));
		int tamScoreY = (int) (0.4*height);
		int heightScore = (int) (0.45*heightGroup);
		ScoreKaraokeView score = (ScoreKaraokeView)getChildAt(2);
		score.layout(
				(int)(tamX - 0.95*heightScore), 
				(int)(tamScoreY - 0.5*heightScore), 
				(int)(tamX + 0.95*heightScore),
				(int)(tamScoreY + 0.5*heightScore));
		
		int heightStar = (int) (0.2*heightGroup);
		
		final StateScoreView state = (StateScoreView)getChildAt(3);
		state.layout(
				(int)(tamX - 0.5*widthGroup), 
				(int)(tamScoreY + 0.5*heightScore), 
				(int)(tamX + 0.5*widthGroup),
				(int)(tamY + 0.5*heightGroup));
		
		score.setOnShowScoreListener(new OnShowScoreListener() {
			@Override public void OnShow(int score) {
				//MyLog.e("MainScoreViewGroup", "OnShow : " + score);
				if (score < 60) {
					state.setTextScore(getResources().getString(R.string.dialog_score_0));
				} else if (score < 80) {
					state.setTextScore(getResources().getString(R.string.dialog_score_1));
				} else if (score < 90) {
					state.setTextScore(getResources().getString(R.string.dialog_score_2));
				} else {
					state.setTextScore(getResources().getString(R.string.dialog_score_3));
				}
			}
		});
		
	}
	
	

}