package com.kopysoft.MorseMessenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

public class PlayMessage extends BroadcastReceiver {

	private static final String TAG = Defines.TAG + " - PlayMessage";
	private static final boolean printDebugMessages = Defines.printDebugMessages;

	@Override
	public void onReceive(Context context, Intent intent) {
		String Message = intent.getExtras().getString("message").toUpperCase();
		int Speed = intent.getExtras().getInt("speed", 75);
		if(printDebugMessages) Log.d(TAG, Message);

		try{
			playMessage(Message, Speed, 100);
		} catch( Exception e){
			Log.d(TAG, e.toString());
		}
	}

	private void playMessage(String message, int delay, int volume) throws InterruptedException{
		ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,volume);
		char[] charMessage = message.toCharArray();
		StringMap map = new StringMap();
		int timeToPlay = 0;
		
		Log.d(TAG, Character.toString((char)charMessage[0]));

		for(int i = 0; i < charMessage.length; i++){
			String charToSend = map.getVal(Character.toString((char)charMessage[i]));
			if(charToSend != null){
				byte[] brokenString = charToSend.getBytes();
				for(int j = 0; j < brokenString.length; j++){
					
					if(((char)brokenString[j]) == '.'){
						timeToPlay = delay;
					} else if(((char)brokenString[j]) == '-') {
						timeToPlay = delay * 3;
					} else if(((char)brokenString[j]) == ' ') {
						timeToPlay = delay * 6;
					}

					if(((char)brokenString[j]) == ' '){
						tg.startTone(ToneGenerator.TONE_CDMA_SIGNAL_OFF, delay);
					} else {
						tg.startTone(ToneGenerator.TONE_DTMF_0, timeToPlay);
					}

					Thread.sleep(timeToPlay);

					tg.startTone(ToneGenerator.TONE_CDMA_SIGNAL_OFF, delay);

					Thread.sleep(delay);
				}
			}
		}
	}

}
