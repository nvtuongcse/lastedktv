package vn.com.sonca.ColorLyric;

import vn.com.sonca.MyLog.MyLog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

public class MyPlayer implements OnCompletionListener, OnErrorListener, OnBufferingUpdateListener, OnPreparedListener, OnInfoListener {
	private final String TAG = "MyPlayer";

	public interface OnPlayerListener {

		public void onPlayerStop(MyPlayer mediaPlayer);

		public void onPlayerError(String path, int what, int extra);

		public void onPlayerReadyToPlay(MyPlayer mediaPlayer);
	}

	public static final int STATE_PREPARING = -1;

	private final int TIMEOUT_PREPARE = 3000;
	private final int TIMEOUT_PREPARE_VITAMO = 9000;
	private final int PERCENT = 33;

	private FragmentActivity mActivity;
	private OnPlayerListener mListener;

	private android.media.MediaPlayer mPlayer;
	private MediaPlayer mVitamoPlayer;
	private String mFilePath;
	private boolean mIsUsingVitamo = false;

	private Handler mHandlerTimeOut = new Handler(new Handler.Callback() {

		@Override public boolean handleMessage(Message arg0) {
			if (isPlaying() == false) {
				try {
					stop(false);
				} catch (Exception ex) {
					MyLog.e(TAG, ex);
				}

				try {
					mListener.onPlayerError(mFilePath, STATE_PREPARING, 0);
				} catch (Exception ex) {
					MyLog.e(TAG, ex);
				}
			}
			return true;
		}
	});

	public boolean isUsingVitamo() {
		return mIsUsingVitamo;
	}

	public MyPlayer(Context context) {
		try {
			MyLog.d(TAG, "====MyPlayer init======");
			mActivity = (FragmentActivity) context;
			mListener = (OnPlayerListener) context;
			mIsUsingVitamo = false;
			if (this.mListener == null)
				throw new ClassCastException();
		} catch (ClassCastException e) {
			throw new ClassCastException(context.getClass().getName().toString() + " must implement OnKaraokeEventListener");
		}
	}

	public void setAudio(boolean isOn) {
		try {
			if (mPlayer != null) {
				if (isOn) {
					mPlayer.setVolume(0.0f, 1.0f);
				} else {
					mPlayer.setVolume(1.0f, 0.0f);
				}
			}

			if (mVitamoPlayer != null) {
				if (isOn) {
					mVitamoPlayer.setVolume(0.0f, 1.0f);
				} else {
					mVitamoPlayer.setVolume(1.0f, 0.0f);
				}
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	private void clearTimeOut() {
		try {
			if (mHandlerTimeOut != null && mHandlerTimeOut.hasMessages(0) == true) {
				mHandlerTimeOut.removeMessages(0);
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	public boolean initPlayer(String path) {
		this.mFilePath = path;
		boolean result = false;
		try {
			MyLog.i(TAG, "Url: " + path);
			stop(false);

			clearTimeOut();
			mHandlerTimeOut.sendEmptyMessageDelayed(0, TIMEOUT_PREPARE);

			mPlayer = new MediaPlayer();
			mPlayer.setOnPreparedListener(this);
			mPlayer.setOnCompletionListener(this);
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnBufferingUpdateListener(this);
			mPlayer.setOnInfoListener(this);
			mPlayer.setDataSource(path);
			mPlayer.prepareAsync();
			MyLog.i(TAG, "===initPlayer==" + mPlayer);
			
			result = true;
		} catch (Exception e) {
			result = false;
		}

		if (result == false) {
			try {
				stop(true);
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
			}
		}

		return result;
	}

	public boolean initVitamoPlayer(String path) {
		mIsUsingVitamo = true;
		mFilePath = path;

		boolean result = false;
		try {
			MyLog.i(TAG, "Url: " + path);
			stop(false);

			clearTimeOut();
			mHandlerTimeOut.sendEmptyMessageDelayed(0, TIMEOUT_PREPARE_VITAMO);

			mVitamoPlayer = new MediaPlayer();
			mVitamoPlayer.setOnPreparedListener(this);
			mVitamoPlayer.setOnCompletionListener(this);
			mVitamoPlayer.setOnErrorListener(this);
			mVitamoPlayer.setOnBufferingUpdateListener(this);
			mVitamoPlayer.setOnInfoListener(this);
			mVitamoPlayer.setDataSource(path);
			mVitamoPlayer.prepareAsync();

			result = true;
		} catch (Exception ex) {
			result = false;
		}

		if (result == false) {
			try {
				stop(true);
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
			}
		}

		return result;
	}

	public void pausePlay() {
		try {
			if (mPlayer != null) {
				if (mPlayer.isPlaying()) {
					mPlayer.pause();
				} else {
					mPlayer.start();
				}
			}

			if (mVitamoPlayer != null) {
				if (mVitamoPlayer.isPlaying()) {
					mVitamoPlayer.pause();
				} else {
					mVitamoPlayer.start();
				}
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	public boolean play() {
		try {
			MyLog.d(TAG, "==play start=="+mPlayer);
			if (mPlayer != null && mPlayer.isPlaying()) {
				MyLog.d(TAG, "==play start 1==");
				return true;
			}

			if (mPlayer != null) {
				MyLog.d(TAG, "==play start 2==");
				mPlayer.start();
				return true;
			}

			if (mVitamoPlayer != null && mVitamoPlayer.isPlaying()) {
				MyLog.d(TAG, "==play start 3==");
				return true;
			}

			if (mVitamoPlayer != null) {
				MyLog.d(TAG, "==play start 4==");
				mVitamoPlayer.start();
				return true;
			}
			MyLog.d(TAG, "==play start 5==");
			return false;
		} catch (Exception e) {
			MyLog.d(TAG, "==play start 6==");
			return false;
		}
	}

	public void stop(boolean enableDelegate) throws Exception {
		try {
			clearTimeOut();

			if (mPlayer != null) {
				try {
					mPlayer.stop();
				} catch (Exception ex) {
					MyLog.e(TAG, ex);
				}

				try {
					if (mListener != null && enableDelegate == true)
						mListener.onPlayerStop(this);
				} catch (Exception ex) {
					MyLog.e(TAG, ex);
				}

				mPlayer.reset();
				mPlayer.release();
				mPlayer = null;
			}

			if (mVitamoPlayer != null) {

				try {
					mVitamoPlayer.stop();
				} catch (Exception ex) {
					MyLog.e(TAG, ex);
				}

				try {
					if (mListener != null)
						mListener.onPlayerStop(this);
				} catch (Exception ex) {
					MyLog.e(TAG, ex);
				}

				mVitamoPlayer.reset();
				mVitamoPlayer.release();
				mVitamoPlayer = null;
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	@Override public boolean onError(MediaPlayer player, int what, int extra) {
		try {
			stop(false);
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
		if (mListener != null) {
			mListener.onPlayerError(mFilePath, what, extra);
		}
		return false;
	}

	public void onCompletion(MediaPlayer player) {
		try {
			stop(true);
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	@Override public void onBufferingUpdate(MediaPlayer player, int percent) {
		if (isPlaying() == true)
			clearTimeOut();
		try {
			if (percent > PERCENT && isPlaying() == false && mListener != null)
				mListener.onPlayerReadyToPlay(this);
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	public int getCurrentPlaybackPosition() {
		if (mPlayer != null)
			return mPlayer.getCurrentPosition();

		if (mVitamoPlayer != null)
			return (int) mVitamoPlayer.getCurrentPosition();

		return 0;
	}

	public boolean isPlaying() {
		if (mPlayer != null)
			return mPlayer.isPlaying();

		if (mVitamoPlayer != null)
			return mVitamoPlayer.isPlaying();

		return false;
	}

	public long getDuration() {
		if (mPlayer != null)
			return mPlayer.getDuration();

		if (mVitamoPlayer != null)
			return mVitamoPlayer.getDuration();

		return -1;
	}

	@Override public void onPrepared(MediaPlayer arg0) {
		if (isPlaying() == false && mListener != null)
			mListener.onPlayerReadyToPlay(this);
	}

	@Override public boolean onInfo(MediaPlayer arg0, int what, int extra) {
		MyLog.i(TAG, "onInfo what: " + String.valueOf(what) + ", extra: " + String.valueOf(extra));
		if (what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING || what == MediaPlayer.MEDIA_INFO_BUFFERING_END || what == MediaPlayer.MEDIA_INFO_BUFFERING_START || what == MediaPlayer.MEDIA_INFO_UNKNOWN) {
			try {
				stop(false);
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
			}

			if (mListener != null) {
				mListener.onPlayerError(mFilePath, what, extra);
			}
			return true;
		}
		return false;
	}
}
