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

package com.kopysoft.MorseMessenger;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.kopysoft.MorseMessenger.GetSet.PreferenceGetter;

public class MorseMessenger extends Activity {
	/** Called when the activity is first created. */
	private static String TAG = Defines.TAG + " - Main";
	private static final boolean printDebugMessages = Defines.printDebugMessages;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		PreferenceGetter pg = new PreferenceGetter(getApplicationContext());
		SeekBar speedBar = (SeekBar) findViewById(R.id.seekBar1);
		speedBar.setProgress(pg.getMorseSpeed());

		//SeekBar vibSpeedBar = (SeekBar) findViewById(R.id.seekBar2);
		//vibSpeedBar.setProgress(pg.getMorseSpeedViberate());

		SeekBar toneBar = (SeekBar) findViewById(R.id.seekBar3);
		toneBar.setProgress(pg.getMorseToneReal());
		
		// Look up the AdView as a resource and load a request.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    adView.loadAd(new AdRequest());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.prefs:
			showPrefs();
			return true;
		case R.id.help:
			showHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/** Describes the showPrefs() method
	 * 
	 * 	Shows the preference dialog
	 */
	public void showPrefs(){
		Intent prefIntent = new Intent(this, com.kopysoft.MorseMessenger.Preferences.class);
		startActivity(prefIntent);
	}

	/** Describes the method showHelp()
	 * 
	 * 	Shows the help menu
	 */
	public void showHelp(){
		return;
	}

	public void onClick(View view){
		if(printDebugMessages) Log.d(TAG, "onClick");
		//SeekBar speedBar = 	(SeekBar) findViewById(R.id.seekBar1);
		//SeekBar vibSpeedBar = (SeekBar) findViewById(R.id.seekBar2);
		//SeekBar toneBar = 	(SeekBar) findViewById(R.id.seekBar3);
		EditText messageView = (EditText) findViewById(R.id.editText1);

		//int speed = 150 - (30 + speedBar.getProgress());
		//int vibSpeed = 200 - (30 + vibSpeedBar.getProgress());
		//int tone = toneBar.getProgress();
		String message = messageView.getText().toString();

		if(printDebugMessages) Log.d(Defines.TAG, message);
		/*Intent runIntent = new Intent().setClass(this, 
				com.kopysoft.MorseMessenger.recieve.PlayMessage.class);
    	runIntent.putExtra("message", message);
    	runIntent.putExtra("speed", speed);
    	runIntent.putExtra("tone", PreferenceGetter.getMorseTone(tone));
    	runIntent.putExtra("viberateSpeed", vibSpeed);
    	if(view.getId() == R.id.button2)
    		runIntent.putExtra("viberate", true);
		getApplicationContext().startService(runIntent);*/
		playLocally singALong = new playLocally();
		singALong.execute(message);

	}

	private class playLocally extends AsyncTask<String, String, Void> {
		int speed = 0;
		int tone = 0;

		protected void onPreExecute(){
			WebView singALong = (WebView) findViewById(R.id.singALong);
			singALong.setVisibility(WebView.VISIBLE);
			singALong.loadData("<br/><br/>", "text/html", "UTF-8");

			SeekBar speedBar = 	(SeekBar) findViewById(R.id.seekBar1);
			SeekBar toneBar = 	(SeekBar) findViewById(R.id.seekBar3);

			speed = 150 - (30 + speedBar.getProgress());
			tone = PreferenceGetter.getMorseTone(toneBar.getProgress());
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
			WebView singALong = (WebView) findViewById(R.id.singALong);
			singALong.loadData(progress[0], "text/html", "UTF-8");
		}

		protected void onPostExecute(Void param) {
			WebView singALong = (WebView) findViewById(R.id.singALong);
			singALong.setVisibility(WebView.INVISIBLE);
			singALong.loadData("<br/><br/>", "text/html", "UTF-8");
		}

		@Override
		protected Void doInBackground(String... params) {
			ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
			String text = params[0];
			char[] messageText = text.toCharArray();
			String[] message = new String[messageText.length];
			StringMap map = new StringMap();

			for(int i = 0; i < messageText.length; i++){
				message[i] = map.getVal(Character.toString((char)messageText[i]));
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

							if(((char)brokenString[j]) == ' '){
								//tg.startTone(ToneGenerator.TONE_CDMA_SIGNAL_OFF, timeToPlay);
							} else {
								tg.startTone(tone, timeToPlay);
							}

							Thread.sleep(timeToPlay);
							//tg.startTone(ToneGenerator.TONE_CDMA_SIGNAL_OFF, delay);
							Thread.sleep(speed);
						}
						//tg.startTone(ToneGenerator.TONE_CDMA_SIGNAL_OFF, delay * 3);
						Thread.sleep(speed * 3);
					}
				}
				String messageToSend = getHTML(messageText.length, message, messageText, false);
				publishProgress(messageToSend);
				Thread.sleep(speed * 14);
			} catch(Exception e){
				Log.e(TAG, "Thread Error");
			}
			return null;
		}

	}
}