package com.kopysoft.MorseMessenger;

/* The following code was written by Matthew Wiggins and modified heavily by Ethan Hall
 * and is released under the APACHE 2.0 license 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 */

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{
	private static final String androidns="http://schemas.android.com/apk/res/android";
	private SeekBar mSeekBar;
	//private Context mContext;
	private final static String TAG = Defines.TAG + " - SeekBarPref";
	private int mValue, mDefault, mMax = 0;
	private String message = "";


	public SeekBarPreference(Context context, AttributeSet attrs) { 
		super(context,attrs); 
		//mContext = context;
		mDefault = attrs.getAttributeIntValue(androidns,"defaultValue", 0);
		mMax = attrs.getAttributeIntValue(androidns,"max", 150);
		message = attrs.getAttributeValue(androidns,"text");
		
	}
	@Override 
	protected View onCreateDialogView() {

		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		View view = layoutInflater.inflate(R.layout.slider_preferences, null);

		if (shouldPersist())
			mValue = getPersistedInt(mDefault);

		mSeekBar = (SeekBar) view.findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(this);
		
		String[] setMessage = message.split("/");
		TextView tv = (TextView)view.findViewById(R.id.textView1);
		tv.setText(setMessage[0]);
		
		tv = (TextView)view.findViewById(R.id.textView2);
		tv.setText(setMessage[1]);
		
		mSeekBar.setMax(mMax);
		return view;        
	}

	@Override 
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		mSeekBar.setProgress(mValue);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue){
		super.onSetInitialValue(restore, defaultValue);
		Log.d(TAG, "Should Persist: " + shouldPersist());
		Log.d(TAG, "Persisted Int: " + getPersistedInt(75));

		if (restore) 
			mValue = shouldPersist() ? getPersistedInt(75) : 0;
			else 
				mValue = (Integer)defaultValue;
	}

	@Override
	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch){
		Log.d(TAG, "Progress Value: " + value); 
		if (shouldPersist())
			persistInt(value);
		callChangeListener(new Integer(value));
	}

	public void onStartTrackingTouch(SeekBar seek) {}
	public void onStopTrackingTouch(SeekBar seek) {}

	public void setProgress(int progress) { 
		mValue = progress;
		if (mSeekBar != null)
			mSeekBar.setProgress(progress); 
	}
	public int getProgress() { return mValue; }
}