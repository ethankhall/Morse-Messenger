package com.kopysoft.MorseMessenger;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListAdapter;

import com.kopysoft.MorseMessenger.Adapters.ViewTranslationsAdapter;

public class ViewTranslations extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.view1);
	  String list = "abcdefghijklmnopqrstuvwxyz0123456789.,?'!/()&:;=+-_\"$@";
	  setListAdapter((ListAdapter) new ViewTranslationsAdapter(list, getApplicationContext()));
	}
}
