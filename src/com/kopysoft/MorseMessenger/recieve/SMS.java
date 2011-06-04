package com.kopysoft.MorseMessenger.recieve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kopysoft.MorseMessenger.Defines;

public class SMS extends BroadcastReceiver {

	public static final String SMS_EXTRA_NAME = "pdus";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Get the SMS map from Intent
		Bundle extras = intent.getExtras();

		String messages = "";

		if ( extras == null )
		{
			return;
		}
		
		// Get received SMS array
		Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );

		for ( int i = 0; i < smsExtra.length; ++i )
		{
			SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);

			String body = sms.getMessageBody().toString();
			String address = sms.getOriginatingAddress();

			messages += "SMS from " + address + " :\n";                    
			messages += body + "\n";
			Log.d(Defines.TAG, messages);
			
			Intent runIntent = new Intent().setClass(context, 
					com.kopysoft.MorseMessenger.PlayMessage.class);
	    	runIntent.putExtra("message", body);
	    	runIntent.putExtra("speed", 80);
	    	context.sendBroadcast(runIntent);
		}
	}
}
