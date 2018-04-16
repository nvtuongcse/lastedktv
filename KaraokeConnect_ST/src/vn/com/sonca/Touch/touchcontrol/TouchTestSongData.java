package vn.com.sonca.Touch.touchcontrol;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongID;
import vn.com.sonca.utils.Lock;
import vn.com.sonca.zzzzz.MyApplication;

public class TouchTestSongData extends Thread {
	
	private Lock lock = new Lock();
	private String TAB = "TestSongData";
	private ArrayList<Song> listSongs;
	private ArrayList<SongID> arraySong;
	private TouchMainActivity mainActivity;
	private Context context;
	private boolean isRun;
	
	public TouchTestSongData(ArrayList<SongID> arraySong , Context context , TouchMainActivity mainActivity) {
		this.mainActivity = mainActivity;
		this.arraySong = arraySong;
		this.context = context;
		isRun = false;
	}
	
	private UpdateDataListener listener;
	public interface UpdateDataListener {
		public void Update(ArrayList<Song> list);
	}
	
	public void setUpdateDataListener(UpdateDataListener listener){
		this.listener = listener;
	}
	
	public boolean isRunnig(){
		return isRun;
	}
	
	public void cancel(boolean isRun){
		this.isRun = isRun;
	}
	
	public void execute(){
		run();
	}
	
	private boolean isCancel(){
		return this.isRun;
	}
	
	@Override
	public void run() {
		try {
			lock.lock();
			listSongs = CheckUpdatePlayList(arraySong);
			handlerCheck.sendEmptyMessage(0);
			lock.unlock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.run();
	}
	
	private ArrayList<Song> CheckUpdatePlayList(ArrayList<SongID> input){
		if(input.size() <= 0){
			return new ArrayList<Song>();
		}
		ArrayList<Song> playlist = ((MyApplication)mainActivity.getApplication()).getListActive();
		ArrayList<Song> list = new ArrayList<Song>();
		for (int i = 0; i < input.size(); i++) {
			SongID songID = input.get(i);
			list.add(new Song(songID.songID , songID.typeABC));
		}
		if(playlist.size() == list.size()){
			for (int i = 0; i < playlist.size(); i++) {
				Song song1 = playlist.get(i);
				Song song2 = list.get(i);
				if(!song1.equals(song2)){
					return list;
				}
			}
			return null;
		}
		return list;
	}
	
	private Handler handlerCheck = new Handler(){
		public void handleMessage(Message msg) {
			if(listener != null && listSongs != null){
				listener.Update(listSongs);
			}
		};
	};

}
