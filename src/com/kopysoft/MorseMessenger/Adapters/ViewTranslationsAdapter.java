package com.kopysoft.MorseMessenger.Adapters;

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

	public int getCount() {
		return mMessage.length();
	}

	public Object getItem(int position) {
		return mMessage.charAt(position);
	}

	public long getItemId(int position) {
		return position;
	}

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
