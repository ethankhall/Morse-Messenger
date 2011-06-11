package com.kopysoft.MorseMessenger.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kopysoft.MorseMessenger.Defines;
import com.kopysoft.MorseMessenger.StringMap;

public class ViewTranslationsAdapter extends BaseAdapter{
	
	private static String TAG = Defines.TAG + " - ViewTranslations";
	private static final boolean printDebugMessages = Defines.printDebugMessages;
	
	String mMessage = null;
	StringMap map = null;
	Context gContext = null;
	
	public ViewTranslationsAdapter(String message, Context context){
		mMessage = message;
		gContext = context;
		map = new StringMap();
	}

	@Override
	public int getCount() {
		return mMessage.length();
	}

	@Override
	public Object getItem(int position) {
		return mMessage.charAt(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(printDebugMessages) Log.d(TAG, "Position: " + position);

		String left = "";
		String right = "";
		Row2 returnValue = null;
		
		if(convertView == null){
			returnValue = new Row2(gContext);
		} else {
			returnValue = (Row2) convertView;
		}
		
		TextView leftView = returnValue.left();
		TextView rightView = returnValue.right();

		left = Character.toString(mMessage.charAt(position));
		right = map.getVal(Character.toString(mMessage.charAt(position)).toUpperCase());

		leftView.setText(left);
		rightView.setText(right);
		return returnValue; 
	}
}
