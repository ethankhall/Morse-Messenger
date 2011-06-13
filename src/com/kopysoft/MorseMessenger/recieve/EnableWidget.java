package com.kopysoft.MorseMessenger.recieve;

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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.kopysoft.MorseMessenger.Defines;
import com.kopysoft.MorseMessenger.R;
import com.kopysoft.MorseMessenger.GetSet.PreferenceGetter;

public class EnableWidget extends AppWidgetProvider {
	
	private static String UPDATE_ICON = "com.kopysoft.MorseMessenger.recieve.EnableWidget.UPDATE_ICON";
	private static String TAG = Defines.TAG + " - Widget";
	private static final boolean printDebugMessages = Defines.printDebugMessages;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            
            //Intent intent = new Intent(context, com.kopysoft.MorseMessenger.EnableWidget.class);
            Intent intent = new Intent(context, com.kopysoft.MorseMessenger.recieve.EnableWidget.class);
            intent.setAction(UPDATE_ICON);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            
            PreferenceGetter prefs = new PreferenceGetter(context);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            if(prefs.isWidgetEnabled()){
            	views.setImageViewResource(R.id.imageButton, R.drawable.ic_widget_enable);
            } else {
            	views.setImageViewResource(R.id.imageButton, R.drawable.ic_widget_disable);
            }
            views.setOnClickPendingIntent(R.id.imageButton, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }        
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if(intent.getAction().compareTo(UPDATE_ICON) == 0){
			if(printDebugMessages) Log.d(TAG, "Update Icon");
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			
			PreferenceGetter prefs = new PreferenceGetter(context);
			boolean enabled = !(prefs.isWidgetEnabled());
			prefs.setWidgetEnabled(enabled);
			if(enabled){
            	views.setImageViewResource(R.id.imageButton, R.drawable.ic_widget_enable);
            } else {
            	views.setImageViewResource(R.id.imageButton, R.drawable.ic_widget_disable);
            }
			
			manager.updateAppWidget(new ComponentName(context, EnableWidget.class), views);
		}
		if(printDebugMessages) Log.d(TAG,intent.getAction());
	}
	
	/** Method onDeleted (Context context, int[] appWidgetIds)
	 * 
	 * 	Called when the app is deleted
	 */
	public void onDeleted (Context context, int[] appWidgetIds){
		super.onDeleted(context, appWidgetIds);
		PreferenceGetter prefs = new PreferenceGetter(context);
		prefs.setWidgetEnabled(false);
	}
}
