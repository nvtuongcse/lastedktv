package app.sonca.Dialog.ScoreLayout;

import java.util.List;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import app.sonca.Dialog.ScoreLayout.ScoreKaraokeView;

public class ScoreDialog extends BaseDialog {

	private String TAB = "ScoreDialog";
	
	public ScoreDialog(Context context, Window window, 
		int displayScore, int showTime, boolean isSK9000, 
		List<Integer> list) {
		super(context, window);
		this.displayScore = displayScore;
		this.showTime = showTime;
		this.isSK9000 = isSK9000;
		this.list = list;
	}
	
	private List<Integer> list;
	private boolean isSK9000 = true;
	@Override
	protected int getIdLayout() {
		if (isSK9000) {
			return R.layout.dialog_score_222;
		}else{
			return R.layout.dialog_score;
		}
	}
	
	@Override
	protected int getTimerShow() {
//		if(showTime == 0){
//			return 5000; // default
//		}
		
		return showTime;
	}

	private ScoreKaraokeView scoreView;
	@Override
	protected View getView(View contentView) {
		scoreView = (ScoreKaraokeView)contentView.findViewById(R.id.ScoreView);
		if (isSK9000) {
			if(list != null && list.size() == 5){
				ScoreItem item00 = (ScoreItem)contentView.findViewById(R.id.item00);
				ScoreItem item01 = (ScoreItem)contentView.findViewById(R.id.item01);
				ScoreItem item02 = (ScoreItem)contentView.findViewById(R.id.item02);
				ScoreItem item03 = (ScoreItem)contentView.findViewById(R.id.item03);
				ScoreItem item04 = (ScoreItem)contentView.findViewById(R.id.item04);
				item00.setValueScore(1, list.get(0));
				item01.setValueScore(2, list.get(1));
				item02.setValueScore(3, list.get(2));
				item03.setValueScore(4, list.get(3));
				item04.setValueScore(5, list.get(4));
			}
		}
		return contentView;
	}

	@Override
	protected void OnShow() {
		// MyApplication.isFocusDialog = true;
//		scoreView.activatedScore();
		scoreView.activatedScore(displayScore, list);
	}

	@Override
	protected void OnDismiss() {
		// MyApplication.isFocusDialog = false;
		scoreView = null;
	}

	@Override
	protected void OnReceiveDpad(KeyEvent event, int key) {
		// TODO Auto-generated method stub
		
	}

	int showTime = 0;
	int displayScore = 0;
	public void setDisplayScore(int displayScore, int showTime){
		this.displayScore = displayScore;
		this.showTime = showTime;
	}
}