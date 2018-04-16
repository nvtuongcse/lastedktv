package app.sonca.Dialog.ScoreLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import app.sonca.Dialog.ScoreLayout.ScoreKaraokeView.OnScoreKaraokeListener;
import app.sonca.Dialog.ScoreLayout.ScoreKaraokeView.OnShowScoreListener;

public class ScoreViewGroup extends ViewGroup {
	
	public ScoreViewGroup(Context context) {
		super(context);
		initView(context);
	}

	public ScoreViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ScoreViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private SaveValueScore valueScore;
	private ArrayList<Integer> listInteger;
	private void initView(Context context) {
		valueScore = new SaveValueScore(context);
		listInteger = new ArrayList<Integer>();
		listInteger.add(valueScore.getScoreValue(0));
		listInteger.add(valueScore.getScoreValue(1));
		listInteger.add(valueScore.getScoreValue(2));
		listInteger.add(valueScore.getScoreValue(3));
		listInteger.add(valueScore.getScoreValue(4));
		Collections.sort(listInteger, Collections.reverseOrder());
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.5*getResources().getDisplayMetrics().heightPixels);
		int width = (int) (1.7*height);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int width = getWidth();
		int height = getHeight();
		
		int padding = 10;
		int percentW = (int) (0.6*width);
		
		View backgroud = getChildAt(0);
		backgroud.layout(0, 0, width, height);
		
		View scoreFrame = getChildAt(1);
		scoreFrame.layout(padding, padding, percentW, height - padding);
		((ScoreFrameView)scoreFrame).setNameFrames(
				getResources().getDrawable(R.drawable.yourscore));
		
		View scoreRank = getChildAt(2);
		scoreRank.layout(percentW, padding, width - padding, height - padding);
		((ScoreFrameView)scoreRank).setNameFrames(
				getResources().getDrawable(R.drawable.rank));
		
		int paddingScore = (int) (0.15*width);
		View score = getChildAt(3);
		score.layout(
			padding + 0, 
			padding + paddingScore, 
			percentW - 0, 
			height - 0 - paddingScore);
		
		int offestY = (int) (0.32*height);
		int offestX = (int) (0.5*(percentW + (width - padding)));
		int heightItem = (int) (0.12*height);
		int widthItem = (int) (0.5*4*heightItem);
		for (int i = 0; i < 5; i++) {
			ScoreItem item = (ScoreItem)getChildAt(4 + i);
			// item.setValueScore(i, listInteger.get(i));
			item.layout(offestX - widthItem, offestY, 
					offestX + widthItem, offestY + heightItem);
			offestY += heightItem;
		}
		
		offestY = (int) (0.18*height);
		offestX = (int) (0.5*(percentW + (width - padding)));
		heightItem = (int) (0.1*height);
		widthItem = (int) (0.5*5*heightItem);
		View itemRating = getChildAt(9);
		itemRating.layout(offestX - widthItem, offestY, 
				offestX + widthItem, offestY + heightItem);
		
/*
		((ScoreKaraokeView)score).setOnScoreKaraokeListener(new OnScoreKaraokeListener() {

			@Override
			public void OnScoreFinished(int intScore, List<Integer> arrayList) {
				// TODO Auto-generated method stub
				if(arrayList != null){
					arrayList.add(intScore);
					Collections.sort(arrayList, Collections.reverseOrder());
					arrayList.remove(arrayList.size() - 1);
					for (int i = 0; i < arrayList.size(); i++) {
						int value = arrayList.get(i).intValue();
						valueScore.saveScoreValue(i, value);
						ScoreItem item = (ScoreItem)getChildAt(4 + i);
						item.setValueScore(i, value);
						item.updateView();
					}
				}
			}
			
		});
		((ScoreKaraokeView)score).setOnShowScoreListener(new OnShowScoreListener() {
			@Override public void OnShow(int intScore) {
				Log.e("ScoreViewGroup", "intScore : " + intScore);
				if(listInteger != null){
					Collections.sort(listInteger, Collections.reverseOrder());
					listInteger.remove(listInteger.size() - 1);
					for (int i = 0; i < listInteger.size(); i++) {
						int value = listInteger.get(i).intValue();
						ScoreItem item = (ScoreItem)getChildAt(4 + i);
						item.setValueScore(i, value);
						item.updateView();
					}
				}
			}
		});
*/
		
	}
	
	
	

}