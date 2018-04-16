package vn.com.sonca.zktv.main;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import app.sonca.Dialog.ScoreLayout.BaseDialog;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogCaoCap extends BaseDialog implements OnClickListener {

	private Context context;
	public DialogCaoCap(Context context, Window window) {
		super(context, window);
		this.context = context;
	}
	
	private KTVMainActivity ktvMainActivity;
	public void setMainActivity(KTVMainActivity ktvMainActivity){
		this.ktvMainActivity = ktvMainActivity;
	}
	
	private OnDialogCaoCaplistener listener;
	public interface OnDialogCaoCaplistener {
		public void OnClick(int idView);
	}
	
	public void setOnDialogCaoCaplistener(OnDialogCaoCaplistener listener){
		this.listener = listener;
	}
	
	private ServerStatus startServerStatus;
	public void setStartStatus(ServerStatus startServerStatus){
		this.startServerStatus = startServerStatus;
	}

	@Override
	protected int getIdLayout() {
		return R.layout.ktv_dialog_caocap;
	}

	@Override
	protected int getTimerShow() {
		return 15000;
	}
	
	private View layoutSpace1;
	private RelativeLayout layoutSpace2;
	private LinearLayout layoutSpace3;

	private ViewCaoCap melodyGiam, melodyTang, tempoGiam, tempoTang, tone, hoaAm, macDinh;
	private ViewCaoCap chinhMiDi, ngatTieng, tamDung, giamTone, tangTone, hatLai, chamDiem;
	@Override
	protected View getView(View contentView) {
		contentView.findViewById(R.id.layout).setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				dismissDialog();
			}
		});
		
		layoutSpace1 = (View)contentView.findViewById(R.id.layoutSpace1);
		layoutSpace2 = (RelativeLayout)contentView.findViewById(R.id.layoutSpace2);
		layoutSpace3 = (LinearLayout)contentView.findViewById(R.id.layoutSpace3);
		
		melodyGiam = (ViewCaoCap)contentView.findViewById(R.id.melodyGiam);
		melodyTang = (ViewCaoCap)contentView.findViewById(R.id.melodyTang);
		tempoGiam = (ViewCaoCap)contentView.findViewById(R.id.tempoGiam);
		tempoTang = (ViewCaoCap)contentView.findViewById(R.id.tempoTang);
		tone = (ViewCaoCap)contentView.findViewById(R.id.tone);
		hoaAm = (ViewCaoCap)contentView.findViewById(R.id.hoaAm);
		macDinh = (ViewCaoCap)contentView.findViewById(R.id.macDinh);
		
		chinhMiDi = (ViewCaoCap)contentView.findViewById(R.id.chinhMiDi);
		ngatTieng = (ViewCaoCap)contentView.findViewById(R.id.ngatTieng);
		tamDung = (ViewCaoCap)contentView.findViewById(R.id.tamDung);
		giamTone = (ViewCaoCap)contentView.findViewById(R.id.giamTone);
		tangTone = (ViewCaoCap)contentView.findViewById(R.id.tangTone);
		hatLai = (ViewCaoCap)contentView.findViewById(R.id.hatLai);
		chamDiem = (ViewCaoCap)contentView.findViewById(R.id.chamDiem);
		
		melodyGiam.setDataView(
			context.getResources().getString(R.string.ktv_caocap_1), 
			context.getResources().getDrawable(R.drawable.ktv_caocap_melody_giam),
			context.getResources().getDrawable(R.drawable.ktv_caocap_melody_giam_xam));
		melodyTang.setDataView(
				context.getResources().getString(R.string.ktv_caocap_1), 
				context.getResources().getDrawable(R.drawable.ktv_caocap_melody_tang),
				context.getResources().getDrawable(R.drawable.ktv_caocap_melody_tang_xam));
		tempoGiam.setDataView(
				context.getResources().getString(R.string.ktv_caocap_2), 
				context.getResources().getDrawable(R.drawable.ktv_caocap_temp_giam),
				context.getResources().getDrawable(R.drawable.ktv_caocap_temp_giam_xam));
		tempoTang.setDataView(
				context.getResources().getString(R.string.ktv_caocap_2), 
				context.getResources().getDrawable(R.drawable.ktv_caocap_temp_tang),
				context.getResources().getDrawable(R.drawable.ktv_caocap_temp_tang_xam));
		tone.setDataView(
				context.getResources().getString(R.string.ktv_caocap_3), 
				context.getResources().getDrawable(R.drawable.ktv_caocap_tone),
				context.getResources().getDrawable(R.drawable.ktv_caocap_tone_xam));
		hoaAm.setDataView(
				context.getResources().getString(R.string.ktv_caocap_4), 
				context.getResources().getDrawable(R.drawable.ktv_caocap_hoaam),
				context.getResources().getDrawable(R.drawable.ktv_caocap_hoaam_xam));
		macDinh.setDataView(
				context.getResources().getString(R.string.ktv_caocap_5), 
				context.getResources().getDrawable(R.drawable.ktv_caocap_default),
				context.getResources().getDrawable(R.drawable.ktv_caocap_default_xam));
	
		chinhMiDi.setDataView(
				context.getResources().getString(R.string.ktv_caocap_6),
				context.getResources().getDrawable(R.drawable.ktv_caocap_midi),
				context.getResources().getDrawable(R.drawable.ktv_caocap_midi_xam));
		ngatTieng.setDataView(
				context.getResources().getString(R.string.ktv_caocap_7),
				context.getResources().getDrawable(R.drawable.ktv_caocap_mute),
				context.getResources().getDrawable(R.drawable.ktv_caocap_mute_xam));
		tamDung.setDataView(
				context.getResources().getString(R.string.ktv_caocap_8),
				context.getResources().getDrawable(R.drawable.ktv_caocap_pause),
				context.getResources().getDrawable(R.drawable.ktv_caocap_pause_xam));
		giamTone.setDataView(
				context.getResources().getString(R.string.ktv_caocap_9),
				context.getResources().getDrawable(R.drawable.ktv_caocap_key_giam),
				context.getResources().getDrawable(R.drawable.ktv_caocap_key_giam_xam));
		tangTone.setDataView(
				context.getResources().getString(R.string.ktv_caocap_10),
				context.getResources().getDrawable(R.drawable.ktv_caocap_key_tang),
				context.getResources().getDrawable(R.drawable.ktv_caocap_key_tang_xam));
		hatLai.setDataView(
				context.getResources().getString(R.string.ktv_main_5),
				context.getResources().getDrawable(R.drawable.ktv_control_repeat),
				context.getResources().getDrawable(R.drawable.ktv_control_repeat_xam));
		chamDiem.setDataView(
				context.getResources().getString(R.string.ktv_caocap_12),
				context.getResources().getDrawable(R.drawable.ktv_caocap_score),
				context.getResources().getDrawable(R.drawable.ktv_caocap_score_xam));
		
		melodyGiam.setOnClickListener(this);
		melodyTang.setOnClickListener(this);
		tempoGiam.setOnClickListener(this);
		tempoTang.setOnClickListener(this);
		tone.setOnClickListener(this);
		hoaAm.setOnClickListener(this);
		macDinh.setOnClickListener(this);
		
		chinhMiDi.setOnClickListener(this);
		ngatTieng.setOnClickListener(this);
		tamDung.setOnClickListener(this);
		giamTone.setOnClickListener(this);
		tangTone.setOnClickListener(this);
		hatLai.setOnClickListener(this);
		chamDiem.setOnClickListener(this);
		
		if(ktvMainActivity.viewChinhMiDi != null){
			// state
			melodyGiam.setEnableView(ktvMainActivity.viewMelodyGiam.getStateView());
			melodyTang.setEnableView(ktvMainActivity.viewMelodyTang.getStateView());
			tempoGiam.setEnableView(ktvMainActivity.viewTempoGiam.getStateView());
			tempoTang.setEnableView(ktvMainActivity.viewTempoTang.getStateView());
			tone.setEnableView(ktvMainActivity.viewTone.getStateView());
			hoaAm.setEnableView(ktvMainActivity.viewHoaAm.getStateView());
			macDinh.setEnableView(ktvMainActivity.viewMacDinh.getStateView());
			
			chinhMiDi.setEnableView(ktvMainActivity.viewChinhMiDi.getStateView());			
			ngatTieng.setEnableView(ktvMainActivity.viewNgatTieng.getStateView());			
			tamDung.setEnableView(ktvMainActivity.viewTamDung.getStateView());
			giamTone.setEnableView(ktvMainActivity.viewGiamTone.getStateView());
			tangTone.setEnableView(ktvMainActivity.viewTangTone.getStateView());
			hatLai.setEnableView(ktvMainActivity.viewHatLai.getStateView());
			chamDiem.setEnableView(ktvMainActivity.viewChamDiem.getStateView());
						
			// TODO value
			ngatTieng.setMute(ktvMainActivity.viewNgatTieng.getMute());
			tamDung.setPlayPause(ktvMainActivity.viewTamDung.isPlayPause());
			giamTone.setKey(ktvMainActivity.viewGiamTone.getKey());
			chamDiem.setScoreView(ktvMainActivity.viewChamDiem.getScoreView());
			melodyGiam.setMelody(ktvMainActivity.viewMelodyGiam.getMelody());
			tempoGiam.setTempo(ktvMainActivity.viewTempoGiam.getTempo());
			tone.setToneView(ktvMainActivity.viewTone.getToneView());
		} else {
			processStartStatus(startServerStatus);
		}
		
		ktvMainActivity.viewMelodyGiam = melodyGiam;
		ktvMainActivity.viewMelodyTang = melodyTang;
		ktvMainActivity.viewTempoGiam = tempoGiam;
		ktvMainActivity.viewTempoTang = tempoTang;
		ktvMainActivity.viewTone = tone;
		ktvMainActivity.viewHoaAm = hoaAm;
		ktvMainActivity.viewMacDinh = macDinh;
		
		ktvMainActivity.viewChinhMiDi = chinhMiDi;
		ktvMainActivity.viewNgatTieng = ngatTieng;
		ktvMainActivity.viewTamDung = tamDung;
		ktvMainActivity.viewGiamTone = giamTone;
		ktvMainActivity.viewTangTone = tangTone;
		ktvMainActivity.viewHatLai = hatLai;
		ktvMainActivity.viewChamDiem = chamDiem;
		
		toogleRow2();
		
		return contentView;
	}
	
	private boolean flagOpenRow2 = true;
	private void toogleRow2(){
		boolean flag = !flagOpenRow2;
		if(flag){ // OPEN
			layoutSpace1.setVisibility(View.VISIBLE);
			layoutSpace2.setVisibility(View.VISIBLE);
			layoutSpace3.setVisibility(View.VISIBLE);			
				
		} else { // CLOSE
			layoutSpace1.setVisibility(View.INVISIBLE);
			layoutSpace2.setVisibility(View.INVISIBLE);
			layoutSpace3.setVisibility(View.INVISIBLE);
			
		}
		flagOpenRow2 = flag;
	}
	
	public void openRow2(boolean flag){
		if(flag){ // OPEN
			layoutSpace1.setVisibility(View.VISIBLE);
			layoutSpace2.setVisibility(View.VISIBLE);
			layoutSpace3.setVisibility(View.VISIBLE);			
				
		} else { // CLOSE
			layoutSpace1.setVisibility(View.INVISIBLE);
			layoutSpace2.setVisibility(View.INVISIBLE);
			layoutSpace3.setVisibility(View.INVISIBLE);
			
		}
		flagOpenRow2 = flag;
	}

	@Override
	protected void OnShow() {
		
	}

	@Override
	protected void OnDismiss() {
		
	}

	@Override
	protected void OnReceiveDpad(KeyEvent event, int key) {
		
	}
	
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.chinhMiDi){
			toogleRow2();
			return;
		}
		
		if(listener != null){
			listener.OnClick(view.getId());
		}
		
		startTimerHide();
		
	}
	
	public void syncFromServer(ServerStatus status){
				
	}
	
	// TODO
	private void processStartStatus(ServerStatus status){
		if(status != null){
			ngatTieng.setMute(status.isMuted());

			UpdateControlView(status.getMediaType());
			
			if (status.getPlayingSongID() != 0) {
				ngatTieng.setEnableView(View.VISIBLE);
				
			}
			
			tamDung.setPlayPause(!status.isPaused());
			chamDiem.setScoreView(status.getCurrentScore());
			giamTone.setKey(status.getCurrentKey());
			
			melodyGiam.setMelody(status.getCurrentMelody());
			tempoGiam.setTempo(status.getCurrentTempo());
			tone.setToneView(status.getCurrentTone());
		}
	}
	
	// TODO
	private void UpdateControlView(int type) {	
		
		ngatTieng.setEnableView(View.VISIBLE);
		chinhMiDi.setEnableView(View.INVISIBLE);
		tamDung.setEnableView(View.VISIBLE);
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
			ngatTieng.setEnableView(View.VISIBLE);
			chinhMiDi.setEnableView(View.INVISIBLE);
			tamDung.setPlayPause(true);
			
			giamTone.setEnableView(View.INVISIBLE);
			tangTone.setEnableView(View.INVISIBLE);
			chamDiem.setEnableView(View.INVISIBLE);
			
			melodyGiam.setEnableView(View.INVISIBLE);
			melodyTang.setEnableView(View.INVISIBLE);
			tempoGiam.setEnableView(View.INVISIBLE);
			tempoTang.setEnableView(View.INVISIBLE);
			tone.setEnableView(View.INVISIBLE);
			
			return;
		}
		
		if (type == 0x07) {
			return;
		}

		chamDiem.setEnableView(View.VISIBLE);
		
		melodyGiam.setEnableView(View.INVISIBLE);
		melodyTang.setEnableView(View.INVISIBLE);
		tempoGiam.setEnableView(View.INVISIBLE);
		tempoTang.setEnableView(View.INVISIBLE);
		tone.setEnableView(View.VISIBLE);
		
		MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
		if (aType == MEDIA_TYPE.MIDI) {
			chinhMiDi.setEnableView(View.VISIBLE);
			giamTone.setEnableView(View.VISIBLE);
			tangTone.setEnableView(View.VISIBLE);
			melodyGiam.setEnableView(View.VISIBLE);
			melodyTang.setEnableView(View.VISIBLE);
			tempoGiam.setEnableView(View.VISIBLE);
			tempoTang.setEnableView(View.VISIBLE);
			return;
		} else if (aType == MEDIA_TYPE.MP3) {
			giamTone.setEnableView(View.VISIBLE);
			tangTone.setEnableView(View.VISIBLE);
			return;
		} else if (aType == MEDIA_TYPE.SINGER) {
			giamTone.setEnableView(View.VISIBLE);
			tangTone.setEnableView(View.VISIBLE);
			return;
		} else if (aType == MEDIA_TYPE.VIDEO) {
			giamTone.setEnableView(View.VISIBLE);
			tangTone.setEnableView(View.VISIBLE);
			return;
		} else {
			giamTone.setEnableView(View.INVISIBLE);
			tangTone.setEnableView(View.INVISIBLE);
			tone.setEnableView(View.INVISIBLE);
		}
	}

	
	
}
