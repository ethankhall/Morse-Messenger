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

import java.util.HashMap;
import java.util.Map;

/** Holds a dictionary of all the Morse Code translations
 * 
 * @author Ethan Hall
 */
public class StringMap {

	Map<String, String> m = new HashMap<String, String>();
	public StringMap(){
		m.put("A", ".-");
        m.put("B", "-...");
        m.put("C", "-.-.");
        m.put("D", "-..");
        m.put("E", ".");
        m.put("F","..-.");
        m.put("G", "--.");
        m.put("H", "....");
        m.put("I", "..");
        m.put("J", ".---");
        m.put("K", "-.-");
        m.put("L",".-..");
        m.put("M", "--");
        m.put("N", "-.");
        m.put("O", "---");
        m.put("P", ".--.");
        m.put("Q", "--.-");
        m.put("R",".-.");
        m.put("S", "...");
        m.put("T", "-");
        m.put("U", "..-");
        m.put("V", "...-");
        m.put("W", ".--");
        m.put("X","-..-");
        m.put("Y","-.--");
        m.put("Z", "--..");
        m.put("1",".----");
        m.put("2", "..---");
        m.put("3", "...--");
        m.put("4", "....-");
        m.put("5",".....");
        m.put("6", "-....");
        m.put("7", "--...");
        m.put("8", "---..");
        m.put("9","----.");
        m.put("0", "-----");
        m.put(".", ".-.-.-");
        m.put(",", "--..--");
        m.put("?", "..--..");
        m.put("'", ".----.");
        m.put("!", "-.-.--");
        m.put("/", "-..-.");
        m.put("(", "-.--.");
        m.put(")", "-.--.-");
        m.put("&", ".-...");
        m.put(":", "---...");
        m.put(";", "-.-.-.");
        m.put("=", "-...-");
        m.put("+", ".-.-.");
        m.put("-", "-....-");
        m.put("_", "..--.-");
        m.put("\"", ".-..-.");
        m.put("$", "...-..-");
        m.put("@", ".--.-.");
        m.put(" ", " ");
	}
	
	/** Description of getVal(String value)
	 * 
	 * @param value	A single character string to get the Morse Code translation of 
	 * @return A string that is the translation of Morse Code, if not found the value is null
	 */
	public String getVal(String value){
		if(m.containsKey(value))
			return m.get(value);
		else
			return " ";
	}
	
	/** Description of getVal(char value)
	 * 
	 * @param value	A single character to get the Morse Code translation of 
	 * @return A string that is the translation of Morse Code, if not found the value is null
	 */
	public String getVal(char value){
		if(m.containsKey(Character.toString(value)))
			return m.get(value);
		else
			return " ";
	}
}
