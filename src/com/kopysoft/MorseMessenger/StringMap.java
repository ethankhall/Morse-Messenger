package com.kopysoft.MorseMessenger;

import java.util.HashMap;
import java.util.Map;

public class StringMap {

	Map<String, String> m = new HashMap<String, String>();
	StringMap(){
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
        m.put(" ", "...");
	}
	
	public String getVal(String value){
		if(m.containsKey(value))
			return m.get(value);
		else
			return null;
	}
}
