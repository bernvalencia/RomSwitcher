package com.grarak.romswitcher.Fragment;

import com.grarak.romswitcher.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AboutUsFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.about_us_header);
	}
}
