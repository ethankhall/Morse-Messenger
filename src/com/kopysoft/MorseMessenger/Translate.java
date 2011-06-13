package com.kopysoft.MorseMessenger;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kopysoft.MorseMessenger.GetSet.PreferenceGetter;
import com.kopysoft.MorseMessenger.UIService.PlayLocally;
import com.kopysoft.MorseMessenger.UIService.VibrateLocally;

public class Translate extends Activity {

	private static String TAG = Defines.TAG + " - Translate";
	private static final boolean printDebugMessages = Defines.printDebugMessages;
	private boolean translateMorse = false;

	PlayLocally playAlong = null;
	VibrateLocally VibrateAlong = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view0);

		PreferenceGetter pg = new PreferenceGetter(getApplicationContext());

		//Layout Inflater for all the view
		SeekBar speedBar = (SeekBar) findViewById(R.id.seekBarSpeed);
		speedBar.setProgress(pg.getMorseSpeed());

		SeekBar toneBar = (SeekBar) findViewById(R.id.seekBarTone);
		toneBar.setProgress(pg.getMorseToneReal());
	}

	/** Method for button presses
	 * 
	 * This method will check the 3 buttons on this activity. If any button is currently 
	 * 	being pressed then it will cancel all the other ones, then start the new one. 
	 * 
	 * @author Ethan Hall
	 * @param view
	 */
	public void onClick(View view){
		if(printDebugMessages) Log.d(TAG, "onClick");

		if(view.getId() == R.id.playMorse){
			if(playAlong == null ){
				playAlong = new PlayLocally(this);
				playAlong.execute();
				
			} else if(playAlong.getStatus() == AsyncTask.Status.FINISHED){
				playAlong = new PlayLocally(this);
				playAlong.execute();
				
			} else {
				playAlong.cancel(true);
				playAlong = null;
				onCancel();
			}
			
		}else if(view.getId() == R.id.vibrateMorse) {
			if(VibrateAlong == null){
				VibrateAlong = new VibrateLocally(this);
				VibrateAlong.execute(getApplicationContext());
				
			} else if(VibrateAlong.getStatus() == AsyncTask.Status.FINISHED) {
				VibrateAlong = new VibrateLocally(this);
				VibrateAlong.execute(getApplicationContext());
				
			} else {
				VibrateAlong.cancel(true);
				VibrateAlong = null;
				onCancel();
			}

		} else if(view.getId() == R.id.translateMorse){
			if(translateMorse){
				translateMorse = false;
				onCancel();
			} else {
				EditText messageView = (EditText) findViewById(R.id.messageInput);
				String message = messageView.getText().toString();
				messageView.setEnabled(false);
				
				Button playButton = (Button)findViewById(R.id.playMorse);
				playButton.setEnabled(false);
				
				Button VibrateButton = (Button)findViewById(R.id.vibrateMorse);
				VibrateButton.setEnabled(false);
				
				Button translateButton = (Button)findViewById(R.id.translateMorse);
				translateButton.setText("Hide");
				translateButton.setEnabled(true);
				
				LinearLayout seekbars = (LinearLayout)findViewById(R.id.SeekbarView);
				seekbars.setVisibility(View.GONE);
				
				WebView singALong = (WebView) findViewById(R.id.singALong);
				singALong.setVisibility(WebView.VISIBLE);
				singALong.loadData(createHTML(message), "text/html", "UTF-8");
				translateMorse = true;
			}
		}


	}

	private void onCancel(){
		if(printDebugMessages) Log.d(TAG, "onCancelled()");

		WebView singALong = (WebView) findViewById(R.id.singALong);
		singALong.setVisibility(WebView.INVISIBLE);
		singALong.loadData("<br/><br/>", "text/html", "UTF-8");

		Button playButton = (Button)findViewById(R.id.playMorse);
		playButton.setText("Play");
		playButton.setEnabled(true);
		
		Button VibrateButton = (Button)findViewById(R.id.vibrateMorse);
		VibrateButton.setText("Vibrate");
		VibrateButton.setEnabled(true);
		
		Button translateButton = (Button)findViewById(R.id.translateMorse);
		translateButton.setText("Translate");
		translateButton.setEnabled(true);
		
		LinearLayout seekbars = (LinearLayout)findViewById(R.id.SeekbarView);
		seekbars.setVisibility(View.VISIBLE);
		
		EditText messageView = (EditText) findViewById(R.id.messageInput);
		messageView.setEnabled(true);

		//set up Toast
		/*
		Context context = getApplicationContext();
		CharSequence text = "Message Canceled!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		*/
	}
	
	private String createHTML(String messageIn){
		String returnValue = "";
		char[] messageText = messageIn.toCharArray();
		String[] message = new String[messageText.length];
		StringMap map = new StringMap();
		

		for(int i = 0; i < messageText.length; i++){
			message[i] = map.getVal(Character.toString((char)messageText[i]).toUpperCase());
			if(printDebugMessages) Log.d(TAG, "Message[" + i + "]: " + messageText[i]);
		}
		
		returnValue = getHTML(message, messageText);
		
		return returnValue;
	}
	
	private String getHTML(String[] message, char[] messageText){
		String tempOutput = "<center><table cellpadding=1>";
		String topRow = "<tr>";
		String bottomRow = "<tr>";


		for(int i = 0; i < message.length; i++){
			String topTemp = Character.toString(messageText[i]);
			String bottomTemp = message[i];
			topRow += "<td><center>" + topTemp + "</center></td>";
			bottomRow += "<td nowrap style=\" padding-left: 1em; \"><center>" + bottomTemp + "</center></td>";
		}
		topRow += "</tr>";
		bottomRow += "</tr>";
		tempOutput += topRow + bottomRow + "</table></center>";
		return tempOutput;
	}
}
