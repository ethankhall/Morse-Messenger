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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Row2 extends RelativeLayout{

	private Context mContext;

	TextView rightText, leftText;

	public TextView left(){
		return leftText;
	}

	public TextView right(){
		return rightText;
	}   

	public Row2(Context context){
		super(context);
		mContext = context;

		setPadding(10, 0, 10, 0);

		leftText = new TextView(mContext);
		leftText.setText("right");
		leftText.setTextSize(18);

		rightText = new TextView(mContext);
		rightText.setText("left");
		rightText.setTextSize(18);

		//Set up the relitive layout
		RelativeLayout.LayoutParams left = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);

		left.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


		RelativeLayout.LayoutParams right = 
			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);

		right.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		right.addRule(RelativeLayout.RIGHT_OF, leftText.getId());

		addView(leftText, left);
		addView(rightText, right);
	}
}
