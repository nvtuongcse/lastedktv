package vn.com.sonca.RemoteControl;

public class RemoteIRCode {

	public static final int IRC_0 = 1;
	public static final int IRC_1 = 2;
	public static final int IRC_2 = 3;
	public static final int IRC_3 = 4;
	public static final int IRC_4 = 5;
	public static final int IRC_5 = 6;
	public static final int IRC_6 = 7;
	public static final int IRC_7 = 8;
	public static final int IRC_8 = 9;
	public static final int IRC_9 = 10;
	public static final int IRC_ENTER = 11;
	public static final int IRC_1STRES = 12;
	public static final int IRC_VOLUME_UP = 13;
	public static final int IRC_VOLUME_DN = 14;
	public static final int IRC_MUTE = 15;
	public static final int IRC_KEY_UP = 16;
	public static final int IRC_KEY_DN = 17;
	public static final int IRC_TEMPO_UP = 18;
	public static final int IRC_TEMPO_DOWN = 19;
	public static final int IRC_MELODY = 20;
	public static final int IRC_VOCAL = 21;
	public static final int IRC_TONE = 22;
	public static final int IRC_PAUSEPLAY = 23;
	public static final int IRC_STOP = 24;
	public static final int IRC_SCORE = 25;
	public static final int IRC_DANCE = 26;
	public static final int IRC_RIGHT = 27;
	public static final int IRC_REPEAT = 28;
	public static final int IRC_DIGIT_MENU = 29;
	public static final int IRC_HIDE_MENU = 30;

	private int irEvent;
	private int irCode;

	public RemoteIRCode() {
		
	}

	public RemoteIRCode(int irEvent, int irCode) {		
		this.irEvent = irEvent;
		this.irCode = irCode;
	}

	public int getIrEvent() {
		return irEvent;
	}

	public void setIrEvent(int irEvent) {
		this.irEvent = irEvent;
	}

	public int getIrCode() {
		return irCode;
	}

	public void setIrCode(int irCode) {
		this.irCode = irCode;
	}

}
