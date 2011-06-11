package com.kopysoft.MorseMessenger.UIService;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kopysoft.MorseMessenger.Defines;
import com.kopysoft.MorseMessenger.R;
import com.kopysoft.MorseMessenger.StringMap;
import com.kopysoft.MorseMessenger.GetSet.PreferenceGetter;

public class PlayLocally extends AsyncTask<String, String, Void> {

	private static String TAG = Defines.TAG + " - PlayLocally";
	private static final boolean printDebugMessages = Defines.printDebugMessages;

	private int speed = 0;
	private int tone = 0;
	private Activity mActicity = null;
	private String mMessage = null;

	public PlayLocally(Activity activity){
		mActicity = activity;
	}

	protected void onPreExecute(){
		WebView singALong = (WebView) mActicity.findViewById(R.id.singALong);
		singALong.setVisibility(WebView.VISIBLE);
		singALong.loadData("<br/><br/>", "text/html", "UTF-8");

		SeekBar speedBar = 	(SeekBar) mActicity.findViewById(R.id.seekBarSpeed);
		SeekBar toneBar = 	(SeekBar) mActicity.findViewById(R.id.seekBarTone);

		speed = 150 - (30 + speedBar.getProgress());
		tone = PreferenceGetter.getMorseTone(toneBar.getProgress());

		Button playButton = (Button)mActicity.findViewById(R.id.playMorse);
		playButton.setText("Stop");
		Button viberateButton = (Button)mActicity.findViewById(R.id.viberateMorse);
		viberateButton.setEnabled(false);
		Button translateButton = (Button)mActicity.findViewById(R.id.translateMorse);
		translateButton.setEnabled(false);
		LinearLayout seekbars = (LinearLayout)mActicity.findViewById(R.id.SeekbarView);
		seekbars.setVisibility(View.GONE);
		EditText messageView = (EditText) mActicity.findViewById(R.id.messageInput);
		mMessage = messageView.getText().toString();
		messageView.setEnabled(false);
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
		singALong.loadData(progress[0], "text/html", "UTF-8");
	}

	protected void onPostExecute(Void param) {
		WebView singALong = (WebView) mActicity.findViewById(R.id.singALong);
		singALong.setVisibility(WebView.INVISIBLE);
		singALong.loadData("<br/><br/>", "text/html", "UTF-8");

		Button playButton = (Button)mActicity.findViewById(R.id.playMorse);
		playButton.setText("Play");
		Button viberateButton = (Button)mActicity.findViewById(R.id.viberateMorse);
		viberateButton.setEnabled(true);
		Button translateButton = (Button)mActicity.findViewById(R.id.translateMorse);
		translateButton.setEnabled(true);
		LinearLayout seekbars = (LinearLayout)mActicity.findViewById(R.id.SeekbarView);
		seekbars.setVisibility(View.VISIBLE);
		EditText messageView = (EditText) mActicity.findViewById(R.id.messageInput);
		messageView.setEnabled(true);
	}

	@Override
	protected Void doInBackground(String... params) {
		ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
		char[] messageText = mMessage.toCharArray();
		String[] message = new String[messageText.length];
		StringMap map = new StringMap();

		for(int i = 0; i < messageText.length; i++){
			message[i] = map.getVal(Character.toString((char)messageText[i]).toUpperCase());
			if(printDebugMessages) Log.d(TAG, "Message[" + i + "]: " + messageText[i]);
		}

		int timeToPlay = 0;
		try{
			Thread.sleep(speed * 3);
			for(int i = 0; i < messageText.length; i++){
				String messageToSend = getHTML(i, message, messageText, true);
				if(printDebugMessages) Log.d(TAG, messageToSend);
				publishProgress(messageToSend);
				//Send Morse Code
				String charToSend = message[i];
				if(charToSend != null){
					byte[] brokenString = charToSend.getBytes();
					for(int j = 0; j < brokenString.length; j++){

						if(((char)brokenString[j]) == '.'){
							timeToPlay = speed;
						} else if(((char)brokenString[j]) == '-') {
							timeToPlay = speed * 3;
						} else if(((char)brokenString[j]) == ' ') {
							timeToPlay = speed * 2;	//Will add up to 7
						}

						if(((char)brokenString[j]) != ' '){
							tg.startTone(tone, timeToPlay);
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
			if(!isCancelled()){
				Log.e(TAG, "Thread Error");
			}
		}
		return null;
	}

}
