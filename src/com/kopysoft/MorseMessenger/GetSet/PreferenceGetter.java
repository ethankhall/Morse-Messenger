package com.kopysoft.MorseMessenger.GetSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import android.preference.PreferenceManager;

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

public class PreferenceGetter {
    Context appContext = null;

    /** Constructor for PreferenceGetter
     *
     * @param appContext Application Context
     */
    public PreferenceGetter(Context appContext){
        this.appContext = appContext;
    }

    /** Method for getPlayBody()
     *
     * @return boolean
     * <ul>
     * 	<li>True - 	Message out the body</li>
     * 	<li>False - Do not message the body</li>
     * </ul>
     *
     */
    public boolean isPlayBody(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return app_preferences.getBoolean("playBody", false);
    }

    /** Method for getPlaySender()
     *
     * @return boolean
     * <ul>
     * 	<li>True - 	Message out the sender</li>
     * 	<li>False - Do not message the sender</li>
     * </ul>
     *
     */
    public boolean isPlaySender(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return app_preferences.getBoolean("playSender", false);
    }

    /** Method for getMorseSpeed()
     *
     * @return int of the speed
     *
     */
    public int getMorseSpeed(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        final int Offset = 150;
        return Offset - (app_preferences.getInt("MorseSpeed", 65) + 30);
    }

    /** Method for getMorseSpeedVibrate()
     *
     * @return int of the speed
     *
     */
    public int getMorseSpeedVibrate(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        final int Offset = 150;
        return Offset - (app_preferences.getInt("MorseSpeedVib",75) + 30);
    }

    /** Method for getMorseTone()
     *
     * @return int 	returns the real value from the preference
     *
     */
    public int getMorseToneReal(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return app_preferences.getInt("MorseTone", 7);
    }

    /** Method for getMorseTone()
     *
     * @param int	Value to convert to ToneGenerator
     * @return int 	returns a ToneGenerator
     */
    public static int getMorseTone(int getConvert){
        int[] ToneArray = { ToneGenerator.TONE_DTMF_1, ToneGenerator.TONE_DTMF_4,
                ToneGenerator.TONE_DTMF_7, ToneGenerator.TONE_DTMF_S, ToneGenerator.TONE_DTMF_2,
                ToneGenerator.TONE_DTMF_5, ToneGenerator.TONE_DTMF_8, ToneGenerator.TONE_DTMF_0,
                ToneGenerator.TONE_DTMF_3, ToneGenerator.TONE_DTMF_6, ToneGenerator.TONE_DTMF_9,
                ToneGenerator.TONE_DTMF_P, ToneGenerator.TONE_DTMF_A, ToneGenerator.TONE_DTMF_B,
                ToneGenerator.TONE_DTMF_C, ToneGenerator.TONE_DTMF_D };
        return ToneArray[getConvert];
    }

    /** Method for isPlayInVib()
     *
     * @return boolean
     * <ul>
     * 	<li>True - 	Message out the sender</li>
     * 	<li>False - Do not message the sender</li>
     * </ul>
     *
     */
    public boolean isPlayInVib(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return app_preferences.getBoolean("playInVibrate", false);
    }

    /** Method for isPlayInNrom()
     *
     * @return boolean
     * <ul>
     * 	<li>True - 	Message out the sender</li>
     * 	<li>False - Do not message the sender</li>
     * </ul>
     *
     */
    public boolean isPlayInNorm(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return app_preferences.getBoolean("playInNormal", false);
    }

    /** Method isSMSEnabled()
     *
     * @return True if SMS reading is enabled, False otherwise.
     */
    public boolean isSMSEnabled(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return app_preferences.getBoolean("enableSMS", false);
    }

    /** Method isWidgetEnabled()
     *
     * @return True is widget is enabled, False otherwise
     */
    public boolean isWidgetEnabled(){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        return app_preferences.getBoolean("widgetEnable", isSMSEnabled());
    }

    /** Method isWidgetEnabled()
     *
     * @return True is widget is enabled, False otherwise
     */
    public void setWidgetEnabled(boolean setEnabled){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putBoolean("widgetEnable", setEnabled);
        editor.commit();
    }

    /** Method needToDisplayULA()
     *
     * @return True if the ULA needs to be displayed
     */
    public boolean needToDisplayULA(int versionNumber){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        int mostRecentVersion = app_preferences.getInt("mostReventVersion", 0);
        boolean needToDisplayULA = false;
        if(mostRecentVersion != versionNumber){
            needToDisplayULA = true;
        }

        return needToDisplayULA;
    }

    public void updateULA(int versionNumber){
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putInt("mostReventVersion", versionNumber);
        editor.commit();
    }


}
