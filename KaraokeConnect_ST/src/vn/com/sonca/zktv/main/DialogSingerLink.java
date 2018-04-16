package vn.com.sonca.zktv.main;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import app.sonca.Dialog.ScoreLayout.BaseDialog;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.CustomView.TouchSingerLinkView;
import vn.com.sonca.Touch.CustomView.TouchSingerLinkView.OnStopShowListener;
import vn.com.sonca.params.Song;

public class DialogSingerLink extends BaseDialog {

	private Song song;
	public DialogSingerLink(Context context, Window window, Song song) {
		super(context, window);
		this.song = song;
	}
	
	private OnDialogSingerLinkListener listener;
	public interface OnDialogSingerLinkListener {
		public void OnSingerLink(int idSinger);
	}
	
	public void setOnDialogSingerLinkListener(OnDialogSingerLinkListener listener){
		this.listener = listener;
	}

	@Override
	protected int getIdLayout() {
		// TODO Auto-generated method stub
		return R.layout.ktv_dialog_singerlink;
	}

	@Override
	protected int getTimerShow() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	protected View getView(View contentView) {
		TouchSingerLinkView linkView = (TouchSingerLinkView)
				contentView.findViewById(R.id.singerLinkView);
		linkView.ShowSingerLink(song.getSinger().getName(), song.getSingerId());
		linkView.setOnStopShowListener(new OnStopShowListener() {
			@Override
			public void OnStop(int idSinger) {
				if(listener != null){
					listener.OnSingerLink(idSinger);
				}
			}
		});
		return contentView;
	}

	@Override
	protected void OnShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnDismiss() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnReceiveDpad(KeyEvent event, int key) {
		// TODO Auto-generated method stub
		
	}

}
