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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kopysoft.MorseMessenger.Defines;
import com.kopysoft.MorseMessenger.GetSet.PreferenceGetter;

public class SMS extends BroadcastReceiver {

	public static final String SMS_EXTRA_NAME = "pdus";
	private static final String TAG = Defines.TAG + " - SMS";
	private static final boolean printDebugMessages = Defines.printDebugMessages;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Get the SMS map from Intent
		Bundle extras = intent.getExtras();
		String messages = "";
		PreferenceGetter pg = new PreferenceGetter(context);

		if(!playMessage(context)) return;
		if(!pg.isSMSEnabled()){
			return;
		}

		// Get received SMS array
		Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );

		for ( int i = 0; i < smsExtra.length; ++i )
		{
			SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);

			String body = sms.getMessageBody().toString();
			String address = sms.getOriginatingAddress();
			String message = "";

			if(pg.isPlaySender())
				message += address;

			if(pg.isPlayBody()){
				if(message.length() != 0)
					message += ": ";
				message += body;
			}

			if(printDebugMessages) Log.d(TAG, messages);

			Intent runIntent = new Intent().setClass(context, 
					com.kopysoft.MorseMessenger.recieve.PlayMessage.class);
			runIntent.putExtra("message", message);
			runIntent.putExtra("speed", pg.getMorseSpeed());
			runIntent.putExtra("viberateSpeed",  pg.getMorseSpeedViberate());
			if (isViberate(context))
				runIntent.putExtra("viberate", true);
			else
				runIntent.putExtra("viberate", false);

			context.startService(runIntent);
		}
	}

	private boolean playMessage(Context context){
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		//RINGER_MODE_NORMAL, RINGER_MODE_SILENT, or RINGER_MODE_VIBRATE.
		int ringerStatus = audioManager.getRingerMode();
		//boolean shouldViberate = audioManager.shouldVibrate(AudioManager.VIBRATE_TYPE_RINGER);
		boolean returnValue = false;

		PreferenceGetter pg = new PreferenceGetter(context);
		boolean inNorm = pg.isPlayInNorm();
		boolean inViberate = pg.isPlayInVib();
		switch(ringerStatus){
		case(AudioManager.RINGER_MODE_NORMAL):
			if(inNorm) returnValue = true;
		//if(!returnValue && shouldViberate) returnValue = true;
		break;
		case(AudioManager.RINGER_MODE_SILENT):
			returnValue = false;
		break;
		case(AudioManager.RINGER_MODE_VIBRATE):
			if(inViberate) returnValue = true;
		//if(!returnValue && shouldViberate) returnValue = true;
		break;
		default:
			break;
		}

		return returnValue;
	}

	private boolean isViberate(Context context){
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int ringerStatus = audioManager.getRingerMode();
		boolean returnValue = false;

		switch(ringerStatus){
		case(AudioManager.RINGER_MODE_NORMAL):
			returnValue = false;
		//if(!returnValue && shouldViberate) returnValue = true;
		break;
		case(AudioManager.RINGER_MODE_SILENT):
			returnValue = false;
		break;
		case(AudioManager.RINGER_MODE_VIBRATE):
			returnValue = true;
		//if(!returnValue && shouldViberate) returnValue = true;
		break;
		default:
			break;
		}

		return returnValue;
	}
}
