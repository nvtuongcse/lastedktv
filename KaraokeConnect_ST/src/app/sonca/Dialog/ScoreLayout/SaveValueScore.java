package app.sonca.Dialog.ScoreLayout;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveValueScore {
	
	private SharedPreferences save;
	
	public SaveValueScore(Context context) {
		save = context.getSharedPreferences("ScoreValue",
				Context.MODE_PRIVATE);
	}
	
	public int getScoreValue(int key){
		return save.getInt("score-" + key, 0);
	}
	
	public void saveScoreValue(int key, int value){
		SharedPreferences.Editor editor = save.edit();
		editor.putInt("score-" + key, value);
		editor.commit();
	}
	
	public void clearScoreValue(){
		SharedPreferences.Editor editor = save.edit();
		editor.clear().commit();
	}

}
