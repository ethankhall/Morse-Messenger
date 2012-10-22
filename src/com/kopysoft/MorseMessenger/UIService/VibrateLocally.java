package com.kopysoft.MorseMessenger.UIService;

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

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.kopysoft.MorseMessenger.Defines;
import com.kopysoft.MorseMessenger.R;
import com.kopysoft.MorseMessenger.StringMap;

public class VibrateLocally extends AsyncTask<Object, String, Void> {
	
	private static String TAG = Defines.TAG + " - PlayLocally";
	private static final boolean printDebugMessages = Defines.printDebugMessages;
	
	private int speed = 0;
	private Activity mActicity = null;
	private String mMessage = null;
	
	public VibrateLocally(Activity activity){
		mActicity = activity;
	}

	protected void onPreExecute(){
		WebView singALong = (WebView) mActicity.findViewById(R.id.singALong);
		singALong.setVisibility(WebView.VISIBLE);
		singALong.loadData("<br/><br/>", "text/html", "UTF-8");

		SeekBar speedBar = 	(SeekBar) mActicity.findViewById(R.id.seekBarSpeed);

		speed = (150 - (30 + speedBar.getProgress())) * 3;	//make it 3 times longer
		
		Button VibrateButton = (Button)mActicity.findViewById(R.id.vibrateMorse);
		VibrateButton.setText("Stop");
		Button playButton = (Button)mActicity.findViewById(R.id.playMorse);
		playButton.setEnabled(false);
		Button translateButton = (Button)mActicity.findViewById(R.id.translateMorse);
		translateButton.setEnabled(false);
		EditText messageView = (EditText) mActicity.findViewById(R.id.messageInput);
		mMessage = messageView.getText().toString();
		messageView.setEnabled(false);
		LinearLayout seekbars = (LinearLayout)mActicity.findViewById(R.id.SeekbarView);
		seekbars.setVisibility(View.GONE);
	}

	private String getHTML(int index, String[] message, char[] messageText, boolean enableBold){
		String tempOutput = "<center><table>";
		if(printDebugMessages) Log.d(TAG, "index: " + index);
		String topRow = "<tr>";
		String bottomRow = "<tr>";

		int messageEnd = Math.min(message.length, index + 5); 		
		int messageStart = Math.max(0, index - 4);

		for(int i = messageStart; i < messageEnd; i++){
			String topTemp = Character.toString(messageText[i]);
			String bottomTemp = message[i];
			if( i == index && enableBold){
				topTemp = "<b>" + topTemp + "</b>";
				bottomTemp = "<b>" + bottomTemp + "</b>";
			}
			topRow += "<td>" + topTemp + "</td>";
			bottomRow += "<td>" + bottomTemp + "</td>";
		}
		topRow += "</tr>";
		bottomRow += "</tr>";
		tempOutput += topRow + bottomRow + "</table></center>";
		return tempOutput;
	}

	protected void onProgressUpdate(String... progress) {
		WebView singALong = (WebView) mActicity.findViewById(R.id.singALong);
		//singALong.loadData(progress[0], "text/html", "UTF-8");
        singALong.loadDataWithBaseURL(null, progress[0], "text/html", "utf-8", null);
	}

	protected void onPostExecute(Void param) {
		WebView singALong = (WebView) mActicity.findViewById(R.id.singALong);
		singALong.setVisibility(WebView.INVISIBLE);
		singALong.loadData("<br/><br/>", "text/html", "UTF-8");
		
		Button VibrateButton = (Button)mActicity.findViewById(R.id.vibrateMorse);
		VibrateButton.setText("Vibrate");
		Button playButton = (Button)mActicity.findViewById(R.id.playMorse);
		playButton.setEnabled(true);
		Button translateButton = (Button)mActicity.findViewById(R.id.translateMorse);
		translateButton.setEnabled(true);
		LinearLayout seekbars = (LinearLayout)mActicity.findViewById(R.id.SeekbarView);
		seekbars.setVisibility(View.VISIBLE);
		EditText messageView = (EditText) mActicity.findViewById(R.id.messageInput);
		messageView.setEnabled(true);
		
	}
	
	protected void  onCancelled(Void param){
		WebView singALong = (WebView) mActicity.findViewById(R.id.singALong);
		singALong.setVisibility(WebView.INVISIBLE);
		singALong.loadData("<br/><br/>", "text/html", "UTF-8");
		
		Button VibrateButton = (Button)mActicity.findViewById(R.id.vibrateMorse);
		VibrateButton.setText("Vibrate");
		Button playButton = (Button)mActicity.findViewById(R.id.playMorse);
		playButton.setEnabled(true);
		Button translateButton = (Button)mActicity.findViewById(R.id.translateMorse);
		translateButton.setEnabled(true);
		
		//set up Toast
		Context context = mActicity.getApplicationContext();
		CharSequence text = "Message Canceled!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	protected Void doInBackground(Object... params) {
		Context context = (Context)params[0];
		char[] messageText = mMessage.toCharArray();
		String[] message = new String[messageText.length];
		StringMap map = new StringMap();
		
		Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		for(int i = 0; i < messageText.length; i++){
			message[i] = map.getVal(Character.toString((char)messageText[i]).toUpperCase());
			if(printDebugMessages) Log.d(TAG, "Message[" + i + "]: " + messageText[i]);
		}

        PowerManager pm = (PowerManager)mActicity.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Morse Messenger");
        mWakeLock.acquire();

		int timeToPlay = 0;
        try{
            Thread.sleep(speed * 3);
            for(int i = 0; i < messageText.length; i++){
                String messageToSend = getHTML(i, message, messageText, true);
                if(printDebugMessages) Log.d(TAG, messageToSend);
                publishProgress(messageToSend);
                //Send Morse Code
                String charToSend = message[i];
                if(printDebugMessages) Log.d(TAG,"Char: " + message[i]);
                if(charToSend != null){
                    for(int j = 0; j < charToSend.length(); j++){
                        if(printDebugMessages) Log.d(TAG,"Play: " + charToSend.charAt(j));

                        if( charToSend.charAt(j) == '.'){
                            timeToPlay = speed;
                        } else if(charToSend.charAt(j) == '-') {
                            timeToPlay = speed * 3;
                        } else if(charToSend.charAt(j) == ' ') {
                            timeToPlay = speed * 2;	//Will add up to 7
                        }

                        if(charToSend.charAt(j) != ' '){
                            vib.vibrate(timeToPlay);
                        }

                        Thread.sleep(timeToPlay);
                        Thread.sleep(speed);
                    }
                    Thread.sleep(speed * 3);
                }
                if(isCancelled()){
                    Log.d(TAG, "isCancelled()");
                    break;
                }
            }
            String messageToSend = getHTML(messageText.length, message, messageText, false);
            publishProgress(messageToSend);
            Thread.sleep(speed * 14);
        } catch(Exception e){
            Log.e(TAG, e.toString());
            if(!isCancelled()){
                Log.e(TAG, "Thread Error");
            }
            if (mWakeLock != null) {
                mWakeLock.release();
                mWakeLock = null;
            }
        }
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
		return null;
	}

}
