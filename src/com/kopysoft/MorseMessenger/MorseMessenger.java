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
import com.kopysoft.MorseMessenger.GetSet.PreferenceGetter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

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
        
        SeekBar vibSpeedBar = (SeekBar) findViewById(R.id.seekBar2);
        vibSpeedBar.setProgress(pg.getMorseSpeedViberate());
        
        SeekBar toneBar = (SeekBar) findViewById(R.id.seekBar3);
        toneBar.setProgress(pg.getMorseToneReal());
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
    	SeekBar speedBar = 	(SeekBar) findViewById(R.id.seekBar1);
    	SeekBar vibSpeedBar = (SeekBar) findViewById(R.id.seekBar2);
    	SeekBar toneBar = 	(SeekBar) findViewById(R.id.seekBar3);
    	EditText messageView = (EditText) findViewById(R.id.editText1);
    	
    	int speed = 150 - (30 + speedBar.getProgress());
    	int vibSpeed = 200 - (30 + vibSpeedBar.getProgress());
    	int tone = toneBar.getProgress();
    	String message = messageView.getText().toString();
    	
    	Log.d(Defines.TAG, message);
    	Intent runIntent = new Intent().setClass(this, 
				com.kopysoft.MorseMessenger.recieve.PlayMessage.class);
    	runIntent.putExtra("message", message);
    	runIntent.putExtra("speed", speed);
    	runIntent.putExtra("tone", PreferenceGetter.getMorseTone(tone));
    	runIntent.putExtra("viberateSpeed", vibSpeed);
    	if(view.getId() == R.id.button2)
    		runIntent.putExtra("viberate", true);
		getApplicationContext().sendBroadcast(runIntent);
    	
    }
}