package com.kopysoft.MorseMessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

public class mainUI extends Activity {
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
    }
   
    public void onClick(View view){
    	//MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.a);
    	//mp.start();
    	SeekBar speedBar = (SeekBar) findViewById(R.id.seekBar1);
    	EditText messageView = (EditText) findViewById(R.id.editText1);
    	
    	int speed = speedBar.getProgress();
    	String message = messageView.getText().toString();
    	
    	Log.d(Defines.TAG, message);
    	Intent runIntent = new Intent().setClass(getApplicationContext(), 
				com.kopysoft.MorseMessenger.PlayMessage.class);
    	runIntent.putExtra("message", message);
    	runIntent.putExtra("speed", speed);
		getApplicationContext().sendBroadcast(runIntent);
    	
    }
}