package com.moonbelly.youtube;

import java.io.File;
import java.util.ArrayList;










import vn.com.hanhphuc.karremote.R;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class YoutubePlayerActivity extends YouTubeFailureRecoveryActivity {

	private final String TAG = "YoutubePlayerActivity";

	private Context context;

	private YouTubePlayer youtubePlayer;
	private YouTubePlayerView youTubePlayerView;

	private AudioManager audio;

	private String youtubePath = "";

	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult errorReason) {
		Toast.makeText(this, errorReason.toString(), Toast.LENGTH_LONG).show();
		
		this.finish();
		
		Intent playIntent = new Intent(Intent.ACTION_VIEW);
    	playIntent.setData(Uri.parse("http://www.youtube.com/watch?v="+ youtubePath));
    	playIntent.putExtra("force_fullscreen", true);
    	startActivity(playIntent);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().getDecorView().setSystemUiVisibility(
//				View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Bundle b = getIntent().getExtras();
		youtubePath = b.getString("youtubePath", "");

		if (youtubePath.equals("")) {
			this.finish();
		}

		this.context = getApplicationContext();
		
		setContentView(R.layout.activity_main_youtube);
		initControl();
	}

	@Override
	protected void onStart() {
		youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY_MHTouch1, this);
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		if(youtubePlayer != null){
			youtubePlayer.release();
		}
		super.onPause();
	}

	private void initControl() {
		youTubePlayerView = (YouTubePlayerView) findViewById(R.id.videoPreview);

	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean wasRestored) {
		youtubePlayer = player;
		youtubePlayer.setPlayerStyle(PlayerStyle.DEFAULT);

		youtubePlayer
				.setPlayerStateChangeListener(new PlayerStateChangeListener() {

					@Override
					public void onVideoStarted() {

					}

					@Override
					public void onVideoEnded() {
						YoutubePlayerActivity.this.finish();
					}

					@Override
					public void onLoading() {

					}

					@Override
					public void onLoaded(String arg0) {

					}

					@Override
					public void onError(ErrorReason arg0) {
						YoutubePlayerActivity.this.finish();
					}

					@Override
					public void onAdStarted() {

					}
				});

		if (!wasRestored) {
			youtubePlayer.loadVideo(youtubePath);
		}

	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		return youTubePlayerView;

	}

}
