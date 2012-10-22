/**
 * 			Copyright (C) 2011 by Ethan Hall
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 * 	in the Software without restriction, including without limitation the rights
 * 	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * 	copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *  
 */

package com.kopysoft.MorseMessenger.recieve;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.util.Log;

import com.kopysoft.MorseMessenger.Defines;
import com.kopysoft.MorseMessenger.StringMap;

/** How all tones will be played
 * 
 * @author Ethan Hall
 */
public class PlayMessage extends IntentService {

	private static final String TAG = Defines.TAG + " - PlayMessage";
	private static final boolean printDebugMessages = Defines.printDebugMessages;

	public PlayMessage() {
		super("PlayMessage");
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals("stop"))
            stopSelf();
        else
            onStart(intent, startId);
        return START_NOT_STICKY;
    }


	@Override
	protected void onHandleIntent(Intent intent) {
		String Message = intent.getExtras().getString("message").toUpperCase();
		int delay = intent.getExtras().getInt("speed", 75);//tone
		int tone = intent.getExtras().getInt("tone", ToneGenerator.TONE_DTMF_0);
		boolean vib = intent.getExtras().getBoolean("viberate", false);
		int vibSpeed = intent.getExtras().getInt("viberateSpeed", 100);
		if(printDebugMessages) Log.d(TAG, Message);

		try{
			Thread.sleep(3000);
			if(!vib)
				playMessageSound(Message, delay, tone, 100);
			else
				playMessageVib(Message, vibSpeed, getBaseContext());
		} catch( Exception e){
			Log.d(TAG, e.toString());
		}
	}

	/**	Method of playMessageVib(String message, int delay, Context context) throws InterruptedException
	 * 
	 * Plays a message out on the vibrator, on general you should set the delay to be 3 times that
	 * 	of the sound delay.
	 * 
	 * @author Ethan Hall
	 * @param message	STring that will be viberated out
	 * @param delay		Base delay in MS
	 * @param context	Context of this service
	 * @throws InterruptedException
	 */
	private void playMessageVib(String message, int delay, Context context) throws InterruptedException{
		Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		char[] charMessage = message.toCharArray();
		StringMap map = new StringMap();
		int timeToPlay = 0;
        AudioManager audio = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        int prevLevel = audio.getStreamVolume(AudioManager.STREAM_RING);

		for(int i = 0; i < charMessage.length; i++){
			String charToSend = map.getVal(Character.toString((char)charMessage[i]));
			if(printDebugMessages) Log.d(TAG, "Message[" + i + "]: " + charMessage[i]);
			if(charToSend != null){
				byte[] brokenString = charToSend.getBytes();
				for(int j = 0; j < brokenString.length; j++){

					if(((char)brokenString[j]) == '.'){
						timeToPlay = delay;
					} else if(((char)brokenString[j]) == '-') {
						timeToPlay = delay * 3;
					} else if(((char)brokenString[j]) == ' ') {
						timeToPlay = delay * 2;	//Will add up to 7
					}

					if(((char)brokenString[j]) != ' '){
						vib.vibrate(timeToPlay);
					} 

					Thread.sleep(timeToPlay);
					vib.cancel();
					Thread.sleep(delay);
				}
				Thread.sleep(delay * 3);
                if(prevLevel != audio.getStreamVolume(AudioManager.STREAM_RING)){
                    break;
                }
			}
		}
	}

	/**	Method of playMessageSound(String message, int delay, int tone, int volume) throws InterruptedException
	 * 
	 * Plays a message on the speaker using ToneGenerator.
	 * 
	 * @author Ethan Hall
	 * @param message	STring that will be vibrated out
	 * @param delay		Base delay in MS
	 * @param tone		a {@link}AudioManager define
	 * @param volume	Percentage from 0 - 100 of the volume
	 * @throws InterruptedException
	 */
	private void playMessageSound(String message, int delay, int tone, int volume) throws InterruptedException{
		ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_RING,volume);
		char[] charMessage = message.toCharArray();
		StringMap map = new StringMap();
		int timeToPlay = 0;
        AudioManager audio = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        int prevLevel = audio.getStreamVolume(AudioManager.STREAM_RING);


		for(int i = 0; i < charMessage.length; i++){
			String charToSend = map.getVal(Character.toString((char)charMessage[i]));
			if(printDebugMessages) Log.d(TAG, "Message[" + i + "]: " + charMessage[i]);
			if(charToSend != null){
				byte[] brokenString = charToSend.getBytes();
				for(int j = 0; j < brokenString.length; j++){

					if(((char)brokenString[j]) == '.'){
						timeToPlay = delay;
					} else if(((char)brokenString[j]) == '-') {
						timeToPlay = delay * 3;
					} else if(((char)brokenString[j]) == ' ') {
						timeToPlay = delay * 2;	//Will add up to 7
					}

					if(((char)brokenString[j]) != ' '){
						tg.startTone(tone, timeToPlay);
					} 

					Thread.sleep(timeToPlay);
					Thread.sleep(delay);
				}
				Thread.sleep(delay * 3);
                if(prevLevel != audio.getStreamVolume(AudioManager.STREAM_RING)){
                    break;
                }
			}
		}
	}

}
